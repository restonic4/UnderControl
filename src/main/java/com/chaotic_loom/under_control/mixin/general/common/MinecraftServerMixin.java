package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    public abstract CommandSourceStack createCommandSourceStack();

    @ModifyReceiver(
            method = "buildPlayerStatus",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;size()I"
            )
    )
    public List<ServerPlayer> getPlayerCount(List<ServerPlayer> original) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        List<ServerPlayer> newPlayerList = OtherEvents.PLAYER_LIST_INFO_REQUIRED.invoker().onPlayerListUpdate(server.getPlayerList(), server.createCommandSourceStack());
        return newPlayerList != null ? newPlayerList : server.getPlayerList().getPlayers();
    }

    @ModifyReceiver(
            method = "buildPlayerStatus",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            )
    )
    public List<ServerPlayer> getPlayers(List<ServerPlayer> original, int index) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        List<ServerPlayer> newPlayerList = OtherEvents.PLAYER_LIST_INFO_REQUIRED.invoker().onPlayerListUpdate(server.getPlayerList(), server.createCommandSourceStack());
        return newPlayerList != null ? newPlayerList : server.getPlayerList().getPlayers();
    }

    @Inject(method = "getPlayerCount", at = @At("HEAD"), cancellable = true)
    public void getPlayerCount(CallbackInfoReturnable<Integer> cir) {
        MinecraftServer server = (MinecraftServer) (Object) this;

        List<ServerPlayer> newPlayerList = OtherEvents.PLAYER_LIST_INFO_REQUIRED.invoker().onPlayerListUpdate(server.getPlayerList(), server.createCommandSourceStack().withPermission(0));
        newPlayerList = newPlayerList != null ? newPlayerList : server.getPlayerList().getPlayers();

        cir.setReturnValue(newPlayerList.size());
        cir.cancel();
    }
}
