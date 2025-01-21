package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.OtherEvents;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    public void explode(CallbackInfo ci) {
        if (OtherEvents.EXPLODE.invoker().onExplode((Explosion) (Object) this) == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
