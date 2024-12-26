package com.restonic4.under_control.mixin.general.common;

import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.LivingEntityExtraEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void isPushable(CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = LivingEntityExtraEvents.PUSHING.invoker().onPushing((LivingEntity) (Object) this);
        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
