package com.chaotic_loom.under_control.client.rendering.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;

import java.util.*;

public class EffectManager {
    private static final Map<String, RenderableEffect> effects = new HashMap<>();
    private static final List<String> effectsToDelete = new ArrayList<>();

    public static RenderableEffect add(RenderableEffect effect) {
        effects.put(effect.getId(), effect);
        return effect;
    }

    public static void delete(String id) {
        effectsToDelete.add(id);
    }

    public static RenderableEffect get(String id) {
        return effects.get(id);
    }

    public static void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        for (String id : effectsToDelete) {
            effects.remove(id);
        }
        effectsToDelete.clear();

        for (RenderableEffect effect : effects.values()) {
            effect.render(poseStack, matrix4f, camera);
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}