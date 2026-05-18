package com.echoweaver.echo;

import com.echoweaver.entity.EchoEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;

public class EchoPlayback {
    private final EchoEntity entity;
    private final EchoRecording recording;
    private final Iterator<RecordedAction> iterator;
    private RecordedAction nextAction;
    private long tickCounter = 0;

    public EchoPlayback(EchoEntity entity, EchoRecording recording) {
        this.entity = entity;
        this.recording = recording;
        this.iterator = recording.getActions().iterator();
        this.nextAction = iterator.hasNext() ? iterator.next() : null;
    }

    public boolean tick(ServerWorld world) {
        if (nextAction == null) return true;

        while (nextAction != null && nextAction.tick() <= tickCounter) {
            executeAction(world, nextAction);
            nextAction = iterator.hasNext() ? iterator.next() : null;
        }

        tickCounter++;
        return nextAction == null;
    }

    private void executeAction(ServerWorld world, RecordedAction action) {
        BlockPos pos = action.pos();
        switch (action.type()) {
            case BLOCK_PLACE -> {
                world.setBlockState(pos, action.state(), Block.NOTIFY_ALL);
                world.playSound(null, pos, action.state().getSoundGroup().getPlaceSound(),
                        SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            case BLOCK_BREAK -> {
                BlockState current = world.getBlockState(pos);
                if (!current.isAir()) {
                    world.breakBlock(pos, true);
                }
            }
        }
    }
}
