package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
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
        if (sourceEntity != null) {
            EventResult eventResult = LivingEntityExtraEvents.CAUSED_GAME_EVENT.invoker().onCausedGameEvent(sourceEntity, serverLevel, gameEvent, context, vec3);
            if (eventResult == EventResult.CANCELED) {
                cir.setReturnValue(false);
                cir.cancel();
            } else if (eventResult == EventResult.SUCCEEDED) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
}
