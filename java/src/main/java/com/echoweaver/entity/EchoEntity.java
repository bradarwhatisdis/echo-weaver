package com.echoweaver.entity;

import com.echoweaver.echo.EchoPlayback;
import com.echoweaver.echo.EchoRecording;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class EchoEntity extends MobEntity {
    public static final TrackedData<Integer> ECHO_COLOR = DataTracker.registerData(EchoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_LEFT = DataTracker.registerData(EchoEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private EchoRecording recording;
    private EchoPlayback playback;
    private boolean finished = false;

    public EchoEntity(EntityType<? extends EchoEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setRecording(EchoRecording recording) {
        this.recording = recording;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ECHO_COLOR, 0x88CCFF);
        builder.add(TIME_LEFT, 600);
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient) return;

        ServerWorld world = (ServerWorld) getWorld();

        if (playback == null && recording != null) {
            playback = new EchoPlayback(this, recording);
        }

        if (playback != null && !finished) {
            finished = playback.tick(world);
        }

        if (finished) {
            int time = dataTracker.get(TIME_LEFT);
            if (time <= 0) {
                discard();
            } else {
                dataTracker.set(TIME_LEFT, time - 1);
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("EchoColor")) dataTracker.set(ECHO_COLOR, nbt.getInt("EchoColor"));
        if (nbt.contains("TimeLeft")) dataTracker.set(TIME_LEFT, nbt.getInt("TimeLeft"));
        if (nbt.contains("Recording")) {
            recording = EchoRecording.fromNbt(nbt.getCompound("Recording"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("EchoColor", dataTracker.get(ECHO_COLOR));
        nbt.putInt("TimeLeft", dataTracker.get(TIME_LEFT));
        if (recording != null) {
            nbt.put("Recording", recording.toNbt());
        }
    }

}
