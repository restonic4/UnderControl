package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;

    @WrapOperation(
            method = "award",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void hideAdvancementMessage(PlayerList playerList, Component component, boolean bl, Operation<Void> original) {
        if (ServerPlayerExtraEvents.ADVANCEMENT_MESSAGE.invoker().onAdvancementMessage(this.player, playerList, component, bl) != EventResult.CANCELED) {
            original.call(playerList, component, bl);
        }
    }

    @Inject(method = "award", at = @At("HEAD"), cancellable = true)
    public void award(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        if (ServerPlayerExtraEvents.ADVANCEMENT_GRANTED.invoker().onEvent(player, advancement, criterionName) == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
