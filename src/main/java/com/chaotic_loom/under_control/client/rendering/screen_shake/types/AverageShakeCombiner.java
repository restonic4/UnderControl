package com.chaotic_loom.under_control.client.rendering.screen_shake.types;

import com.chaotic_loom.under_control.client.rendering.screen_shake.ShakeCombiner;

import java.util.List;

public class AverageShakeCombiner implements ShakeCombiner {
    @Override
    public float combine(List<Float> intensities) {
        return intensities.isEmpty() ? 0f :
                (float) intensities.stream().mapToDouble(i -> i).average().orElse(0f);
    }
}