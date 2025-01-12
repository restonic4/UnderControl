package com.chaotic_loom.under_control.client.rendering.effects;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.chaotic_loom.under_control.util.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;

public class Cube {
    private final long id;

    private Vector3f position;
    private Vector3f scale;
    private Vector3f rotation;
    private Color color;

    public Cube(long id) {
        this.id = id;

        this.position = new Vector3f();
        this.scale = new Vector3f(1);
        this.rotation = new Vector3f();
        this.color = new Color(0xFFFFFF);
    }

    public Cube setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Cube setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public Cube setRotation(Vector3f rotation) {
        this.rotation = rotation;
        return this;
    }

    public Cube setColor(Color color) {
        this.color = color;
        return this;
    }

    public void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        float r = MathHelper.getNormalizedColorR(color);
        float g = MathHelper.getNormalizedColorG(color);
        float b = MathHelper.getNormalizedColorB(color);
        float a = MathHelper.getNormalizedColorA(color);

        RenderSystem.setShaderColor(r, g, b, a);

        RenderingHelper.renderCube(poseStack, matrix4f, camera, this.position, this.scale, this.rotation);

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public void cleanup() {

    }

    public long getId() {
        return this.id;
    }
}
