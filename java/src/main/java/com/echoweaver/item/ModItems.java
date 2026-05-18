package com.echoweaver.item;

import com.echoweaver.EchoWeaver;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item THREAD_OF_RESONANCE = register("thread_of_resonance",
            new Item(new Item.Settings()));
    public static final Item SOURCE_CRYSTAL = register("source_crystal",
            new Item(new Item.Settings()));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EchoWeaver.MOD_ID, name), item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(THREAD_OF_RESONANCE);
            entries.add(SOURCE_CRYSTAL);
        });
        EchoWeaver.LOGGER.info("Registered items");
    }
}
