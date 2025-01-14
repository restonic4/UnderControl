package com.chaotic_loom.under_control.client.rendering.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SphereManager {
    private static final Map<Long, Sphere> spheres = new HashMap<>();
    private static final List<Long> spheresToDelete = new ArrayList<>();

    public static Sphere create(long id) {
        Sphere orbEffect = new Sphere(id);
        spheres.put(id, orbEffect);

        return orbEffect;
    }

    public static void delete(long id) {
        spheresToDelete.add(id);
    }

    public static Sphere get(long id) {
        return spheres.get(id);
    }

    public static void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        for (int i = spheresToDelete.size() - 1; i >= 0; i--) {
            long sphereId = spheresToDelete.get(i);

            spheres.remove(sphereId);
            spheresToDelete.remove(i);
        }

        for (Map.Entry<Long, Sphere> entry : spheres.entrySet()) {
            entry.getValue().render(poseStack, matrix4f, camera);
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
