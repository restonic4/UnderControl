package com.chaotic_loom.under_control.client.rendering.effects;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.chaotic_loom.under_control.client.rendering.shader.ShaderHolder;
import com.chaotic_loom.under_control.client.rendering.shader.ShaderProfile;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
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

    private int renderingFlags;
    private ShaderHolder shaderHolder;
    private ShaderProfile shaderProfile;

    public Sphere(long id) {
        this.id = id;

        this.position = new Vector3f();
        this.radius = 10;
        this.color = new Color(0xFFFFFF);
        this.shaderHolder = UnderControlShaders.SIMPLE_COLOR;
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

    public Sphere setRenderingFlags(int renderingFlags) {
        this.renderingFlags = renderingFlags;
        return this;
    }

    public Sphere setShader(ShaderHolder shaderHolder) {
        this.shaderHolder = shaderHolder;
        return this;
    }

    public Sphere setShaderProfile(ShaderProfile shaderProfile) {
        this.shaderProfile = shaderProfile;
        return this;
    }

    public void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        float r = MathHelper.getNormalizedColorR(color);
        float g = MathHelper.getNormalizedColorG(color);
        float b = MathHelper.getNormalizedColorB(color);
        float a = MathHelper.getNormalizedColorA(color);

        RenderSystem.setShaderColor(r, g, b, a);

        if (this.shaderProfile != null)  {
            this.shaderProfile.apply();
            RenderingHelper.renderSphere(poseStack, matrix4f, camera, this.shaderProfile.getShaderHolder(), this.position, this.radius, this.renderingFlags);
        } else {
            RenderingHelper.renderSphere(poseStack, matrix4f, camera, this.shaderHolder, this.position, this.radius, this.renderingFlags);
        }
    }

    public void cleanup() {

    }

    public long getId() {
        return this.id;
    }
}
