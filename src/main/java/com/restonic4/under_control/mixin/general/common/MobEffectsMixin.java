package com.restonic4.under_control.mixin.general.common;

import com.restonic4.under_control.events.EventResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.restonic4.under_control.events.types.SeverExtraPlayerEvents;

@Mixin(targets = "net/minecraft/world/effect/MobEffects$1")
public class MobEffectsMixin {
    @Inject(
        method = "applyEffectTick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;serverLevel()Lnet/minecraft/server/level/ServerLevel;"
        ),
        cancellable = true
    )
    public void onPlayerBadOmenRaid(LivingEntity livingEntity, int level, CallbackInfo ci) {
        EventResult eventResult = SeverExtraPlayerEvents.VILLAGE_RAID_STARTED.invoker().onVillageRaidStarted((ServerPlayer) livingEntity, level);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
