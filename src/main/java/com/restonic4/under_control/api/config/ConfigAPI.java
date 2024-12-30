package com.restonic4.under_control.api.config;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.saving.SavingAPI;
import com.restonic4.under_control.config.ConfigProvider;
import com.restonic4.under_control.config.ConfigScreenManager;
import com.restonic4.under_control.networking.packets.server_to_client.AskClientForMods;
import com.restonic4.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import com.restonic4.under_control.saving.SavingProvider;
import com.restonic4.under_control.saving.VanillaSerializableTypes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigAPI {
    private static Map<String, ConfigProvider> clientConfigProviders = new HashMap<>();
    private static Map<String, ConfigProvider> serverConfigProviders = new HashMap<>();

    public static ConfigProvider registerServerConfig(String modID, MinecraftServer minecraftServer) {
        String serverID = modID + "_server";

        if (minecraftServer != null && minecraftServer.isDedicatedServer()) {
            Path serverRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(serverID + ".config");

            ConfigProvider configProvider = (ConfigProvider) SavingAPI.registerProvider(serverID, SavingAPI.getWorldSavingProviders(), new ConfigProvider(modID, serverRootPath.toString()));
            serverConfigProviders.put(modID, configProvider);
            return configProvider;
        } else {
            Path serverRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(serverID + ".config");
            ConfigProvider configProvider = (ConfigProvider) SavingAPI.registerProvider(serverID, SavingAPI.getClientSavingProviders(), new ConfigProvider(modID, serverRootPath.toString()));
            serverConfigProviders.put(modID, configProvider);
            return configProvider;
        }
    }

    public static ConfigProvider registerClientConfig(String modID) {
        String clientID = modID + "_client";

        Path clientRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(clientID + ".config");

        ConfigProvider configProvider = (ConfigProvider) SavingAPI.registerProvider(clientID, SavingAPI.getClientSavingProviders(), new ConfigProvider(modID, clientRootPath.toString()));
        clientConfigProviders.put(modID, configProvider);
        return configProvider;
    }

    public static void registerConfigScreen(String modID, Class<? extends Screen> screenClass) {
        ConfigScreenManager.registerConfigScreen(modID, screenClass);
    }

    public static ConfigProvider getClientProvider(String modID) {
        return clientConfigProviders.get(modID);
    }

    public static ConfigProvider getServerProvider(String modID) {
        return serverConfigProviders.get(modID);
    }

    public static void reloadServerConfigs(MinecraftServer server) {
        UnderControl.LOGGER.info("Reloading configs");

        for (Map.Entry<String, ConfigProvider> entry : serverConfigProviders.entrySet()) {
            String key = entry.getKey();
            ConfigProvider value = entry.getValue();

            UnderControl.LOGGER.info("Reloading config for " + key);

            value.reload();
        }

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer serverPlayer : players) {
            UpdateServerDataOnClient.sendToClient(serverPlayer);
        }
    }

    public static void registerServerEvents() {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                reloadServerConfigs(server);

                UnderControl.LOGGER.info("Server reloaded correctly");

                if (getServerProvider(UnderControl.MOD_ID).get("log_on_reload_in_chat", Boolean.class)) {
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

                if (getServerProvider(UnderControl.MOD_ID).get("log_on_reload_in_chat", Boolean.class)) {
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
}
