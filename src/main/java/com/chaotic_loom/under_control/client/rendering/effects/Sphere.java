package com.chaotic_loom.under_control.client.rendering.effects;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.chaotic_loom.under_control.util.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;

public class Sphere {
    private final long id;

    private Vector3f position;
    private float radius;
    private Color color;

    public Sphere(long id) {
        this.id = id;

        this.position = new Vector3f();
        this.radius = 10;
        this.color = new Color(0xFFFFFF);
    }

    public Sphere setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Sphere setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public Sphere setColor(Color color) {
        this.color = color;
        return this;
    }

    public void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        float r = MathHelper.getNormalizedColorR(color);
        float g = MathHelper.getNormalizedColorG(color);
        float b = MathHelper.getNormalizedColorB(color);
        float a = MathHelper.getNormalizedColorA(color);

        RenderSystem.setShaderColor(r, g, b, a);

        RenderingHelper.renderSphere(poseStack, matrix4f, camera, this.position, this.radius);

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public void cleanup() {

    }

    public long getId() {
        return this.id;
    }
}
