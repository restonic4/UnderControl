package com.restonic4.under_control;

import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.restonic4.under_control.api.saving.SavingAPI;
import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.BlockEvents;
import com.restonic4.under_control.events.types.LivingEntityExtraEvents;
import com.restonic4.under_control.networking.PacketManager;
import com.restonic4.under_control.registries.RegistriesManager;
import com.restonic4.under_control.events.types.SeverExtraPlayerEvents;
import com.restonic4.under_control.saving.SavingProvider;
import com.restonic4.under_control.saving.VanillaSerializableTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnderControl implements ModInitializer {
    public static final String MOD_ID = "under_control";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Starting the library");

        SavingAPI.registerServerEvents();
        RegistriesManager.register(this);
        PacketManager.registerClientToServer();

        test();
    }

    public static void test() {
        SeverExtraPlayerEvents.VILLAGE_RAID_STARTED.register((serverPlayer, badOmenLevel) -> EventResult.CANCELED);
        SeverExtraPlayerEvents.TOUCH_ENTITY.register((player, entity) -> EventResult.CANCELED);
        SeverExtraPlayerEvents.CAN_BEW_HIT_BY_PROJECTILES.register((player) -> EventResult.CANCELED);
        BlockEvents.FARM_TRAMPLE.register((level, blockState, blockPos, entity, f) -> EventResult.CANCELED);
        BlockEvents.TURTLE_EGG_TRAMPLE.register((level, blockState, blockPos, entity, f) -> EventResult.CANCELED);
        BlockEvents.PRESSURE_PLATE_PRESSED.register((level, blockState, blockPos, entity) -> EventResult.CANCELED);
        BlockEvents.DRIPLEAF_PRESSED.register((level, blockState, blockPos, entity) -> EventResult.CANCELED);
        BlockEvents.TRIP_WIRE_PRESSED.register((level, blockState, blockPos, entity) -> EventResult.CANCELED);
        BlockEvents.RED_STONE_ORE_PRESSED.register((level, blockState, blockPos, entity) -> EventResult.CANCELED);
        BlockEvents.SCULK_LIKE_STEPPED.register((level, blockState, blockPos, entity) -> EventResult.CANCELED);
        LivingEntityExtraEvents.PUSHING.register((livingEntity -> EventResult.CANCELED));

        SavingAPI.registerProviderForWorlds(MOD_ID);
        ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
            SavingProvider savingProvider = SavingAPI.getWorldProvider(MOD_ID);

            savingProvider.registerDefaultValue("thing", 999);

            System.out.println("old thing " + savingProvider.get("thing", Integer.class));
            System.out.println("old waos " + savingProvider.get("waos", BlockPos.class));

            savingProvider.save("int", 10);
            savingProvider.save("float", 10f);
            savingProvider.save("long", 10L);
            savingProvider.save("double", 10.10);
            savingProvider.save("bool", true);
            savingProvider.save("string", "dhgfdgfg");
            savingProvider.save("waos", new BlockPos(1, 2, 3));
        });
    }
}