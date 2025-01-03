package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.types.OtherEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntitySelector.class)
public class CommandEntitySelectorMixin {
    @Inject(method = "findPlayers", at = @At("RETURN"))
    public void removeVanishedPlayers(CommandSourceStack css, CallbackInfoReturnable<List<ServerPlayer>> cir) {
        OtherEvents.PLAYERS_FOUND_BY_COMMAND.invoker().onPlayersFoundByCommand(cir.getReturnValue(), css);
    }
}
