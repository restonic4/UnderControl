package com.restonic4.under_control.mixin.general.common;

import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasePressurePlateBlock.class)
public class BasePressurePlateBlockMixin {
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        EventResult eventResult = BlockEvents.PRESSURE_PLATE_PRESSED.invoker().onPressurePlatePressed(level, blockState, blockPos, entity);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
