package com.chaotic_loom.under_control.commands;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class VanishCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vanish")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("state", BoolArgumentType.bool())
                                .executes(ctx -> setVanish(ctx, EntityArgument.getPlayer(ctx, "player"), BoolArgumentType.getBool(ctx, "state"))))
                        .executes(ctx -> toggleVanish(ctx, EntityArgument.getPlayer(ctx, "player"))))
        );

        dispatcher.register(Commands.literal("unvanish")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> setVanish(ctx, EntityArgument.getPlayer(ctx, "player"), false)))
        );
    }

    private static int setVanish(CommandContext<CommandSourceStack> ctx, ServerPlayer player, boolean state) {
        if (state) {
            VanishAPI.vanish(player);
            ctx.getSource().sendSuccess(() -> Component.literal("Player " + player.getName().getString() + " is now vanished."), true);
        } else {
            VanishAPI.unVanish(player);
            ctx.getSource().sendSuccess(() -> Component.literal("Player " + player.getName().getString() + " is no longer vanished."), true);
        }
        return 1;
    }

    private static int toggleVanish(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        boolean isVanished = VanishAPI.isVanished(player);
        return setVanish(ctx, player, !isVanished);
    }
}
