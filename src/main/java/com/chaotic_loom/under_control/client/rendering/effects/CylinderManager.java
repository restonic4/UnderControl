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
@Deprecated(forRemoval = true)
public class CylinderManager {
    private static final Map<Long, Cylinder> cylinders = new HashMap<>();
    private static final List<Long> cylindersToDelete = new ArrayList<>();

    public static Cylinder create(long id) {
        Cylinder orbEffect = new Cylinder(id);
        cylinders.put(id, orbEffect);

        return orbEffect;
    }

    public static void delete(long id) {
        cylindersToDelete.add(id);
    }

    public static Cylinder get(long id) {
        return cylinders.get(id);
    }

    public static void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        for (int i = cylindersToDelete.size() - 1; i >= 0; i--) {
            long sphereId = cylindersToDelete.get(i);

            cylinders.remove(sphereId);
            cylindersToDelete.remove(i);
        }

        for (Map.Entry<Long, Cylinder> entry : cylinders.entrySet()) {
            entry.getValue().render(poseStack, matrix4f, camera);
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
