package com.chaotic_loom.under_control.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;

public class MinecraftHelper {
    public static boolean isTaskAdvancement(Advancement advancement) {
        DisplayInfo display = advancement.getDisplay();
        return display != null && display.getFrame() == FrameType.TASK;
    }

    public static boolean isGoalAdvancement(Advancement advancement) {
        DisplayInfo display = advancement.getDisplay();
        return display != null && display.getFrame() == FrameType.GOAL;
    }

    public static boolean isChallengeAdvancement(Advancement advancement) {
        DisplayInfo display = advancement.getDisplay();
        return display != null && display.getFrame() == FrameType.CHALLENGE;
    }

    public static boolean isNotInternal(Advancement advancement) {
        return isTaskAdvancement(advancement) || isChallengeAdvancement(advancement) || isGoalAdvancement(advancement);
    }
}
