package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public abstract class WardenMixin {
    @Inject(method = "canTargetEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/warden/Warden;isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    public void excludeVanished(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = LivingEntityExtraEvents.WARDEN_PICK_TARGET.invoker().onWardenPickTarget((Warden) (Object) this, entity);
        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
