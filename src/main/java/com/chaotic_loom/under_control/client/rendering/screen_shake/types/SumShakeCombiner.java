package com.chaotic_loom.under_control.client.rendering.screen_shake.types;

import com.chaotic_loom.under_control.client.rendering.screen_shake.ShakeCombiner;

import java.util.List;

public class SumShakeCombiner implements ShakeCombiner {
    @Override
    public float combine(List<Float> intensities) {
        return intensities.stream().reduce(0f, Float::sum);
    }
}
