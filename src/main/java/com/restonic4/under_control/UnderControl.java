package com.restonic4.under_control;

import com.restonic4.under_control.registries.RegistriesManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnderControl implements ModInitializer {
    public static final String MOD_ID = "under_control";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Starting the library");

        RegistriesManager.register(this);
    }
}
