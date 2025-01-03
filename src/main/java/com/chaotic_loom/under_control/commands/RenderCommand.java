package com.chaotic_loom.under_control.commands;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.chaotic_loom.under_control.client.ClientCacheData;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RenderCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext commandBuildContext) {
        dispatcher.register(
                literal("render")
                        .then(literal("wireframe")
                                .then(argument("state", BoolArgumentType.bool())
                                        .executes(RenderCommand::setWireframe)))
                        .then(literal("sphere")
                                .then(argument("x", FloatArgumentType.floatArg())
                                        .then(argument("y", FloatArgumentType.floatArg())
                                                .then(argument("z", FloatArgumentType.floatArg())
                                                        .then(argument("radius", FloatArgumentType.floatArg(0))
                                                                .executes(RenderCommand::renderSphere))))))
        );
    }

    private static int setWireframe(CommandContext<FabricClientCommandSource> context) {
        if (!canUseCommand()) {
            sendNotAllowedMessage();
            return 1;
        }

        boolean state = BoolArgumentType.getBool(context, "state");

        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Wireframe: " + (state ? "Activated" : "Deactivated")));

        RenderSystem.assertOnRenderThread();
        ClientCacheData.shouldRenderWireframe = state;

        return 1;
    }

    private static int renderSphere(CommandContext<FabricClientCommandSource> context) {
        if (!canUseCommand()) {
            sendNotAllowedMessage();
            return 1;
        }

        float x = FloatArgumentType.getFloat(context, "x");
        float y = FloatArgumentType.getFloat(context, "y");
        float z = FloatArgumentType.getFloat(context, "z");
        float radius = FloatArgumentType.getFloat(context, "radius");

        return 1;
    }

    private static boolean canUseCommand() {
        return ClientCacheData.isRenderCommandAllowedOnServer;
    }

    private static void sendNotAllowedMessage() {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("This command is not allowed on this server!"));
    }
}
