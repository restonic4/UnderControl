package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "checkFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"
            )
    )
    public <T extends ParticleOptions> int hideFallingParticles(ServerLevel level, T particleOptions, double x, double y, double z, int count, double dx, double dy, double dz, double speed, Operation<Integer> original) {
        EventResult result = LivingEntityExtraEvents.SPAWN_FALL_PARTICLES.invoker().onSpawnFallParticles((LivingEntity) (Object) this, level, particleOptions, x, y, z, count, dx, dy, dz, speed);

        if (result == EventResult.CANCELED) {
            return 0;
        }

        return original.call(level, particleOptions, x, y, z, count, dx, dy, dz, speed); // This is fine, Intellij idea is just being silly
    }

    @Inject(method = "canBeSeenByAnyone", at = @At("HEAD"), cancellable = true)
    public void hideFromEntities(CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = LivingEntityExtraEvents.CAN_BE_SEEN.invoker().onCanBeSeen((LivingEntity) (Object) this);

        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
