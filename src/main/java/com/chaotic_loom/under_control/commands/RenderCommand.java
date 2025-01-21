package com.chaotic_loom.under_control.commands;

import com.chaotic_loom.under_control.client.rendering.screen_shake.ScreenShake;
import com.chaotic_loom.under_control.client.rendering.screen_shake.ScreenShakeManager;
import com.chaotic_loom.under_control.client.rendering.screen_shake.ShakeCombiner;
import com.chaotic_loom.under_control.client.rendering.screen_shake.types.AverageShakeCombiner;
import com.chaotic_loom.under_control.client.rendering.screen_shake.types.MaxShakeCombiner;
import com.chaotic_loom.under_control.client.rendering.screen_shake.types.SumShakeCombiner;
import com.chaotic_loom.under_control.util.EasingSystem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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
                        .then(literal("screen_shake")
                                .then(argument("combiner", StringArgumentType.string())
                                        .then(argument("intensity", FloatArgumentType.floatArg())
                                                .then(argument("duration", LongArgumentType.longArg())
                                                        .executes(RenderCommand::addScreenShake)))))
                        .then(literal("sphere")
                                .then(argument("x", FloatArgumentType.floatArg())
                                        .then(argument("y", FloatArgumentType.floatArg())
                                                .then(argument("z", FloatArgumentType.floatArg())
                                                        .then(argument("radius", FloatArgumentType.floatArg(0))
                                                                .executes(RenderCommand::renderSphere))))))
        );
    }

    private static int addScreenShake(CommandContext<FabricClientCommandSource> context) {
        String combiner = StringArgumentType.getString(context, "combiner");
        float intensity = FloatArgumentType.getFloat(context, "intensity");
        long duration = LongArgumentType.getLong(context, "duration");

        ShakeCombiner shakeCombiner = null;

        switch (combiner) {
            case "sum" -> shakeCombiner = new SumShakeCombiner();
            case "average" -> shakeCombiner = new AverageShakeCombiner();
            case "max" -> shakeCombiner = new MaxShakeCombiner();
            default -> new SumShakeCombiner();
        }

        ScreenShakeManager screenShakeManager = ScreenShakeManager.create(shakeCombiner, intensity);
        screenShakeManager.addShake(new ScreenShake(duration, intensity, EasingSystem.EasingType.LINEAR));

        return 1;
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
