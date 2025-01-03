package com.chaotic_loom.under_control.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.List;

public class FabricHelper {
    public static List<String> getLoadedModIDs() {
        List<String> mods = new ArrayList<>();

        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            mods.add(modContainer.getMetadata().getId());
        }

        return mods;
    }
}
