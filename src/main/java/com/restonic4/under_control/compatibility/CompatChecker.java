package com.restonic4.under_control.compatibility;

import net.fabricmc.loader.api.FabricLoader;

public class CompatChecker {
    public static boolean is(String modID) {
        return FabricLoader.getInstance().isModLoaded(modID);
    }
}
