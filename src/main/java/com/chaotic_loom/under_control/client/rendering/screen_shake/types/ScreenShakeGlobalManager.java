package com.chaotic_loom.under_control.client.rendering.screen_shake.types;

import com.chaotic_loom.under_control.client.rendering.screen_shake.ScreenShakeManager;
import com.chaotic_loom.under_control.client.rendering.screen_shake.ShakeCombiner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenShakeGlobalManager {
    private static final List<ScreenShakeManager> managers = new ArrayList<>();
    private static ShakeCombiner globalCombiner = new SumShakeCombiner();
    private static float maxGlobalIntensity = 1000;

    public static void addManager(ScreenShakeManager manager) {
        managers.add(manager);
    }

    public static float computeGlobalShakeOffset() {
        List<Float> intensities = new ArrayList<>();
        Iterator<ScreenShakeManager> iterator = managers.iterator();

        while (iterator.hasNext()) {
            ScreenShakeManager manager = iterator.next();
            float managerIntensity = manager.computeShakeOffset();

            if (managerIntensity > 0) {
                intensities.add(managerIntensity);
            }
        }

        float combinedGlobalIntensity = globalCombiner.combine(intensities);
        return Math.min(combinedGlobalIntensity, maxGlobalIntensity);
    }


    public static void setGlobalCombiner(ShakeCombiner shakeCombiner) {
        globalCombiner = shakeCombiner;
    }

    public static ShakeCombiner getGlobalCombiner() {
        return globalCombiner;
    }

    public static void setMaxGlobalIntensity(float maxIntensity) {
        maxGlobalIntensity = maxIntensity;
    }

    public static float getMaxGlobalIntensity() {
        return maxGlobalIntensity;
    }
}