package com.chaotic_loom.under_control.events.types;

import com.mojang.datafixers.util.Pair;
import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class ShaderEvents {
    public static final Event<Registration> REGISTRATION = EventFactory.createArray(Registration.class, callbacks -> (resourceProvider, shaderPairList) -> {
        for (Registration callback : callbacks) {
            callback.onRegistration(resourceProvider, shaderPairList);
        }
    });

    @FunctionalInterface
    public interface Registration {
        void onRegistration(ResourceProvider resourceProvider, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderPairList) throws IOException;
    }
}
