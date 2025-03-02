package com.chaotic_loom.under_control.config;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.config.ConfigAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public abstract class ModConfig {
    private final String modId;
    private ConfigProvider clientProvider;
    private ConfigProvider serverProvider;

    private boolean registered = false;

    protected ModConfig(String modId) {
        this.modId = modId;
    }

    public void register() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registerClient();
            this.registerServer(null);

            this.registered = true;
        } else {
            ServerLifecycleEvents.SERVER_STARTED.register((server)-> {
                this.registerServer(server);
                this.registered = true;
            });
        }
    }

    private void registerClient() {
        this.clientProvider = ConfigAPI.registerClientConfig(modId);
        registerClientOptions();
    }

    private void registerServer(@Nullable MinecraftServer server) {
        this.serverProvider = ConfigAPI.registerServerConfig(modId, server);
        registerServerOptions();
    }

    protected abstract void registerClientOptions();
    protected abstract void registerServerOptions();

    public ConfigProvider getClientConfig() { return clientProvider; }
    public ConfigProvider getServerConfig() { return serverProvider; }
    public boolean isRegistered() { return registered; }
}
