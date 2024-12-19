package com.restonic4.under_control.commands;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.restonic4.under_control.client.ClientCacheData;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static org.lwjgl.opengl.GL11.*;

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
        boolean state = BoolArgumentType.getBool(context, "state");

        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Wireframe: " + (state ? "Activated" : "Deactivated")));

        RenderSystem.assertOnRenderThread();
        ClientCacheData.shouldRenderWireframe = state;

        return 1;
    }

    private static int renderSphere(CommandContext<FabricClientCommandSource> context) {
        float x = FloatArgumentType.getFloat(context, "x");
        float y = FloatArgumentType.getFloat(context, "y");
        float z = FloatArgumentType.getFloat(context, "z");
        float radius = FloatArgumentType.getFloat(context, "radius");


        return 1;
    }
}
