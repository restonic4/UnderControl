package com.restonic4.under_control.incompatibilities;

import com.restonic4.under_control.client.gui.ModIncompatibilityScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class ClientIncompatibilitiesManager {
    private static final List<String> cachedOriginalMods = new ArrayList<>();
    private static final List<String> cachedMods = new ArrayList<>();
    private static void throwFatalErrorIfIncompatibilities() {
        cachedOriginalMods.clear();
        cachedMods.clear();

        IncompatibilitiesUtil.getIncompatibleModsOnUse(cachedOriginalMods, cachedMods);

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
