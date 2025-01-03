package com.chaotic_loom.under_control.registries;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.registries.client.UnderControlClientCommands;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.registries.common.UnderControlCommonCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class RegistriesManager {
    // Common side
    public static void register(ModInitializer modInitializer) {
        UnderControl.LOGGER.info("Registering common registries");

        UnderControlCommonCommands.register();
    }

    // Client side
    public static void register(ClientModInitializer clientModInitializer) {
        UnderControl.LOGGER.info("Registering client registries");

        UnderControlShaders.register();
        UnderControlClientCommands.register();
    }
}
