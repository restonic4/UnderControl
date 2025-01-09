package com.chaotic_loom.under_control.client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.client.rendering.effects.SphereManager;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.core.UnderControlConfig;
import com.chaotic_loom.under_control.incompatibilities.ClientIncompatibilitiesManager;
import com.chaotic_loom.under_control.networking.PacketManager;
import com.chaotic_loom.under_control.registries.RegistriesManager;
import net.fabricmc.api.ClientModInitializer;
import org.joml.Vector3f;

import java.awt.*;

public class UnderControlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UnderControl.LOGGER.info("Starting the library, client side");

        SavingManager.registerClientEvents();
        UnderControlConfig.registerClient();
        RegistriesManager.register(this);
        ClientIncompatibilitiesManager.registerClient();
        PacketManager.registerServerToClient();
    }
}
