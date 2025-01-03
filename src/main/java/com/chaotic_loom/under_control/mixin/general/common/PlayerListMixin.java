package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.saving.SavingAPI;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.saving.SavingProvider;
import com.chaotic_loom.under_control.saving.custom.VanishList;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V"
            )
    )
    private void vanishOnJoin(Connection connection, ServerPlayer actor, CallbackInfo ci) {
        ServerPlayerExtraEvents.STARTED_JOINING.invoker().onStartedJoining(connection, actor);
    }

    @WrapOperation(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public void hideJoinMessage(PlayerList playerList, Component component, boolean bl, Operation<Void> original, Connection connection, ServerPlayer actor) {
        if (ServerPlayerExtraEvents.JOIN_MESSAGE.invoker().onJoinMessage(playerList, component, bl, connection, actor) != EventResult.CANCELED) {
            original.call(playerList, component, bl);
        }
    }

    @WrapWithCondition(
            method = "broadcast",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"
            )
    )
    public boolean gameEvents(ServerGamePacketListenerImpl packetListener, Packet<?> packet, Player player) {
        EventResult eventResult = ServerPlayerExtraEvents.GAME_EVENT.invoker().onGameEvent(packetListener, packet, player);
        return eventResult != EventResult.CANCELED;
    }

    @Redirect(
            method = "canPlayerLogin",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/players/PlayerList;players:Ljava/util/List;"
            )
    )
    private List<ServerPlayer> getPlayerCount(PlayerList playerList) {
        List<ServerPlayer> newPlayerList = OtherEvents.PLAYER_LIST_INFO_REQUIRED.invoker().onPlayerListUpdate(playerList, playerList.getServer().createCommandSourceStack());
        return newPlayerList != null ? newPlayerList : playerList.getPlayers();
    }
}
