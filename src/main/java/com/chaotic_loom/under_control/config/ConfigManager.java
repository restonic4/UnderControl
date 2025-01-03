package com.chaotic_loom.under_control.config;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.config.ConfigAPI;
import com.chaotic_loom.under_control.networking.packets.server_to_client.AskClientForMods;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private static final Map<String, ConfigProvider> clientConfigProviders = new HashMap<>();
    private static final Map<String, ConfigProvider> serverConfigProviders = new HashMap<>();

    public static void registerServerEvents() {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                ConfigAPI.reloadServerConfigs(server);

                UnderControl.LOGGER.info("Server reloaded correctly");

                if (ConfigAPI.getServerProvider(UnderControl.MOD_ID).get("log_on_reload_in_chat", Boolean.class)) {
                    Component message = Component.translatable("message.under_control.server.reload.complete");

                    server.getPlayerList().getPlayers().stream()
                            .filter(serverPlayer -> server.getPlayerList().isOp(serverPlayer.getGameProfile()))
                            .forEach(serverPlayer -> serverPlayer.sendSystemMessage(message));
                }

                List<ServerPlayer> players = server.getPlayerList().getPlayers();
                for (ServerPlayer serverPlayer : players) {
                    AskClientForMods.sendToClient(serverPlayer);
                }
            } else {
                UnderControl.LOGGER.info("Error reloading the server");

                if (ConfigAPI.getServerProvider(UnderControl.MOD_ID).get("log_on_reload_in_chat", Boolean.class)) {
                    Component message = Component.translatable("message.under_control.server.reload.error");

                    server.getPlayerList().getPlayers().stream()
                            .filter(serverPlayer -> server.getPlayerList().isOp(serverPlayer.getGameProfile()))
                            .forEach(serverPlayer -> serverPlayer.sendSystemMessage(message));
                }
            }
        });

        ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
            for (Map.Entry<String, ConfigProvider> entry : serverConfigProviders.entrySet()) {
                entry.getValue().reload();
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((minecraftServer) -> {
            for (Map.Entry<String, ConfigProvider> entry : serverConfigProviders.entrySet()) {
                entry.getValue().saveToFile();
            }
        });
    }

    public static Map<String, ConfigProvider> getClientConfigProviders() {
        return clientConfigProviders;
    }

    public static Map<String, ConfigProvider> getServerConfigProviders() {
        return serverConfigProviders;
    }
}
