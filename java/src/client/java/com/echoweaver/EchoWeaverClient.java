package com.echoweaver;

import net.fabricmc.api.ClientModInitializer;

public class EchoWeaverClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EchoWeaver.LOGGER.info("Echo Weaver client initialized");
    }
}
