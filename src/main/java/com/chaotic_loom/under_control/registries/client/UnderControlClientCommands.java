package com.chaotic_loom.under_control.registries.client;

import com.chaotic_loom.under_control.commands.RenderCommand;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.Registration;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

@Registration(side = ExecutionSide.CLIENT)
public class UnderControlClientCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext) -> {
            RenderCommand.register(dispatcher, commandBuildContext);
        });
    }
}
