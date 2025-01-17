package com.chaotic_loom.under_control.client.rendering.effects;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.chaotic_loom.under_control.client.rendering.shader.ShaderHolder;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.util.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;

@Environment(value = EnvType.CLIENT)
public class Cylinder extends RenderableEffect {
    public Cylinder(String id) {
        super(id);
    }

    public void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        float r = MathHelper.getNormalizedColorR(color);
        float g = MathHelper.getNormalizedColorG(color);
        float b = MathHelper.getNormalizedColorB(color);
        float a = MathHelper.getNormalizedColorA(color);

        RenderSystem.setShaderColor(r, g, b, a);

        RenderingHelper.renderCylinder(poseStack, matrix4f, camera, getAndApplyShader(), this.position, this.scale, this.rotation, this.renderingFlags);
    }
}
