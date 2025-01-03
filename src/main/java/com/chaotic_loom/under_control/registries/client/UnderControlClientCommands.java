package com.chaotic_loom.under_control.registries.client;

import com.chaotic_loom.under_control.commands.RenderCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class UnderControlClientCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext) -> {
            RenderCommand.register(dispatcher, commandBuildContext);
        });
    }
}
