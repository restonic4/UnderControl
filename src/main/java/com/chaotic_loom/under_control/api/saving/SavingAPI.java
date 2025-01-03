package com.chaotic_loom.under_control.api.saving;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.saving.ClassProvider;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.saving.SavingProvider;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.Map;

public class SavingAPI {
    public static SavingProvider registerProviderForClient(String modID) {
        Path clientRootPath = FabricLoader.getInstance().getGameDir().resolve("data").resolve(modID + ".data");
        return registerProvider(modID, clientRootPath, SavingManager.getClientSavingProviders());
    }

    public static void registerProviderForWorlds(String modID) {
        SavingManager.getWorldSavingProvidersGenerators().add(modID);
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
        return SavingManager.getClientSavingProviders().get(modID);
    }

    public static SavingProvider getWorldProvider(String modID) {
        return SavingManager.getWorldSavingProviders().get(modID);
    }

    public static  <T> void registerClassProvider(ClassProvider<T> provider) {
        SavingManager.getClassProviders().put(provider.getIdentifier(), provider);
    }

    public static void saveAllClientProviders() {
        for (Map.Entry<String, SavingProvider> entry : SavingManager.getClientSavingProviders().entrySet()) {
            entry.getValue().saveToFile();
        }
    }

    public static void saveAllWorldProviders() {
        for (String modID : SavingManager.getWorldSavingProvidersGenerators()) {
            SavingProvider savingProvider = getWorldProvider(modID);
            if (savingProvider != null) {
                savingProvider.saveToFile();
            }
        }
    }
}
