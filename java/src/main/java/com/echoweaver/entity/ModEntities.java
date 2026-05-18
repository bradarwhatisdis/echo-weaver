package com.echoweaver.entity;

import com.echoweaver.EchoWeaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<EchoEntity> ECHO = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(EchoWeaver.MOD_ID, "echo"),
            EntityType.Builder.create(EchoEntity::new, SpawnGroup.MISC)
                    .dimensions(0.6f, 1.8f)
                    .maxTrackingRange(10)
                    .trackingTickInterval(2)
                    .build()
    );

    public static void initialize() {
        EchoWeaver.LOGGER.info("Registered entities");
    }
}
