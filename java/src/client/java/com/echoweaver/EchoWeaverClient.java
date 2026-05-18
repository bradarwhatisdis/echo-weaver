package com.echoweaver;

import com.echoweaver.entity.ModEntities;
import com.echoweaver.render.EchoEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EchoWeaverClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ECHO, EchoEntityRenderer::new);
        EchoWeaver.LOGGER.info("Echo Weaver client initialized");
    }
}
