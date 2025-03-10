package com.chaotic_loom.under_control.config;

import com.chaotic_loom.under_control.client.gui.ConfigSelectorScreen;
import com.chaotic_loom.under_control.compatibility.CompatChecker;
import com.chaotic_loom.under_control.compatibility.modmenu.ModMenuCompat;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.Map;

public class ConfigScreenManager {
    private static Map<String, Class<? extends ConfigSelectorScreen.Builder>> screens = new HashMap<>();

    public static void registerConfigScreen(String modID, Class<? extends ConfigSelectorScreen.Builder> screenClass) {
        screens.put(modID, screenClass);

        if (CompatChecker.is("modmenu")) {
            ModMenuCompat.addScreen(modID, screenClass);
        }
    }

    public static Map<String, Class<? extends ConfigSelectorScreen.Builder>> getScreens() {
        return screens;
    }
}
