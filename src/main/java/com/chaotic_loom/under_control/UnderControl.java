package com.chaotic_loom.under_control;

import com.chaotic_loom.under_control.api.config.ConfigAPI;
import com.chaotic_loom.under_control.api.registry.UnderControlRegistries;
import com.chaotic_loom.under_control.api.registry.UnderControlRegistry;
import com.chaotic_loom.under_control.api.saving.SavingAPI;
import com.chaotic_loom.under_control.api.server.ServerAPI;
import com.chaotic_loom.under_control.config.ConfigManager;
import com.chaotic_loom.under_control.config.ConfigProvider;
import com.chaotic_loom.under_control.core.UnderControlConfig;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.debug.Debugger;
import com.chaotic_loom.under_control.debug.TickingDebugger;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.chaotic_loom.under_control.networking.services.ApiClient;
import com.chaotic_loom.under_control.registries.core.ObjectIdentifier;
import com.chaotic_loom.under_control.saving.SavingManager;
import com.chaotic_loom.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import com.chaotic_loom.under_control.registries.RegistriesManager;
import com.chaotic_loom.under_control.saving.custom.VanishList;
import com.chaotic_loom.under_control.vanish.VanishManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnderControl implements ModInitializer {
    public static final String MOD_ID = "under_control";
    public static final String MOD_NAME = "Under Control";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ApiClient CHAOTIC_LOOM_API = new ApiClient("https://chaotic-loom.com/api/");

    private static final UnderControlConfig config = new UnderControlConfig();

    @Override
    public void onInitialize() {
        LOGGER.info("Starting the library, common side");

        SavingManager.registerServerEvents();
        SavingAPI.registerProviderForWorlds(MOD_ID);
        SavingAPI.registerClassProvider(new VanishList());

        ConfigManager.registerServerEvents();

        VanishManager.registerServerEvents();

        RegistriesManager.collectModPackages();
        RegistriesManager.startRegistrationAnnotationCollection(ExecutionSide.COMMON);
        RegistriesManager.startPacketAnnotationCollection(PacketDirection.CLIENT_TO_SERVER);

        config.register();

        List<Debugger> debuggers = UnderControlRegistry.getRegistryValues(UnderControlRegistries.DEBUGGER);
        List<TickingDebugger> tickingDebuggers = new ArrayList<>();

        for (Debugger debugger : debuggers) {
            if (debugger.getExecutionSide() != ExecutionSide.CLIENT) {
                debugger.onInitialize();

                if (debugger instanceof TickingDebugger tickingDebugger) {
                    tickingDebuggers.add(tickingDebugger);
                }
            }

        }

        ServerTickEvents.START_SERVER_TICK.register((minecraftServer) -> {
            for (int i = 0; i < tickingDebuggers.size(); i++) {
                TickingDebugger debugger = tickingDebuggers.get(i);
                debugger.internalTick();
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();

            UpdateServerDataOnClient.sendToClient(player);
        });
    }

    private static ConfigProvider getProvider() {
        if (ConfigAPI.getServerProvider(UnderControl.MOD_ID) == null) {
            return ConfigAPI.getClientProvider(UnderControl.MOD_ID);
        }

        return ConfigAPI.getServerProvider(UnderControl.MOD_ID);
    }
}