package com.chaotic_loom.under_control.client.rendering.render_layers;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class GeometryData {
    public abstract <T extends GeometryData> void buildVertices(Vector4f color, PoseStack poseStack, VertexConsumer vertexConsumer, RenderLayerRenderer<T> renderLayerRenderer);

    public static void addVertex(VertexConsumer vertexConsumer, Matrix4f matrix, float sizeX, float sizeY, float sizeZ, float u, float v, Vector4f color, float nx, float ny, float nz) {
        RenderingHelper.RenderLayers.addVertex(vertexConsumer, matrix, sizeX, sizeY, sizeZ, u, v, color.x, color.y, color.z, color.w, nx, ny, nz);
    }
}
