package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.commands.TellRawCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TellRawCommand.class)
public class TellRawCommandMixin {
    @Redirect(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;executes(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"
            )
    )
    private static ArgumentBuilder<?, ?> onExecutes(RequiredArgumentBuilder<CommandSourceStack, ?> builder, Command<CommandSourceStack> command) {
        return builder.executes(commandContext -> {
            int i = 0;

            ServerPlayer origin = commandContext.getSource().getPlayer();

            for(ServerPlayer serverPlayer : EntityArgument.getPlayers(commandContext, "targets")) {
                if (ServerPlayerExtraEvents.TELLRAW_RECEIVED.invoker().onTellRawReceived(origin, serverPlayer, commandContext, command) != EventResult.CANCELED) {
                    serverPlayer.sendSystemMessage(
                            ComponentUtils.updateForEntity(commandContext.getSource(), ComponentArgument.getComponent(commandContext, "message"), serverPlayer, 0), false
                    );
                    ++i;
                }
            }

            return i;
        });
    }
}
