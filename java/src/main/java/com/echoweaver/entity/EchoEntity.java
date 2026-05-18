package com.echoweaver.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class EchoEntity extends MobEntity {
    private static final TrackedData<Integer> ECHO_COLOR = DataTracker.registerData(EchoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_LEFT = DataTracker.registerData(EchoEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public EchoEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ECHO_COLOR, 0x88CCFF);
        builder.add(TIME_LEFT, 1200);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
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
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("EchoColor", dataTracker.get(ECHO_COLOR));
        nbt.putInt("TimeLeft", dataTracker.get(TIME_LEFT));
    }

    @Override
    public boolean canImmediatelyDeserialize(double distanceSq) {
        return false;
    }
}
