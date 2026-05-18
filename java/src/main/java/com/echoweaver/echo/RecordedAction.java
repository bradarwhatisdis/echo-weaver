package com.echoweaver.echo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record RecordedAction(Type type, BlockPos pos, BlockState state, long tick) {
    public enum Type {
        BLOCK_PLACE,
        BLOCK_BREAK
    }

    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("Type", type.ordinal());
        tag.putLong("Pos", pos.asLong());
        tag.putString("Block", Registries.BLOCK.getId(state.getBlock()).toString());
        tag.putInt("BlockState", Block.getRawIdFromState(state));
        tag.putLong("Tick", tick);
        return tag;
    }

    public static RecordedAction fromNbt(NbtCompound tag) {
        Type type = Type.values()[tag.getInt("Type")];
        BlockPos pos = BlockPos.fromLong(tag.getLong("Pos"));
        BlockState state = Block.getStateFromRawId(tag.getInt("BlockState"));
        if (state.isAir() && tag.contains("Block")) {
            Identifier id = Identifier.of(tag.getString("Block"));
            state = Registries.BLOCK.get(id).getDefaultState();
        }
        long tick = tag.getLong("Tick");
        return new RecordedAction(type, pos, state, tick);
    }
}
