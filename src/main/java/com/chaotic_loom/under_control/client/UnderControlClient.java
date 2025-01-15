package com.chaotic_loom.under_control.client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.client.rendering.effects.Sphere;
import com.chaotic_loom.under_control.client.rendering.effects.SphereManager;
import com.chaotic_loom.under_control.client.rendering.shader.ShaderProfile;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.core.UnderControlConfig;
import com.chaotic_loom.under_control.incompatibilities.ClientIncompatibilitiesManager;
import com.chaotic_loom.under_control.registries.RegistriesManager;
import com.chaotic_loom.under_control.util.data_holders.RenderingFlags;
import net.fabricmc.api.ClientModInitializer;
import org.joml.Vector3f;

public class UnderControlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UnderControl.LOGGER.info("Starting the library, client side");

        SavingManager.registerClientEvents();
        UnderControlConfig.registerClient();
        RegistriesManager.collectModPackages();
        RegistriesManager.startRegistrationAnnotationCollection(ExecutionSide.CLIENT);
        ClientIncompatibilitiesManager.registerClient();
        RegistriesManager.startPacketAnnotationCollection(PacketDirection.SERVER_TO_CLIENT);
    }
}
