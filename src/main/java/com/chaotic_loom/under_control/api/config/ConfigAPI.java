package com.chaotic_loom.under_control.api.config;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.saving.SavingAPI;
import com.chaotic_loom.under_control.config.ConfigManager;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.config.ConfigProvider;
import com.chaotic_loom.under_control.config.ConfigScreenManager;
import com.chaotic_loom.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ConfigAPI {
    public static ConfigProvider registerServerConfig(String modID, MinecraftServer minecraftServer) {
        String serverID = modID + "_server";

        Path serverRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(serverID + ".config");

        ConfigProvider configProvider;

        if (minecraftServer != null && minecraftServer.isDedicatedServer()) {
            configProvider = (ConfigProvider) SavingAPI.registerProvider(serverID, SavingManager.getWorldSavingProviders(), new ConfigProvider(modID, serverRootPath.toString()));
        } else {
            configProvider = (ConfigProvider) SavingAPI.registerProvider(serverID, SavingManager.getClientSavingProviders(), new ConfigProvider(modID, serverRootPath.toString()));
        }

        ConfigManager.getServerConfigProviders().put(modID, configProvider);
        return configProvider;
    }

    public static ConfigProvider registerClientConfig(String modID) {
        String clientID = modID + "_client";

        Path clientRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(clientID + ".config");

        ConfigProvider configProvider = (ConfigProvider) SavingAPI.registerProvider(clientID, SavingManager.getClientSavingProviders(), new ConfigProvider(modID, clientRootPath.toString()));
        ConfigManager.getClientConfigProviders().put(modID, configProvider);
        return configProvider;
    }

    public static void registerConfigScreen(String modID, Class<? extends Screen> screenClass) {
        ConfigScreenManager.registerConfigScreen(modID, screenClass);
    }

    public static ConfigProvider getClientProvider(String modID) {
        return ConfigManager.getClientConfigProviders().get(modID);
    }

    public static ConfigProvider getServerProvider(String modID) {
        return ConfigManager.getServerConfigProviders().get(modID);
    }

    public static void reloadServerConfigs(MinecraftServer server) {
        UnderControl.LOGGER.info("Reloading configs");

        for (Map.Entry<String, ConfigProvider> entry : ConfigManager.getServerConfigProviders().entrySet()) {
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
}
