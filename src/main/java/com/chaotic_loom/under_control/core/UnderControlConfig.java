package com.chaotic_loom.under_control.core;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.config.ModConfig;
import com.chaotic_loom.under_control.incompatibilities.IncompatibilitiesList;

public class UnderControlConfig extends ModConfig {
    public UnderControlConfig() {
        super(UnderControl.MOD_ID);
    }

    @Override
    protected void registerClientOptions() {
        getClientConfig().registerOption("Logs", "log_extra", false, "Logs extra information about what's going on with the library.");
    }

    @Override
    protected void registerServerOptions() {
        getServerConfig().registerOption("allow_render_command", true, "Allows the client the use of the render command.");
        getServerConfig().registerOption("mod_incompatibilities", new IncompatibilitiesList(), "It allows the server to prevent a player from joining with an incompatible mod, or you can even use it as a small layer of security to prevent users from using malicious mods.");
        getServerConfig().registerOption("Logs", "log_extra", false, "Logs extra information about what's going on with the library.");
        getServerConfig().registerOption("Logs", "log_on_reload_in_chat", true, "Logs info on chat when the server reloads.");
    }
}
