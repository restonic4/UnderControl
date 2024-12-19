package com.restonic4.under_control.events.types;

import com.mojang.datafixers.util.Pair;
import com.restonic4.under_control.events.Event;
import com.restonic4.under_control.events.EventFactory;
import com.restonic4.under_control.events.EventResult;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
