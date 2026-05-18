package com.echoweaver.echo;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RecordingManager {
    private static final Map<UUID, EchoRecording> activeRecordings = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> recordingStartTicks = new ConcurrentHashMap<>();

    public static void startRecording(ServerPlayerEntity player) {
        activeRecordings.put(player.getUuid(), new EchoRecording());
        recordingStartTicks.put(player.getUuid(), player.getWorld().getTime());
    }

    public static EchoRecording stopRecording(ServerPlayerEntity player) {
        EchoRecording recording = activeRecordings.remove(player.getUuid());
        recordingStartTicks.remove(player.getUuid());
        return recording;
    }

    public static boolean isRecording(ServerPlayerEntity player) {
        return activeRecordings.containsKey(player.getUuid());
    }

    public static void recordBlockPlace(ServerPlayerEntity player, BlockPos pos) {
        EchoRecording recording = activeRecordings.get(player.getUuid());
        if (recording != null) {
            long tick = player.getWorld().getTime() - recordingStartTicks.get(player.getUuid());
            recording.addAction(new RecordedAction(RecordedAction.Type.BLOCK_PLACE, pos,
                    player.getWorld().getBlockState(pos), tick));
        }
    }

    public static void recordBlockBreak(ServerPlayerEntity player, BlockPos pos, World world) {
        EchoRecording recording = activeRecordings.get(player.getUuid());
        if (recording != null) {
            long tick = world.getTime() - recordingStartTicks.get(player.getUuid());
            recording.addAction(new RecordedAction(RecordedAction.Type.BLOCK_BREAK, pos,
                    world.getBlockState(pos), tick));
        }
    }
}
