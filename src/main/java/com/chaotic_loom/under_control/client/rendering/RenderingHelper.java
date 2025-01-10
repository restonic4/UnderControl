package com.chaotic_loom.under_control.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.util.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
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

    public static void renderBeam3D(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 position, float width, float height) {
        renderBeam3D(poseStack, matrix4f, camera, (float) position.x, (float) position.y, (float) position.z, width, height);
    }

    public static void renderBeam3D(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, float width, float height) {
        renderBeam3D(poseStack, matrix4f, camera, position.x, position.y, position.z, width, height);
    }

    public static void renderBeam3D(PoseStack poseStack, Matrix4f matrix4f, Camera camera, float x, float y, float z, float width, float height) {
        List<Vector3f[]> vector3fList = RenderShapes.BEAM.getVertices();

        for (int i = 0; i < vector3fList.size(); i++) {
            Vector3f[] vector3fs = vector3fList.get(i);

            MathHelper.scaleVertices(vector3fs, width, height, width);
            MathHelper.translateVertices(vector3fs, x - width/2, y, z - width/2);
            RenderingHelper.renderDynamicGeometry(poseStack, matrix4f, camera, VertexFormat.Mode.TRIANGLE_FAN, vector3fs);
        }
    }

    public static void renderBillboardQuad(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 pointA, Vec3 pointB, float width) {
        renderBillboardQuad(poseStack, matrix4f, camera, (float) pointA.x, (float) pointA.y, (float) pointA.z, (float) pointB.x, (float) pointB.y, (float) pointB.z, width);
    }

    public static void renderBillboardQuad(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f pointA, Vector3f pointB, float width) {
        renderBillboardQuad(poseStack, matrix4f, camera, pointA.x, pointA.y, pointA.z, pointB.x, pointB.y, pointB.z, width);
    }

    public static void renderBillboardQuad(PoseStack poseStack, Matrix4f matrix4f, Camera camera, float xA, float yA, float zA, float xB, float yB, float zB, float width) {
        // Calculate the delta vector between point A and point B
        float deltaX = xB - xA;
        float deltaY = yB - yA;
        float deltaZ = zB - zA;

        // Get the camera's direction relative to point A
        float camX = (float) camera.getPosition().x - xA;
        float camY = (float) camera.getPosition().y - yA;
        float camZ = (float) camera.getPosition().z - zA;

        // Normalize the camera direction vector
        float camLength = (float) Math.sqrt(camX * camX + camY * camY + camZ * camZ);
        camX /= camLength;
        camY /= camLength;
        camZ /= camLength;

        // Calculate the cross product of the camera direction and delta (to find the normal vector)
        float normalX = camY * deltaZ - camZ * deltaY;
        float normalY = camZ * deltaX - camX * deltaZ;
        float normalZ = camX * deltaY - camY * deltaX;

        // Normalize the normal vector and scale it by half the width
        float normalLength = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        normalX = (normalX / normalLength) * (width / 2f);
        normalY = (normalY / normalLength) * (width / 2f);
        normalZ = (normalZ / normalLength) * (width / 2f);

        // Calculate the positions of the quad's vertices
        float topLeftX = xA - normalX;
        float topLeftY = yA - normalY;
        float topLeftZ = zA - normalZ;

        float topRightX = xA + normalX;
        float topRightY = yA + normalY;
        float topRightZ = zA + normalZ;

        float bottomLeftX = xB - normalX;
        float bottomLeftY = yB - normalY;
        float bottomLeftZ = zB - normalZ;

        float bottomRightX = xB + normalX;
        float bottomRightY = yB + normalY;
        float bottomRightZ = zB + normalZ;

        // Retrieve the pre-allocated quad vertices array
        Vector3f[] vertices = RenderShapes.QUAD.getVertices().get(0);

        // Assign the calculated vertex positions to the array
        vertices[0].set(topLeftX, topLeftY, topLeftZ);
        vertices[1].set(bottomLeftX, bottomLeftY, bottomLeftZ);
        vertices[2].set(bottomRightX, bottomRightY, bottomRightZ);
        vertices[3].set(topRightX, topRightY, topRightZ);

        // Build the quad geometry using the vertex data
        BufferBuilder.RenderedBuffer renderedBuffer = buildGeometry(Tesselator.getInstance().getBuilder(), VertexFormat.Mode.QUADS, vertices);

        // Render the quad
        renderQuad(generateBuffer(renderedBuffer), poseStack, matrix4f, camera);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 position, float radius) {
        renderSphere(poseStack, matrix4f, camera, (float) position.x, (float) position.y, (float) position.z, radius);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, float radius) {
        renderSphere(poseStack, matrix4f, camera, position.x, position.y, position.z, radius);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, float x, float y, float z, float radius) {
        List<Vector3f[]> vector3fList = RenderShapes.SPHERE.getVertices();

        for (int i = 0; i < vector3fList.size(); i++) {
            Vector3f[] vector3fs = vector3fList.get(i);

            MathHelper.scaleVertices(vector3fs, radius, radius, radius);
            MathHelper.translateVertices(vector3fs, x, y, z);
            RenderingHelper.renderDynamicGeometry(poseStack, matrix4f, camera, VertexFormat.Mode.TRIANGLES, vector3fs);
        }
    }
}
