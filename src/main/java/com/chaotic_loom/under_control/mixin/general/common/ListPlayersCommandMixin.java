package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ListPlayersCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ListPlayersCommand.class)
public class ListPlayersCommandMixin {
    @Redirect(
            method = "format",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;getPlayers()Ljava/util/List;"
            )
    )
    private static List<ServerPlayer> removeVanishedPlayers(PlayerList playerList, @Local(argsOnly = true) CommandSourceStack observer) {
        List<ServerPlayer> newPlayerList = OtherEvents.PLAYER_LIST_INFO_REQUIRED.invoker().onPlayerListUpdate(playerList, observer);
        return newPlayerList != null ? newPlayerList : playerList.getPlayers();
    }
}
