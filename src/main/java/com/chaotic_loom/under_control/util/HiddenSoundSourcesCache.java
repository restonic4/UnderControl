package com.chaotic_loom.under_control.util;

import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;

public class HiddenSoundSourcesCache {
    private static final List<SoundSource> excludedSources = new ArrayList<>();

    public static void registerSoundSource(SoundSource soundSource) {
        excludedSources.add(soundSource);
    }

    public static boolean isHidden(SoundSource soundSource) {
        return excludedSources.contains(soundSource);
    }
}
