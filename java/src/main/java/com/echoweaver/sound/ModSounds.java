package com.echoweaver.sound;

import com.echoweaver.EchoWeaver;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent LOOM_WEAVE = register("loom_weave");
    public static final SoundEvent ECHO_ACTIVATE = register("echo_activate");
    public static final SoundEvent ECHO_FADE = register("echo_fade");
    public static final SoundEvent RESONANCE_AMBIENT = register("resonance_ambient");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of(EchoWeaver.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize() {
        EchoWeaver.LOGGER.info("Registered sounds");
    }
}
