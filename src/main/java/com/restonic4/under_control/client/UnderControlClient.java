package com.restonic4.under_control.client;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.client.gui.FatalErrorScreen;
import com.restonic4.under_control.config.ConfigProvider;
import com.restonic4.under_control.core.UnderControlConfig;
import com.restonic4.under_control.incompatibilities.ClientIncompatibilitiesManager;
import com.restonic4.under_control.api.saving.SavingAPI;
import com.restonic4.under_control.networking.PacketManager;
import com.restonic4.under_control.registries.RegistriesManager;
import com.restonic4.under_control.saving.SavingProvider;
import com.restonic4.under_control.saving.VanillaSerializableTypes;
import net.fabricmc.api.ClientModInitializer;

public class UnderControlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UnderControl.LOGGER.info("Starting client");

        SavingAPI.registerClientEvents();
        UnderControlConfig.registerClient();
        RegistriesManager.register(this);
        ClientIncompatibilitiesManager.registerClient();
        PacketManager.registerServerToClient();

        SavingProvider savingProvider = SavingAPI.registerProviderForClient(UnderControl.MOD_ID);
    }
}
