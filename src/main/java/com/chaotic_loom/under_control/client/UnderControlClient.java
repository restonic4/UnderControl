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

        ShaderProfile profile1 = new ShaderProfile(UnderControlShaders.VERTICAL_GRADIENT);
        profile1.setUniformData("Center", new float[] {0, 0, 0});
        profile1.setUniformData("Radius", new float[] {10});
        profile1.setUniformData("TopColor", new float[] {1, 0, 0, 1});
        profile1.setUniformData("BottomColor", new float[] {0, 1, 0, 1});

        Sphere a = SphereManager.create(0);
        a.setPosition(new Vector3f());
        a.setRadius(10);
        a.setRenderingFlags(RenderingFlags.INVERT_NORMALS);
        a.setShaderProfile(profile1);

        ShaderProfile profile2 = new ShaderProfile(UnderControlShaders.VERTICAL_GRADIENT);
        profile2.setUniformData("Center", new float[] {20, 0, 0});
        profile2.setUniformData("Radius", new float[] {10});
        profile2.setUniformData("TopColor", new float[] {0, 0, 1, 1});
        profile2.setUniformData("BottomColor", new float[] {0, 1, 1, 1});

        Sphere b = SphereManager.create(1);
        b.setPosition(new Vector3f(20, 0, 0));
        b.setRadius(10);
        b.setRenderingFlags(RenderingFlags.INVERT_NORMALS);
        b.setShaderProfile(profile2);
    }
}
