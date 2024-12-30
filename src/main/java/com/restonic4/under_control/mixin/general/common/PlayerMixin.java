package com.restonic4.under_control.mixin.general.common;

import com.restonic4.under_control.events.EventResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.restonic4.under_control.events.types.SeverPlayerExtraEvents;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "touch", at = @At("HEAD"), cancellable = true)
    private void touch(Entity entity, CallbackInfo ci) {
        EventResult eventResult = SeverPlayerExtraEvents.TOUCH_ENTITY.invoker().onTouchEntity((Player) (Object) this, entity);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }

    @Inject(method = "canBeHitByProjectile", at = @At("HEAD"), cancellable = true)
    public void canBeHitByProjectile(CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = SeverPlayerExtraEvents.CAN_BEW_HIT_BY_PROJECTILES.invoker().onCanBeHitByProjectiles((Player) (Object) this);
        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
