package com.restonic4.under_control.client;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.registries.RegistriesManager;
import net.fabricmc.api.ClientModInitializer;

public class UnderControlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UnderControl.LOGGER.info("Starting client");

        RegistriesManager.register(this);
    }
}
