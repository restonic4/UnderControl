package com.chaotic_loom.under_control.client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.registry.UnderControlRegistries;
import com.chaotic_loom.under_control.api.registry.UnderControlRegistry;
import com.chaotic_loom.under_control.config.ModConfig;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.debug.Debugger;
import com.chaotic_loom.under_control.debug.TickingDebugger;
import com.chaotic_loom.under_control.networking.packets.client_to_server.ClientJumpKeyPressed;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.core.UnderControlConfig;
import com.chaotic_loom.under_control.incompatibilities.ClientIncompatibilitiesManager;
import com.chaotic_loom.under_control.registries.RegistriesManager;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class UnderControlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UnderControl.LOGGER.info("Starting the library, client side");

        SavingManager.registerClientEvents();
        RegistriesManager.collectModPackages();
        RegistriesManager.startRegistrationAnnotationCollection(ExecutionSide.CLIENT);
        ClientIncompatibilitiesManager.registerClient();
        RegistriesManager.startPacketAnnotationCollection(PacketDirection.SERVER_TO_CLIENT);

        List<Debugger> debuggers = UnderControlRegistry.getRegistryValues(UnderControlRegistries.DEBUGGER);
        List<TickingDebugger> tickingDebuggers = new ArrayList<>();

        for (Debugger debugger : debuggers) {
            if (debugger.getExecutionSide() == ExecutionSide.CLIENT) {
                debugger.onInitialize();

                if (debugger instanceof TickingDebugger tickingDebugger) {
                    tickingDebuggers.add(tickingDebugger);
                }
            }
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (int i = 0; i < tickingDebuggers.size(); i++) {
                TickingDebugger debugger = tickingDebuggers.get(i);
                debugger.internalTick();
            }

            if (client.options.keyJump.consumeClick()) {
                ClientJumpKeyPressed.sendToServer();
            }
        });
    }
}
