package com.restonic4.under_control;

import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.restonic4.under_control.core.UnderControlConfig;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnderControlServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeServer() {
        LOGGER.info("Starting the library, server side");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            UnderControlConfig.registerServer(server);
        });
    }
}
