package com.chaotic_loom.under_control.client.rendering.render_layers;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CubeGeometry extends GeometryData {

    @Override
    public <T extends GeometryData> void buildVertices(Vector4f color, PoseStack poseStack, VertexConsumer vertexConsumer, RenderLayerRenderer<T> renderLayerRenderer) {
        Matrix4f matrix = poseStack.last().pose();
        float size = 0.5f;

        // Front
        addVertex(vertexConsumer, matrix, -size, -size, size, 0, 1, color, 0, 0, 1);
        addVertex(vertexConsumer, matrix, size, -size, size, 1, 1, color, 0, 0, 1);
        addVertex(vertexConsumer, matrix, size, size, size, 1, 0, color, 0, 0, 1);
        addVertex(vertexConsumer, matrix, -size, size, size, 0, 0, color, 0, 0, 1);

        // Back
        addVertex(vertexConsumer, matrix, size, -size, -size, 0, 1, color, 0, 0, -1);
        addVertex(vertexConsumer, matrix, -size, -size, -size, 1, 1, color, 0, 0, -1);
        addVertex(vertexConsumer, matrix, -size, size, -size, 1, 0, color, 0, 0, -1);
        addVertex(vertexConsumer, matrix, size, size, -size, 0, 0, color, 0, 0, -1);

        // Top
        addVertex(vertexConsumer, matrix, -size, size, -size, 0, 1, color, 0, 1, 0);
        addVertex(vertexConsumer, matrix, -size, size, size, 0, 0, color, 0, 1, 0);
        addVertex(vertexConsumer, matrix, size, size, size, 1, 0, color, 0, 1, 0);
        addVertex(vertexConsumer, matrix, size, size, -size, 1, 1, color, 0, 1, 0);

        // Bottom
        addVertex(vertexConsumer, matrix, -size, -size, size, 0, 1, color, 0, -1, 0);
        addVertex(vertexConsumer, matrix, -size, -size, -size, 0, 0, color, 0, -1, 0);
        addVertex(vertexConsumer, matrix, size, -size, -size, 1, 0, color, 0, -1, 0);
        addVertex(vertexConsumer, matrix, size, -size, size, 1, 1, color, 0, -1, 0);

        // Left
        addVertex(vertexConsumer, matrix, -size, -size, -size, 0, 1, color, -1, 0, 0);
        addVertex(vertexConsumer, matrix, -size, -size, size, 1, 1, color, -1, 0, 0);
        addVertex(vertexConsumer, matrix, -size, size, size, 1, 0, color, -1, 0, 0);
        addVertex(vertexConsumer, matrix, -size, size, -size, 0, 0, color, -1, 0, 0);

        // Right
        addVertex(vertexConsumer, matrix, size, -size, size, 0, 1, color, 1, 0, 0);
        addVertex(vertexConsumer, matrix, size, -size, -size, 1, 1, color, 1, 0, 0);
        addVertex(vertexConsumer, matrix, size, size, -size, 1, 0, color, 1, 0, 0);
        addVertex(vertexConsumer, matrix, size, size, size, 0, 0, color, 1, 0, 0);
    }
}
