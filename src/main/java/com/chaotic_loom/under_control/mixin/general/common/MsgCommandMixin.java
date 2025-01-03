package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(MsgCommand.class)
public class MsgCommandMixin {

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private static void preventMessage(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, PlayerChatMessage playerChatMessage, CallbackInfo ci) {
        if (ServerPlayerExtraEvents.MSG.invoker().onMsg(commandSourceStack, collection, playerChatMessage) == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
