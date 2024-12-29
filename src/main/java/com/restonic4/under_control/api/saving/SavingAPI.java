package com.restonic4.under_control.api.saving;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.saving.ClassProvider;
import com.restonic4.under_control.saving.SavingProvider;
import com.restonic4.under_control.saving.VanillaSerializableTypes;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavingAPI {
    private static final Map<String, SavingProvider> clientSavingProviders = new HashMap<>();
    private static final Map<String, SavingProvider> worldSavingProviders = new HashMap<>();

    private static final Map<String, ClassProvider<?>> classProviders = new HashMap<>();

    private static final List<String> worldSavingProvidersGenerators = new ArrayList<>();

    public static final int AUTO_SAVE_DELAY_IN_TICKS = 3000;

    private static int ticksWithoutSavingOnServer, ticksWithoutSavingOnClient;

    public static void registerServerEvents() {
        SavingAPI.registerClassProvider(new VanillaSerializableTypes.BlockPos());

        ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
            for (String modID: worldSavingProvidersGenerators) {
                if (!worldSavingProviders.containsKey(modID)) {
                    Path rootPath = minecraftServer.getWorldPath(LevelResource.ROOT).resolve("data").resolve(modID + ".data");
                    registerProvider(modID, rootPath, worldSavingProviders);
                }
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((minecraftServer) -> {
            saveAllWorldProviders();

            for (String modID: worldSavingProvidersGenerators) {
                worldSavingProviders.remove(modID);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ticksWithoutSavingOnServer++;

            if (ticksWithoutSavingOnServer >= AUTO_SAVE_DELAY_IN_TICKS) {
                ticksWithoutSavingOnServer = 0;

                saveAllWorldProviders();
            }
        });
    }

    public static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ticksWithoutSavingOnClient++;

            if (ticksWithoutSavingOnClient >= AUTO_SAVE_DELAY_IN_TICKS) {
                ticksWithoutSavingOnClient = 0;

                saveAllClientProviders();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register((minecraft) -> {
            saveAllClientProviders();
        });
    }

    public static SavingProvider registerProviderForClient(String modID) {
        Path clientRootPath = FabricLoader.getInstance().getGameDir().resolve("data").resolve(modID + ".data");
        return registerProvider(modID, clientRootPath, clientSavingProviders);
    }

    public static void registerProviderForWorlds(String modID) {
        worldSavingProvidersGenerators.add(modID);
    }


    public static SavingProvider registerProvider(String modID, Path path, Map<String, SavingProvider> map) {
        return registerProvider(modID, path.toString(), map);
    }

    public static SavingProvider registerProvider(String modID, String path, Map<String, SavingProvider> map) {
        SavingProvider savingProvider =  new SavingProvider(modID, path);
        return registerProvider(modID, map, savingProvider);
    }

    public static SavingProvider registerProvider(String modID, Map<String, SavingProvider> map, SavingProvider savingProvider) {
        map.put(modID, savingProvider);

        savingProvider.loadFromFile();

        UnderControl.LOGGER.info("Saving provider for " + modID + " loaded at " + savingProvider.getSaveFilePath());

        savingProvider.saveToFile();

        return savingProvider;
    }

    public static SavingProvider getClientProvider(String modID) {
        return clientSavingProviders.get(modID);
    }

    public static SavingProvider getWorldProvider(String modID) {
        return worldSavingProviders.get(modID);
    }

    public static  <T> void registerClassProvider(ClassProvider<T> provider) {
        classProviders.put(provider.getIdentifier(), provider);
    }

    public static void saveAllClientProviders() {
        for (Map.Entry<String, SavingProvider> entry : clientSavingProviders.entrySet()) {
            entry.getValue().saveToFile();
        }
    }

    public static void saveAllWorldProviders() {
        for (String modID : worldSavingProvidersGenerators) {
            SavingProvider savingProvider = getWorldProvider(modID);
            if (savingProvider != null) {
                savingProvider.saveToFile();
            }
        }
    }

    public static Map<String, ClassProvider<?>> getClassProviders() {
        return classProviders;
    }

    public static Map<String, SavingProvider> getClientSavingProviders() {
        return  clientSavingProviders;
    }

    public static Map<String, SavingProvider> getWorldSavingProviders() {
        return  worldSavingProviders;
    }
}
