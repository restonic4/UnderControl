package com.chaotic_loom.under_control.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.util.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderingHelper {
    public static void renderDynamicGeometry(PoseStack poseStack, Matrix4f matrix4f, Camera camera, VertexFormat.Mode mode, Vector3f[] vertices) {
        BufferBuilder.RenderedBuffer renderedBuffer = buildGeometryReusable(mode, vertices);
        renderQuad(generateReusableBuffer(renderedBuffer), poseStack, matrix4f, camera);
    }

    public static void renderQuad(VertexBuffer vertexBuffer, PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        poseStack.pushPose();
        poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), matrix4f, UnderControlShaders.SIMPLE_COLOR.getInstance().get());

        poseStack.popPose();

        RenderSystem.depthMask(true);
    }

    public static VertexBuffer generateBuffer(BufferBuilder.RenderedBuffer renderedBuffer) {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

        buffer.bind();
        buffer.upload(renderedBuffer);
        VertexBuffer.unbind();

        return buffer;
    }

    private static final VertexBuffer reusableVertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
    public static VertexBuffer generateReusableBuffer(BufferBuilder.RenderedBuffer renderedBuffer) {
        reusableVertexBuffer.bind();
        reusableVertexBuffer.upload(renderedBuffer);
        VertexBuffer.unbind();
        return reusableVertexBuffer;
    }

    public static BufferBuilder.RenderedBuffer buildGeometry(BufferBuilder bufferBuilder, VertexFormat.Mode mode, Vector3f[] positions) {
        RenderSystem.setShader(GameRenderer::getPositionShader);

        bufferBuilder.begin(mode, DefaultVertexFormat.POSITION);

        for (int i = 0; i < positions.length; i++) {
            bufferBuilder.vertex(positions[i].x, positions[i].y, positions[i].z).endVertex();
        }

        return bufferBuilder.end();
    }

    private static final BufferBuilder reusableBufferBuilder = Tesselator.getInstance().getBuilder();
    public static BufferBuilder.RenderedBuffer buildGeometryReusable(VertexFormat.Mode mode, Vector3f[] positions) {
        reusableBufferBuilder.begin(mode, DefaultVertexFormat.POSITION);
        for (Vector3f position : positions) {
            reusableBufferBuilder.vertex(position.x, position.y, position.z).endVertex();
        }
        return reusableBufferBuilder.end();
    }

    private static final Map<String, VertexBuffer> dynamicBuffers = new HashMap<>();
    public static VertexBuffer getDynamicBuffer(String key, BufferBuilder.RenderedBuffer renderedBuffer) {
        VertexBuffer buffer = dynamicBuffers.computeIfAbsent(key, k -> new VertexBuffer(VertexBuffer.Usage.DYNAMIC));
        buffer.bind();
        buffer.upload(renderedBuffer);
        VertexBuffer.unbind();
        return buffer;
    }

    public static void renderBeam3D(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, float width, float height) {
        List<Vector3f[]> vector3fList = RenderShapes.BEAM.getVertices();

        for (int i = 0; i < vector3fList.size(); i++) {
            Vector3f[] vector3fs = vector3fList.get(i);

            MathHelper.scaleVertices(vector3fs, width, height, width);
            MathHelper.translateVertices(vector3fs, position.x - width/2, position.y, position.z - width/2);
            RenderingHelper.renderDynamicGeometry(poseStack, matrix4f, camera, VertexFormat.Mode.TRIANGLE_FAN, vector3fs);
        }
    }

    public static void renderBillboardQuad(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f pointA, Vector3f pointB, float width) {
        Vector3f delta = new Vector3f(pointB).sub(pointA);

        Vector3f cameraDirection = new Vector3f(camera.getPosition().toVector3f()).sub(pointA).normalize();

        Vector3f normal = new Vector3f(cameraDirection).cross(delta).normalize().mul(width / 2f, width / 2f, width / 2f);

        Vector3f topLeft = new Vector3f(pointA).sub(normal);
        Vector3f topRight = new Vector3f(pointA).add(normal);
        Vector3f bottomLeft = new Vector3f(pointB).sub(normal);
        Vector3f bottomRight = new Vector3f(pointB).add(normal);

        Vector3f[] vertices = new Vector3f[] { topLeft, bottomLeft, bottomRight, topRight };

        BufferBuilder.RenderedBuffer renderedBuffer = buildGeometry(Tesselator.getInstance().getBuilder(), VertexFormat.Mode.QUADS, vertices);
        renderQuad(generateBuffer(renderedBuffer), poseStack, matrix4f, camera);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, float radius) {
        List<Vector3f[]> vector3fList = RenderShapes.SPHERE.getVertices();

        for (int i = 0; i < vector3fList.size(); i++) {
            Vector3f[] vector3fs = vector3fList.get(i);

            MathHelper.scaleVertices(vector3fs, radius, radius, radius);
            MathHelper.translateVertices(vector3fs, position.x, position.y, position.z);
            RenderingHelper.renderDynamicGeometry(poseStack, matrix4f, camera, VertexFormat.Mode.TRIANGLES, vector3fs);
        }
    }
}
