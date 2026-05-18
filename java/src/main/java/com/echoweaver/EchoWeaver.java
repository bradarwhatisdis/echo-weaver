package com.echoweaver;

import com.echoweaver.block.ModBlocks;
import com.echoweaver.entity.ModEntities;
import com.echoweaver.item.ModItems;
import com.echoweaver.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
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
        LOGGER.info("Echo Weaver initialized");
    }
}
