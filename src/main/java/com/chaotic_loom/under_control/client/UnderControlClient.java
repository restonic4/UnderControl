package com.chaotic_loom.under_control.client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.cutscene.CutsceneAPI;
import com.chaotic_loom.under_control.client.rendering.effects.EffectManager;
import com.chaotic_loom.under_control.client.rendering.effects.Sphere;
import com.chaotic_loom.under_control.client.rendering.screen_shake.ScreenShake;
import com.chaotic_loom.under_control.client.rendering.screen_shake.ScreenShakeManager;
import com.chaotic_loom.under_control.client.rendering.screen_shake.types.SumShakeCombiner;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.core.UnderControlConfig;
import com.chaotic_loom.under_control.incompatibilities.ClientIncompatibilitiesManager;
import com.chaotic_loom.under_control.registries.RegistriesManager;
import com.chaotic_loom.under_control.util.EasingSystem;
import com.chaotic_loom.under_control.util.data_holders.RenderingFlags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

import java.awt.*;

@Environment(value = EnvType.CLIENT)
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

        /*CutsceneAPI.play();
        CutsceneAPI.setPosition(0, 0, 0);
        CutsceneAPI.setRotation(0, 0);*/

        ScreenShakeManager explosionManager = ScreenShakeManager.create(new SumShakeCombiner(), 1000);

        OtherEvents.EXPLODE.register((explosion) -> {
            explosionManager.addShake(new ScreenShake(3000, 10, EasingSystem.EasingType.QUAD_IN_OUT));

            return EventResult.SUCCEEDED;
        });
    }
}
