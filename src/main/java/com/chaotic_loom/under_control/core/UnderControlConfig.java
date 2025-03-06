package com.chaotic_loom.under_control.core;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.config.ModConfig;
import com.chaotic_loom.under_control.incompatibilities.IncompatibilitiesList;
import net.minecraft.core.BlockPos;

public class UnderControlConfig extends ModConfig {
    private static final String logsGroup = "logs";
    private static final String otherGroup = "other";
    private static final String commandsGroup = "commands";
    private static final String testGroup = "testing";

    public UnderControlConfig() {
        super(UnderControl.MOD_ID);
    }

    @Override
    protected void registerClientOptions() {
        getClientConfig().registerOption(logsGroup, "log_extra", false);
    }

    @Override
    protected void registerServerOptions() {
        getServerConfig().registerOption(commandsGroup, "allow_render_command", true);
        getServerConfig().registerOption(otherGroup, "mod_incompatibilities", new IncompatibilitiesList());
        getServerConfig().registerOption(logsGroup, "log_extra", false);
        getServerConfig().registerOption(logsGroup, "log_on_reload_in_chat", true);

        getServerConfig().registerOption(testGroup, "test_boolean", true);
        getServerConfig().registerOption(testGroup, "test_int", 1);
        getServerConfig().registerOption(testGroup, "test_float", 1.5f);
        getServerConfig().registerOption(testGroup, "test_long", 100L);
        getServerConfig().registerOption(testGroup, "test_double", 100.5);
        getServerConfig().registerOption(testGroup, "test_string", "Hello world!");
        getServerConfig().registerOption(testGroup, "test_blockpos", new BlockPos(10, 20, 30));
    }
}
