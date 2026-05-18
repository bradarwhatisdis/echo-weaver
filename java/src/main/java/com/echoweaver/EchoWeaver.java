package com.echoweaver;

import com.echoweaver.block.ModBlocks;
import com.echoweaver.echo.RecordingManager;
import com.echoweaver.entity.ModEntities;
import com.echoweaver.item.ModItems;
import com.echoweaver.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoWeaver implements ModInitializer {
    public static final String MOD_ID = "echoweaver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModSounds.initialize();
        ModItems.initialize();
        ModBlocks.initialize();
        ModEntities.initialize();

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (player instanceof ServerPlayerEntity serverPlayer && RecordingManager.isRecording(serverPlayer)) {
                RecordingManager.recordBlockBreak(serverPlayer, pos, world);
            }
        });

        LOGGER.info("Echo Weaver initialized");
    }
}
