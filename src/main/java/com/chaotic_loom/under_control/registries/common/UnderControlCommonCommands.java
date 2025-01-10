package com.chaotic_loom.under_control.registries.common;

import com.chaotic_loom.under_control.commands.VanishCommand;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.Registration;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

@Registration(side = ExecutionSide.COMMON)
public class UnderControlCommonCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            VanishCommand.register(dispatcher);
        });
    }
}
