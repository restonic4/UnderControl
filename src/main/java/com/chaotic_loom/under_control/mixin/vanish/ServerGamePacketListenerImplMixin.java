package com.chaotic_loom.under_control.mixin.vanish;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Inject(
            method = "handlePlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket;getPos()Lnet/minecraft/core/BlockPos;"
            )
    )
    public void beforeHandlePlayerAction(ServerboundPlayerActionPacket serverboundPlayerActionPacket, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
            method = "handleUseItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;ackBlockChangesUpTo(I)V"
            )
    )
    public void beforeHandleUseItemOn(ServerboundUseItemOnPacket serverboundUseItemOnPacket, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
            method = "handleUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;ackBlockChangesUpTo(I)V"
            )
    )
    public void beforeHandleUseItem(ServerboundUseItemPacket serverboundUseItemPacket, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
            method = "handleInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;serverLevel()Lnet/minecraft/server/level/ServerLevel;",
                    ordinal = 1
            )
    )
    public void beforeHandleInteract(ServerboundInteractPacket serverboundInteractPacket, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(
            method = {"handlePlayerAction", "handleUseItemOn", "handleUseItem", "handleInteract"},
            at = @At("RETURN")
    )
    public void afterPacket(CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.remove();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void beforeTick(CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(this.player);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void afterTick(CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.remove();
    }
}
