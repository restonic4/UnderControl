package com.chaotic_loom.under_control.client.rendering.screen_shake;

import com.chaotic_loom.under_control.client.rendering.screen_shake.types.ScreenShakeGlobalManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenShakeManager {
    private final List<ScreenShake> activeShakes = new ArrayList<>();
    private final ShakeCombiner combiner;
    private final float maxIntensity;

    public ScreenShakeManager(ShakeCombiner combiner, float maxIntensity) {
        this.combiner = combiner;
        this.maxIntensity = maxIntensity;
    }

    public static ScreenShakeManager create(ShakeCombiner combiner, float maxIntensity) {
        ScreenShakeManager screenShakeManager = new ScreenShakeManager(combiner, maxIntensity);

        ScreenShakeGlobalManager.addManager(screenShakeManager);

        return screenShakeManager;
    }

    public void addShake(ScreenShake shake) {
        activeShakes.add(shake);
    }

    public float computeShakeOffset() {
        List<Float> intensities = new ArrayList<>();
        Iterator<ScreenShake> iterator = activeShakes.iterator();
        long currentTime = System.currentTimeMillis();

        while (iterator.hasNext()) {
            ScreenShake shake = iterator.next();

            if (shake.isFinished()) {
                iterator.remove();
            } else {
                float intensity = shake.getCurrentIntensity(currentTime);

                if (intensity > 0) {
                    intensities.add(intensity);
                }
            }
        }

        float combinedIntensity = combiner.combine(intensities);
        return Math.min(combinedIntensity, maxIntensity);
    }

}
