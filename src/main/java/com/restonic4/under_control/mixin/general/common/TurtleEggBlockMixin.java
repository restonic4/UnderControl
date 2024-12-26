package com.restonic4.under_control.mixin.general.common;

import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {
    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallHeight, CallbackInfo ci) {
        EventResult eventResult = BlockEvents.TURTLE_EGG_TRAMPLE.invoker().onTurtleEggTrample(level, blockState, blockPos, entity, fallHeight);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }

    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    private void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo ci) {
        EventResult eventResult = BlockEvents.TURTLE_EGG_TRAMPLE.invoker().onTurtleEggTrample(level, blockState, blockPos, entity, 0);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
