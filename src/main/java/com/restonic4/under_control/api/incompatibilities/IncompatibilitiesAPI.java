package com.restonic4.under_control.api.incompatibilities;

import com.restonic4.under_control.client.gui.ModIncompatibilityScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class IncompatibilitiesAPI {
    private static List<IncompatibilityData> incompatibilityDataList = new ArrayList<>();

    public static void registerIncompatibleMods(String modID, String... incompatibleModID) {
        for (String foundModID : incompatibleModID) {
            registerIncompatibleMod(modID, foundModID);
        }
    }

    public static void registerIncompatibleMod(String modID, String incompatibleModID) {
        IncompatibilityData incompatibilityData = null;

        for (int i = 0; i < incompatibilityDataList.size() && incompatibilityData == null; i++) {
            if (incompatibilityDataList.get(i).getModID().equals(modID)) {
                incompatibilityData = incompatibilityDataList.get(i);
            }
        }

        if (incompatibilityData == null) {
            incompatibilityData = new IncompatibilityData(modID);
            incompatibilityDataList.add(incompatibilityData);
        }

        incompatibilityData.addMod(incompatibleModID);
    }

    public static void getIncompatibleModsOnUse(List<String> list, List<String> modList) {
        for (IncompatibilityData incompatibilityData : incompatibilityDataList) {
            for (String modID : incompatibilityData.getIncompatibleMods()) {
                if (FabricLoader.getInstance().isModLoaded(modID)) {
                    modList.add(modID);

                    if (!list.contains(incompatibilityData.getModID())) {
                        list.add(incompatibilityData.getModID());
                    }
                }
            }
        }
    }

    private static final List<String> cachedOriginalMods = new ArrayList<>();
    private static final List<String> cachedMods = new ArrayList<>();
    private static void throwFatalErrorIfIncompatibilities() {
        cachedOriginalMods.clear();
        cachedMods.clear();

        getIncompatibleModsOnUse(cachedOriginalMods, cachedMods);

        if (!cachedMods.isEmpty() && (Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof ModIncompatibilityScreen))) {
            String originals = String.join(", ", cachedOriginalMods);
            String mods = String.join(", ", cachedMods);
            incompatibilityScreen = new ModIncompatibilityScreen(originals, mods);
        }
    }

    static ModIncompatibilityScreen incompatibilityScreen = null;
    static boolean checked = false;
    public static void registerClient() {
        throwFatalErrorIfIncompatibilities();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (incompatibilityScreen != null) {
                Minecraft.getInstance().forceSetScreen(incompatibilityScreen);
            }

            if (!checked) {
                checked = true;
                throwFatalErrorIfIncompatibilities();
            }
        });
    }
}
