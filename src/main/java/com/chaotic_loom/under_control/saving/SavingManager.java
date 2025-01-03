package com.chaotic_loom.under_control.saving;

import com.chaotic_loom.under_control.api.saving.SavingAPI;
import com.chaotic_loom.under_control.incompatibilities.IncompatibilitiesList;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavingManager {
    private static final Map<String, SavingProvider> clientSavingProviders = new HashMap<>();
    private static final Map<String, SavingProvider> worldSavingProviders = new HashMap<>();

    private static final Map<String, ClassProvider<?>> classProviders = new HashMap<>();

    private static final List<String> worldSavingProvidersGenerators = new ArrayList<>();

    public static final int AUTO_SAVE_DELAY_IN_TICKS = 3000;

    private static int ticksWithoutSavingOnServer, ticksWithoutSavingOnClient;

    public static void registerServerEvents() {
        SavingAPI.registerClassProvider(new VanillaSerializableTypes.BlockPos());
        SavingAPI.registerClassProvider(new IncompatibilitiesList());

        ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
            for (String modID: worldSavingProvidersGenerators) {
                if (!worldSavingProviders.containsKey(modID)) {
                    Path rootPath = minecraftServer.getWorldPath(LevelResource.ROOT).resolve("data").resolve(modID + ".data");
                    SavingAPI.registerProvider(modID, rootPath, worldSavingProviders);
                }
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((minecraftServer) -> {
            SavingAPI.saveAllWorldProviders();

            for (String modID: worldSavingProvidersGenerators) {
                worldSavingProviders.remove(modID);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ticksWithoutSavingOnServer++;

            if (ticksWithoutSavingOnServer >= AUTO_SAVE_DELAY_IN_TICKS) {
                ticksWithoutSavingOnServer = 0;

                SavingAPI.saveAllWorldProviders();
            }
        });
    }

    public static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ticksWithoutSavingOnClient++;

            if (ticksWithoutSavingOnClient >= AUTO_SAVE_DELAY_IN_TICKS) {
                ticksWithoutSavingOnClient = 0;

                SavingAPI.saveAllClientProviders();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register((minecraft) -> {
            SavingAPI.saveAllClientProviders();
        });
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

    public static List<String> getWorldSavingProvidersGenerators() {
        return worldSavingProvidersGenerators;
    }
}
