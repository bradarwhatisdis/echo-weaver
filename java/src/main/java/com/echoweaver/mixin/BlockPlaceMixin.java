package com.echoweaver.mixin;

import com.echoweaver.echo.RecordingManager;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class BlockPlaceMixin {
    @Inject(method = "tryPlaceBlock", at = @At("RETURN"))
    private void onBlockPlaced(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue() == ActionResult.SUCCESS) {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            if (RecordingManager.isRecording(player)) {
                BlockPos pos = context.getBlockPos();
                RecordingManager.recordBlockPlace(player, pos);
            }
        }
    }
}
