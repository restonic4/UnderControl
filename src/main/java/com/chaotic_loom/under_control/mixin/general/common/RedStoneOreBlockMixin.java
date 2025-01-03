package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedStoneOreBlock.class)
public class RedStoneOreBlockMixin {
    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    private void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo ci) {
        EventResult eventResult = BlockEvents.RED_STONE_ORE_PRESSED.invoker().onRedStoneOrePressed(level, blockState, blockPos, entity);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
