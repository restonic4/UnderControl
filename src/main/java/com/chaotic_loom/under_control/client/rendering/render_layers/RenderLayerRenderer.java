package com.chaotic_loom.under_control.client.rendering.render_layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.function.Supplier;

public class RenderLayerRenderer<T extends GeometryData> {
    Minecraft client = Minecraft.getInstance();

    private final Vector3f position = new Vector3f(0, 0, 0);
    private final Vector3f scale = new Vector3f(1, 1, 1);
    private final Vector3f rotation = new Vector3f(0, 0, 0);
    private final Vector4f color = new Vector4f(1, 1, 1, 1);

    private final String id;
    private ResourceLocation texture;
    private final RenderType renderType;
    private final T geometryData;

    public RenderLayerRenderer(String id, ResourceLocation texture, RenderType renderType, T geometryData) {
        this.id = id;
        this.texture = texture;
        this.renderType = renderType;
        this.geometryData = geometryData;
    }

    public void render(PoseStack matrixStack, MultiBufferSource bufferSource) {
        matrixStack.pushPose();
        this.applyTransformations(matrixStack);

        RenderType renderType = this.renderType;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        this.geometryData.buildVertices(this.color, matrixStack, vertexConsumer, this);

        matrixStack.popPose();
    }

    protected void applyTransformations(PoseStack matrixStack) {
        matrixStack.translate(this.position.x, this.position.y, this.position.z);
        matrixStack.mulPose(Axis.XP.rotationDegrees(this.rotation.x));
        matrixStack.mulPose(Axis.YP.rotationDegrees(this.rotation.y));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(this.rotation.z));
        matrixStack.scale(this.scale.x, this.scale.y, this.scale.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector4f getColor() {
        return color;
    }

    public String getId() {
        return id;
    }
}
