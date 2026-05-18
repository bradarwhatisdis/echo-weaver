package com.echoweaver.echo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public record RecordedAction(Type type, BlockPos pos, BlockState state, long tick) {
    public enum Type {
        BLOCK_PLACE,
        BLOCK_BREAK
    }

    public NbtCompound toNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound tag = new NbtCompound();
        tag.putInt("Type", type.ordinal());
        tag.put("Pos", NbtHelper.fromBlockPos(pos));
        tag.put("State", NbtHelper.fromBlockState(state, registries));
        tag.putLong("Tick", tick);
        return tag;
    }

    public static RecordedAction fromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries) {
        Type type = Type.values()[tag.getInt("Type")];
        BlockPos pos = NbtHelper.toBlockPos(tag.getCompound("Pos"));
        BlockState state = NbtHelper.toBlockState(registries, tag.getCompound("State"));
        long tick = tag.getLong("Tick");
        return new RecordedAction(type, pos, state, tick);
    }
}
