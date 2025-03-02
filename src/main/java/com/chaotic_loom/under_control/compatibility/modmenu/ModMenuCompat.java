package com.chaotic_loom.under_control.compatibility.modmenu;

import com.chaotic_loom.under_control.client.gui.ConfigSelectorScreen;
import com.google.common.collect.ImmutableMap;
import com.chaotic_loom.under_control.mixin.compatibility.modmenu.ModMenuAccessor;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.Map;

public class ModMenuCompat {
    public static void addScreen(String modID, Class<? extends ConfigSelectorScreen.Builder> screenClass) {
        List<Map<String, ConfigScreenFactory<?>>> delayedFactories = ModMenuAccessor.getDelayedScreenFactoryProviders();

        if (delayedFactories != null) {
            delayedFactories.add(
                    ImmutableMap.of(modID, parent -> {
                        try {
                            return screenClass.getConstructor(Screen.class, String.class).newInstance(parent, modID).build();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    })
            );
        }
    }
}
