package com.chaotic_loom.under_control;

import com.chaotic_loom.under_control.core.UnderControlConfig;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.registries.RegistriesManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnderControlServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeServer() {
        LOGGER.info("Starting the library, server side");

        RegistriesManager.startRegistrationAnnotationCollection(ExecutionSide.SERVER);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            UnderControlConfig.registerServer(server);
        });
    }
}
