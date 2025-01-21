package com.chaotic_loom.under_control.client.rendering.screen_shake;

import com.chaotic_loom.under_control.util.EasingSystem;

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

    public void applyShake(float[] cameraPosition) {
        long currentTime = System.currentTimeMillis();

        if (currentTime > endTime) {
            return;
        }

        float progress = (float) (currentTime - startTime) / (endTime - startTime);
        float easedIntensity = EasingSystem.getEasedValue(progress, intensity, 0, easingType);

        // Generate random offsets within the eased intensity range.
        float offsetX = (random.nextFloat() * 2 - 1) * easedIntensity;
        float offsetY = (random.nextFloat() * 2 - 1) * easedIntensity;

        // Apply offsets to camera position.
        cameraPosition[0] += offsetX;
        cameraPosition[1] += offsetY;
    }

    public float getCurrentIntensity(long currentTime) {
        if (currentTime > endTime) {
            return 0;
        }

        float progress = (float) (currentTime - startTime) / (endTime - startTime);
        return EasingSystem.getEasedValue(progress, intensity, 0, easingType);
    }

    public boolean isShakeActive() {
        return System.currentTimeMillis() <= endTime;
    }

    public boolean isFinished() {
        return !isShakeActive();
    }
}
