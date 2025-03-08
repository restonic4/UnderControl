package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class RegistrationEvents {
    public static final Event<SoundSource> SOUND_SOURCE = EventFactory.createArray(SoundSource.class, callbacks -> (soundSourceID) -> {
        for (SoundSource callback : callbacks) {
            callback.onEvent(soundSourceID);
        }
    });

    @FunctionalInterface
    public interface SoundSource {
        void onEvent(String soundSourceID);
    }

    public static final Event<SoundSourceCompleted> SOUND_SOURCE_COMPLETED = EventFactory.createArray(SoundSourceCompleted.class, callbacks -> () -> {
        for (SoundSourceCompleted callback : callbacks) {
            callback.onEvent();
        }
    });

    @FunctionalInterface
    public interface SoundSourceCompleted {
        void onEvent();
    }
}
