package com.chaotic_loom.under_control.client.rendering.screen_shake;

import java.util.List;

public interface ShakeCombiner {
    float combine(List<Float> intensities);
}
