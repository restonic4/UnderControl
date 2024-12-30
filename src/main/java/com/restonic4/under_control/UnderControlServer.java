package com.restonic4.under_control;

import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.restonic4.under_control.core.UnderControlConfig;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class UnderControlServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            UnderControlConfig.registerServer(server);
        });
    }
}
