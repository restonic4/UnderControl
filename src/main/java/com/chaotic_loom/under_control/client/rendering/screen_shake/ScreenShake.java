package com.chaotic_loom.under_control.client.rendering.screen_shake;

import com.chaotic_loom.under_control.util.EasingSystem;
import com.chaotic_loom.under_control.util.MathHelper;

import java.util.Random;

public class ScreenShake {
    private long startTime;
    private long endTime;
    private float intensity;
    private final Random random = new Random();
    private EasingSystem.EasingType easingType;

    public ScreenShake(long durationMillis, float intensity, EasingSystem.EasingType easingType) {
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + durationMillis;
        this.intensity = intensity;
        this.easingType = easingType;
    }

    public float getCurrentIntensity(long currentTime) {
        if (currentTime > endTime) {
            return 0;
        }

        return EasingSystem.getEasedValue(startTime, endTime, intensity, 0, easingType);
    }

    public boolean isShakeActive() {
        return System.currentTimeMillis() <= endTime;
    }

    public boolean isFinished() {
        return !isShakeActive();
    }
}
