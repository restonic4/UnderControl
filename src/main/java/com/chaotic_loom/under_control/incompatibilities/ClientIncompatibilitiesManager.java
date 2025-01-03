package com.chaotic_loom.under_control.incompatibilities;

import com.chaotic_loom.under_control.client.gui.ModIncompatibilityScreen;
import com.chaotic_loom.under_control.util.FabricHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.List;

public class ClientIncompatibilitiesManager {
    private static void throwFatalErrorIfIncompatibilities() {
        List<String> incompatibleMods = IncompatibilitiesUtil.getIncompatibleModsOnUse(FabricHelper.getLoadedModIDs());

        if (!incompatibleMods.isEmpty() && (Minecraft.getInstance().screen == null || !(Minecraft.getInstance().screen instanceof ModIncompatibilityScreen))) {
            String mods = String.join(", ", incompatibleMods);
            incompatibilityScreen = new ModIncompatibilityScreen(mods);
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
