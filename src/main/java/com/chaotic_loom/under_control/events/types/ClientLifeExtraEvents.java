package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.chaotic_loom.under_control.events.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class ClientLifeExtraEvents {
    public static final Event<ClientStartedDelayed> CLIENT_STARTED_DELAYED = EventFactory.createArray(ClientStartedDelayed.class, callbacks -> (minecraft) -> {
        for (ClientStartedDelayed callback : callbacks) {
            callback.onClientStartedDelayed(minecraft);
        }
    });

    @FunctionalInterface
    public interface ClientStartedDelayed {
        void onClientStartedDelayed(Minecraft minecraft);
    }
}
