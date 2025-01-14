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
public class CubeManager {
    private static final Map<Long, Cube> cubes = new HashMap<>();
    private static final List<Long> cubesToDelete = new ArrayList<>();

    public static Cube create(long id) {
        Cube orbEffect = new Cube(id);
        cubes.put(id, orbEffect);

        return orbEffect;
    }

    public static void delete(long id) {
        cubesToDelete.add(id);
    }

    public static Cube get(long id) {
        return cubes.get(id);
    }

    public static void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        for (int i = cubesToDelete.size() - 1; i >= 0; i--) {
            long sphereId = cubesToDelete.get(i);

            cubes.remove(sphereId);
            cubesToDelete.remove(i);
        }

        for (Map.Entry<Long, Cube> entry : cubes.entrySet()) {
            entry.getValue().render(poseStack, matrix4f, camera);
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
