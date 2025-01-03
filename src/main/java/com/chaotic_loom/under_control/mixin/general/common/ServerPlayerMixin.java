package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemToTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void hideTeamDeathMessage(PlayerList playerList, Player player, Component component, Operation<Void> original) {
        if (ServerPlayerExtraEvents.DEATH_MESSAGE.invoker().onDeathMessage(playerList, player, component, (ServerPlayer) (Object) this) != EventResult.CANCELED) {
            original.call(playerList, player, component);
        }
    }

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemToAllExceptTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void hideExceptTeamDeathMessage(PlayerList playerList, Player player, Component component, Operation<Void> original) {
        if (ServerPlayerExtraEvents.DEATH_MESSAGE.invoker().onDeathMessage(playerList, player, component, (ServerPlayer) (Object) this) != EventResult.CANCELED) {
            original.call(playerList, player, component);
        }
    }

    @WrapOperation(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    private void hideDeathMessage(PlayerList playerList, Component component, boolean overlay, Operation<Void> original) {
        if (ServerPlayerExtraEvents.DEATH_MESSAGE.invoker().onDeathMessage(playerList, null, component, (ServerPlayer) (Object) this) != EventResult.CANCELED) {
            original.call(playerList, component, overlay);
        }
    }
}
