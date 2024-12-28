package com.restonic4.under_control.api.config;

import com.restonic4.under_control.api.saving.SavingAPI;
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
    public static void registerServerConfig(String modID) {
        String serverID = modID + "_server";

        Path serverRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(serverID + ".config");

        SavingAPI.registerProvider(serverID, serverRootPath, SavingAPI.getWorldSavingProviders());
    }

    public static void registerClientConfig(String modID) {
        String clientID = modID + "_client";
        String serverID = modID + "_server";

        Path clientRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(clientID + ".config");
        Path serverRootPath = FabricLoader.getInstance().getGameDir().resolve("config").resolve(serverID + ".config");

        SavingAPI.registerProvider(clientID, clientRootPath, SavingAPI.getClientSavingProviders());
        SavingAPI.registerProvider(serverID, serverRootPath, SavingAPI.getClientSavingProviders());
    }

    public static SavingProvider getClientProvider(String modID) {
        return SavingAPI.getClientSavingProviders().get(modID + "_client");
    }

    public static SavingProvider getServerProvider(String modID, MinecraftServer minecraftServer) {
        if (minecraftServer.isDedicatedServer()) {
            return SavingAPI.getWorldSavingProviders().get(modID + "_server");
        }

        return SavingAPI.getClientSavingProviders().get(modID);
    }
}
