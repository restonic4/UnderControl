package com.restonic4.under_control.registries.client;

import com.restonic4.under_control.commands.RenderCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class UnderControlClientCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext) -> {
            RenderCommand.register(dispatcher, commandBuildContext);
        });
    }
}
