package com.restonic4.under_control.api.config;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.saving.SavingAPI;
import com.restonic4.under_control.config.ConfigProvider;
import com.restonic4.under_control.saving.SavingProvider;
import com.restonic4.under_control.saving.VanillaSerializableTypes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
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

            ConfigProvider configProvider = new ConfigProvider(SavingAPI.registerProvider(serverID, serverRootPath, SavingAPI.getWorldSavingProviders()));
            serverConfigProviders.put(modID, configProvider);
            return configProvider;
        } else {
            Path serverRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(serverID + ".config");
            ConfigProvider configProvider = new ConfigProvider(SavingAPI.registerProvider(serverID, serverRootPath, SavingAPI.getClientSavingProviders()));
            serverConfigProviders.put(modID, configProvider);
            return configProvider;
        }
    }

    public static ConfigProvider registerClientConfig(String modID) {
        String clientID = modID + "_client";

        Path clientRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(clientID + ".config");

        ConfigProvider configProvider = new ConfigProvider(SavingAPI.registerProvider(clientID, clientRootPath, SavingAPI.getClientSavingProviders()));
        clientConfigProviders.put(modID, configProvider);
        return configProvider;
    }

    public static ConfigProvider getClientProvider(String modID) {
        return clientConfigProviders.get(modID);
    }

    public static ConfigProvider getServerProvider(String modID) {
        return serverConfigProviders.get(modID);
    }

    public static void reloadServerConfigs() {
        for (Map.Entry<String, ConfigProvider> entry : serverConfigProviders.entrySet()) {
            String key = entry.getKey();
            ConfigProvider value = entry.getValue();

            value.reload();
        }

    }

    public static void registerServerEvents() {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                reloadServerConfigs();
                UnderControl.LOGGER.info("Server reloaded correctly");
            } else {
                UnderControl.LOGGER.info("Error reloading the server");
            }
        });
    }
}
