package com.restonic4.under_control.mixin.general.common;

import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {SculkSensorBlock.class, SculkShriekerBlock.class})
public class SculkLikeBlockMixin {
    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    private void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo ci) {
        EventResult eventResult = BlockEvents.SCULK_LIKE_STEPPED.invoker().onSculkLikeStepped(level, blockState, blockPos, entity);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
