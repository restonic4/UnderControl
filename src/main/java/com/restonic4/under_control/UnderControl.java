package com.restonic4.under_control;

import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.saving.SavingAPI;
import com.restonic4.under_control.api.whitelist.WhitelistAPI;
import com.restonic4.under_control.networking.PacketManager;
import com.restonic4.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import com.restonic4.under_control.registries.RegistriesManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnderControl implements ModInitializer {
    public static final String MOD_ID = "under_control";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Starting the library, common side");

        SavingAPI.registerServerEvents();
        ConfigAPI.registerServerEvents();
        RegistriesManager.register(this);
        PacketManager.registerClientToServer();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();

            UpdateServerDataOnClient.sendToClient(player);
        });
    }
}