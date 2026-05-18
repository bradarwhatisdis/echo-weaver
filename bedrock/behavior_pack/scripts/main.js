import { world, system, ItemStack, BlockPermutation } from "@minecraft/server";

const RECORDING_PLAYERS = new Map();

world.beforeEvents.worldInitialize.subscribe(({ blockTypeRegistry, itemComponentRegistry }) => {
    itemComponentRegistry.registerCustomComponent("echoweaver:loom_interact", {
        onUse(event) {
            const { source, block } = event;
            if (!block || block.typeId !== "echoweaver:loom_of_time") return;

            const playerId = source.id;

            if (RECORDING_PLAYERS.has(playerId)) {
                stopRecording(source, block);
            } else {
                startRecording(source, block);
            }
        }
    });
});

function startRecording(player, block) {
    const inventory = player.getComponent("inventory").container;
    let slot = -1;

    for (let i = 0; i < inventory.size; i++) {
        const item = inventory.getItem(i);
        if (item?.typeId === "echoweaver:thread_of_resonance") {
            slot = i;
            break;
        }
    }

    if (slot === -1) {
        player.sendMessage("§b[Echo Weaver]§r You need a Thread of Resonance to start recording.");
        return;
    }

    const item = inventory.getItem(slot);
    item.amount--;
    if (item.amount <= 0) {
        inventory.setItem(slot, undefined);
    } else {
        inventory.setItem(slot, item);
    }

    RECORDING_PLAYERS.set(player.id, { actions: [], startTick: system.currentTick, blockPos: block.location });
    player.sendMessage("§b[Echo Weaver]§r Recording started! Interact again to stop.");
    block.dimension.playSound("loom_weave", block.location);
}

function stopRecording(player, block) {
    const data = RECORDING_PLAYERS.get(player.id);
    RECORDING_PLAYERS.delete(player.id);

    const inventory = player.getComponent("inventory").container;
    let hasCrystal = false;

    for (let i = 0; i < inventory.size; i++) {
        const item = inventory.getItem(i);
        if (item?.typeId === "echoweaver:source_crystal") {
            hasCrystal = true;
            item.amount--;
            if (item.amount <= 0) {
                inventory.setItem(i, undefined);
            } else {
                inventory.setItem(i, item);
            }
            break;
        }
    }

    if (!hasCrystal) {
        player.sendMessage("§b[Echo Weaver]§r Recording discarded - no Source Crystal to bind the echo.");
        return;
    }

    if (data.actions.length === 0) {
        player.sendMessage("§b[Echo Weaver]§r No actions recorded.");
        return;
    }

    spawnEcho(player, block, data);
    player.sendMessage(`§b[Echo Weaver]§r Echo spawned with ${data.actions.length} recorded actions.`);
    block.dimension.playSound("echo_activate", block.location);
}

function spawnEcho(player, block, data) {
    const dimension = block.dimension;
    const location = player.location;

    dimension.spawnEntity("echoweaver:echo", location);
}

world.afterEvents.playerBreakBlock.subscribe((event) => {
    const playerId = event.player.id;
    if (RECORDING_PLAYERS.has(playerId)) {
        const data = RECORDING_PLAYERS.get(playerId);
        data.actions.push({
            type: "break",
            pos: event.block.location,
            tick: system.currentTick - data.startTick
        });
    }
});

world.afterEvents.playerPlaceBlock.subscribe((event) => {
    const playerId = event.player.id;
    if (RECORDING_PLAYERS.has(playerId)) {
        const data = RECORDING_PLAYERS.get(playerId);
        data.actions.push({
            type: "place",
            pos: event.block.location,
            block: event.block.typeId,
            states: event.block.permutation.getState(""),
            tick: system.currentTick - data.startTick
        });
    }
});
