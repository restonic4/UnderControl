package com.restonic4.under_control;

import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.incompatibilities.IncompatibilitiesAPI;
import net.fabricmc.api.DedicatedServerModInitializer;

public class UnderControlServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        IncompatibilitiesAPI.registerIncompatibleMods(UnderControl.MOD_ID, "minecraft", "fabric");
        ConfigAPI.registerServerConfig(UnderControl.MOD_ID);
    }
}
