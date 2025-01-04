package com.chaotic_loom.under_control.registries.common;

import com.chaotic_loom.under_control.commands.VanishCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class UnderControlCommonCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            VanishCommand.register(dispatcher);
        });
    }
}
