package com.restonic4.under_control.core;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.config.ConfigProvider;
import com.restonic4.under_control.incompatibilities.IncompatibilitiesList;
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
        clientConfigProvider.registerOption("log_extra", false, "Logs extra information about what's going on with the library.");
    }

    private static void setUpServer() {
        serverConfigProvider.registerOption("log_extra", false, "Logs extra information about what's going on with the library.");
        serverConfigProvider.registerOption("allow_render_command", true, "Allows the client the use of the render command.");
        serverConfigProvider.registerOption("mod_incompatibilities", new IncompatibilitiesList(), "It allows the server to prevent a player from joining with an incompatible mod, or you can even use it as a small layer of security to prevent users from using malicious mods.");
        serverConfigProvider.registerOption("a", "aaaaaaaaaa", "no");
    }
}
