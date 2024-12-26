package com.restonic4.under_control.mixin.vanish;

import com.restonic4.under_control.api.vanish.VanishAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.Listener.class)
public class VibrationSystemMixin {
    @Inject(method = "handleGameEvent", at = @At("HEAD"), cancellable = true)
    private void preventEntityVibrations(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3, CallbackInfoReturnable<Boolean> cir) {
        Entity sourceEntity = context.sourceEntity();
        if (sourceEntity != null && VanishAPI.isVanished(sourceEntity)) {
            cir.setReturnValue(false);
        }
    }
}
