package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "broadcastToPlayer", at = @At("HEAD"), cancellable = true)
    public void shouldBroadcast(ServerPlayer observer, CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = LivingEntityExtraEvents.BROADCAST_TO_PLAYER.invoker().onBroadcastToPlayer((Entity) (Object) this, observer);
        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void setInvisible(Player player, CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = LivingEntityExtraEvents.INVISIBLE_TO.invoker().onInvisibleTo((Entity) (Object) this, player);
        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), cancellable = true)
    private void preventSound(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        if (LivingEntityExtraEvents.PLAY_SOUND.invoker().onPlaySound((Entity) (Object) this, soundEvent, f, g) == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
