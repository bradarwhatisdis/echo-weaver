package com.echoweaver.echo;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;

public class EchoRecording {
    private final List<RecordedAction> actions = new ArrayList<>();
    private long totalTicks = 0;

    public void addAction(RecordedAction action) {
        actions.add(action);
        if (action.tick() > totalTicks) {
            totalTicks = action.tick();
        }
    }

    public List<RecordedAction> getActions() {
        return actions;
    }

    public long getTotalTicks() {
        return totalTicks;
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public NbtCompound toNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound tag = new NbtCompound();
        tag.putLong("TotalTicks", totalTicks);
        NbtList list = new NbtList();
        for (RecordedAction action : actions) {
            list.add(action.toNbt(registries));
        }
        tag.put("Actions", list);
        return tag;
    }

    public static EchoRecording fromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries) {
        EchoRecording recording = new EchoRecording();
        recording.totalTicks = tag.getLong("TotalTicks");
        NbtList list = tag.getList("Actions", 10);
        for (int i = 0; i < list.size(); i++) {
            recording.actions.add(RecordedAction.fromNbt(list.getCompound(i), registries));
        }
        return recording;
    }
}
