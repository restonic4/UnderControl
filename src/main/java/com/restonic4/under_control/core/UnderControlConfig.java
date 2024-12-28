package com.restonic4.under_control.core;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.config.ConfigProvider;
import com.restonic4.under_control.saving.SavingProvider;
import net.minecraft.server.MinecraftServer;

public class UnderControlConfig {
    private static ConfigProvider clientConfigProvider = null;
    private static ConfigProvider serverConfigProvider = null;

    public static void registerClient() {
        clientConfigProvider = ConfigAPI.registerClientConfig(UnderControl.MOD_ID);
        serverConfigProvider = ConfigAPI.registerServerConfig(UnderControl.MOD_ID, null);

        setUp();
    }

    public static void registerServer(MinecraftServer minecraftServer) {
        serverConfigProvider = ConfigAPI.registerServerConfig(UnderControl.MOD_ID, minecraftServer);

        setUp();
    }

    private static void setUp() {
        if (clientConfigProvider != null) {
            setUpClient();
        }

        setUpServer();
    }

    private static void setUpClient() {
        clientConfigProvider.registerDefaultValue("log_extra", false);
    }

    private static void setUpServer() {
        serverConfigProvider.registerDefaultValue("log_extra", false);
    }
}
