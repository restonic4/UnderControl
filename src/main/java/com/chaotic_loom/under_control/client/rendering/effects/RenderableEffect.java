package com.chaotic_loom.under_control.client.rendering.effects;

import com.chaotic_loom.under_control.client.rendering.shader.ShaderHolder;
import com.chaotic_loom.under_control.client.rendering.shader.ShaderProfile;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.util.MathHelper;
import com.chaotic_loom.under_control.util.pooling.Poolable;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public abstract class RenderableEffect implements Poolable {
    private String id;
    protected Vector3f position;
    protected Vector3f scale;
    protected Vector3f rotation;
    protected Color color;
    protected int renderingFlags;
    protected ShaderHolder shaderHolder;
    private ShaderProfile shaderProfile;

    public RenderableEffect(String id) {
        initialize(id);
    }

    public void initialize(String id) {
        this.id = id;
        this.position = new Vector3f();
        this.scale = new Vector3f(1);
        this.rotation = new Vector3f();
        this.color = new Color(0xFFFFFF);
        this.shaderHolder = UnderControlShaders.SIMPLE_COLOR;
    }

    public RenderableEffect setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public RenderableEffect setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public RenderableEffect setRotation(Vector3f rotation) {
        this.rotation = rotation;
        return this;
    }

    public RenderableEffect setColor(Color color) {
        this.color = color;
        return this;
    }

    public RenderableEffect setRenderingFlags(int renderingFlags) {
        this.renderingFlags = renderingFlags;
        return this;
    }

    public RenderableEffect setShader(ShaderHolder shaderHolder) {
        this.shaderHolder = shaderHolder;
        return this;
    }

    public RenderableEffect setShaderProfile(ShaderProfile shaderProfile) {
        this.shaderProfile = shaderProfile;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public ShaderHolder getAndApplyShader() {
        if (this.shaderProfile != null)  {
            this.shaderProfile.apply();
            return this.shaderProfile.getShaderHolder();
        }

        return this.shaderHolder;
    }

    public abstract void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera);

    public void cleanup() {
        // Cleanup resources if needed
    }

    @Override
    public void reset() {

    }
}
