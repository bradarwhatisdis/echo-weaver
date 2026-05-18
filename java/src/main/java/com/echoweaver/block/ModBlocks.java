package com.echoweaver.block;

import com.echoweaver.EchoWeaver;
import com.echoweaver.block.entity.LoomOfTimeBlockEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block LOOM_OF_TIME = register("loom_of_time",
            new LoomOfTimeBlock(Block.Settings.create().strength(3.0f).requiresTool()));

    public static final BlockEntityType<LoomOfTimeBlockEntity> LOOM_OF_TIME_ENTITY =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(EchoWeaver.MOD_ID, "loom_of_time"),
                    FabricBlockEntityTypeBuilder.create(LoomOfTimeBlockEntity::new, LOOM_OF_TIME).build()
            );

    private static Block register(String name, Block block) {
        Identifier id = Identifier.of(EchoWeaver.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(LOOM_OF_TIME);
        });
        EchoWeaver.LOGGER.info("Registered blocks");
    }
}
