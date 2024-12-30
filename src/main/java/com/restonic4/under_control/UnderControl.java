package com.restonic4.under_control;

import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.restonic4.under_control.api.saving.SavingAPI;
import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.BlockEvents;
import com.restonic4.under_control.events.types.LivingEntityExtraEvents;
import com.restonic4.under_control.networking.PacketManager;
import com.restonic4.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import com.restonic4.under_control.registries.RegistriesManager;
import com.restonic4.under_control.events.types.SeverExtraPlayerEvents;
import com.restonic4.under_control.saving.SavingProvider;
import com.restonic4.under_control.saving.VanillaSerializableTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnderControl implements ModInitializer {
    public static final String MOD_ID = "under_control";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Starting the library");

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