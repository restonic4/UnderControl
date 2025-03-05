package com.chaotic_loom.under_control.client.rendering;

import com.chaotic_loom.under_control.client.rendering.shader.ShaderHolder;
import com.chaotic_loom.under_control.util.FlagFactory;
import com.chaotic_loom.under_control.util.data_holders.RenderingFlags;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.util.MathHelper;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(value = EnvType.CLIENT)
public class RenderingHelper {
    public static void renderDynamicGeometry(PoseStack poseStack, Matrix4f matrix4f, Camera camera, VertexFormat.Mode mode, Vector3f[] vertices, ShaderHolder shaderHolder) {
        renderDynamicGeometry(poseStack, matrix4f, camera, mode, vertices, shaderHolder, 0);
    }

    public static void renderDynamicGeometry(PoseStack poseStack, Matrix4f matrix4f, Camera camera, VertexFormat.Mode mode, Vector3f[] vertices, ShaderHolder shaderHolder, int flags) {
        BufferBuilder.RenderedBuffer renderedBuffer = buildGeometryReusable(mode, vertices);
        renderQuad(generateReusableBuffer(renderedBuffer), poseStack, matrix4f, camera, shaderHolder, flags);
    }

    public static void renderQuad(VertexBuffer vertexBuffer, PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder) {
        renderQuad(vertexBuffer, poseStack, matrix4f, camera, shaderHolder, 0);
    }

    public static void renderQuad(VertexBuffer vertexBuffer, PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, int flags) {
        poseStack.pushPose();
        poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        if (FlagFactory.hasFlag(flags, RenderingFlags.ON_TOP)) {
            RenderSystem.disableDepthTest();
        }

        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), matrix4f, shaderHolder.getInstance().get());

        if (FlagFactory.hasFlag(flags, RenderingFlags.ON_TOP)) {
            RenderSystem.enableDepthTest();
        }

        poseStack.popPose();
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
            RenderingHelper.renderDynamicGeometry(poseStack, matrix4f, camera, VertexFormat.Mode.TRIANGLE_FAN, vector3fs, UnderControlShaders.SIMPLE_COLOR);
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
        renderQuad(generateBuffer(renderedBuffer), poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR);
    }

    public static void renderGeometry(List<Vector3f[]> vector3fList, VertexFormat.Mode vertexFormatMode, PoseStack poseStack, Matrix4f matrix4f, Camera camera, float x, float y, float z, float xScale, float yScale, float zScale, float xRotation, float yRotation, float zRotation) {
        renderGeometry(vector3fList, vertexFormatMode, UnderControlShaders.SIMPLE_COLOR, poseStack, matrix4f, camera, x, y, z, xScale, yScale, zScale, xRotation, yRotation, zRotation, 0);
    }

    public static void renderGeometry(List<Vector3f[]> vector3fList, VertexFormat.Mode vertexFormatMode, ShaderHolder shaderHolder, PoseStack poseStack, Matrix4f matrix4f, Camera camera, float x, float y, float z, float xScale, float yScale, float zScale, float xRotation, float yRotation, float zRotation, int flags) {
        for (int i = 0; i < vector3fList.size(); i++) {
            Vector3f[] vector3fs = vector3fList.get(i);

            if (FlagFactory.hasFlag(flags, RenderingFlags.INVERT_NORMALS)) {
                MathHelper.invertNormals(vector3fs);
            }

            MathHelper.transformGeometry(vector3fs, x, y, z, xScale, yScale, zScale, xRotation, yRotation, zRotation);
            RenderingHelper.renderDynamicGeometry(poseStack, matrix4f, camera, vertexFormatMode, vector3fs, shaderHolder, flags);
        }
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 position, float radius) {
        renderSphere(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, (float) position.x, (float) position.y, (float) position.z, radius, radius, radius, 0, 0, 0, 0);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 position, Vec3 scale, Vec3 rotation) {
        renderSphere(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, (float) position.x, (float) position.y, (float) position.z, (float) scale.x(), (float) scale.y(), (float) scale.z(), (float) rotation.x(), (float) rotation.y(), (float) scale.z(), 0);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, float radius) {
        renderSphere(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, radius, radius, radius, 0, 0, 0, 0);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, float radius, int flags) {
        renderSphere(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, radius, radius, radius, 0, 0, 0, flags);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, Vector3f position, float radius, int flags) {
        renderSphere(poseStack, matrix4f, camera, shaderHolder, position.x, position.y, position.z, radius, radius, radius, 0, 0, 0, flags);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, Vector3f position, Vector3f scale, int flags) {
        renderSphere(poseStack, matrix4f, camera, shaderHolder, position.x, position.y, position.z, scale.x, scale.y, scale.z, 0, 0, 0, flags);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, Vector3f scale, Vector3f rotation) {
        renderSphere(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, 0);
    }

    public static void renderSphere(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, float x, float y, float z, float xScale, float yScale, float zScale, float xRotation, float yRotation, float zRotation, int flags) {
        renderGeometry(
                RenderShapes.SPHERE.getVertices(),
                VertexFormat.Mode.TRIANGLES,
                shaderHolder,
                poseStack,
                matrix4f,
                camera,
                x, y, z,
                xScale, yScale, zScale,
                xRotation, yRotation, zRotation,
                flags
        );
    }

    public static void renderCube(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 position, Vec3 scale, Vec3 rotation) {
        renderCube(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, (float) position.x, (float) position.y, (float) position.z, (float) scale.x, (float) scale.y, (float) scale.z, (float) rotation.x, (float) rotation.y, (float) rotation.z, 0);
    }

    public static void renderCube(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, Vector3f scale, Vector3f rotation) {
        renderCube(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, 0);
    }

    public static void renderCube(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, Vector3f scale, Vector3f rotation, int flags) {
        renderCube(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, flags);
    }

    public static void renderCube(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, Vector3f position, Vector3f scale, Vector3f rotation, int flags) {
        renderCube(poseStack, matrix4f, camera, shaderHolder, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, flags);
    }

    public static void renderCube(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, float x, float y, float z, float xScale, float yScale, float zScale, float xRotation, float yRotation, float zRotation, int flags) {
        renderGeometry(
                RenderShapes.CUBE.getVertices(),
                VertexFormat.Mode.TRIANGLES,
                shaderHolder,
                poseStack,
                matrix4f,
                camera,
                x, y, z,
                xScale, yScale, zScale,
                xRotation, yRotation, zRotation,
                flags
        );
    }

    public static void renderCylinder(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vec3 position, Vec3 scale, Vec3 rotation) {
        renderCylinder(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, (float) position.x, (float) position.y, (float) position.z, (float) scale.x, (float) scale.y, (float) scale.z, (float) rotation.x, (float) rotation.y, (float) rotation.z, 0);
    }

    public static void renderCylinder(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, Vector3f scale, Vector3f rotation) {
        renderCylinder(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, 0);
    }

    public static void renderCylinder(PoseStack poseStack, Matrix4f matrix4f, Camera camera, Vector3f position, Vector3f scale, Vector3f rotation, int flags) {
        renderCylinder(poseStack, matrix4f, camera, UnderControlShaders.SIMPLE_COLOR, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, flags);
    }

    public static void renderCylinder(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, Vector3f position, Vector3f scale, Vector3f rotation, int flags) {
        renderCylinder(poseStack, matrix4f, camera, shaderHolder, position.x, position.y, position.z, scale.x, scale.y, scale.z, rotation.x, rotation.y, rotation.z, flags);
    }

    public static void renderCylinder(PoseStack poseStack, Matrix4f matrix4f, Camera camera, ShaderHolder shaderHolder, float x, float y, float z, float xScale, float yScale, float zScale, float xRotation, float yRotation, float zRotation, int flags) {
        renderGeometry(
                RenderShapes.CYLINDER.getVertices(),
                VertexFormat.Mode.TRIANGLES,
                shaderHolder,
                poseStack,
                matrix4f,
                camera,
                x, y, z,
                xScale, yScale, zScale,
                xRotation, yRotation, zRotation,
                flags
        );
    }

    public static abstract class GUI {
        public static void blit(GuiGraphics guiGraphics, ResourceLocation resourceLocation, int x, int y, int u, int v, int width, int height, int totalTextureWidth, int totalTextureHeight) {
            guiGraphics.blit(resourceLocation, x, y, 0, (float)u, (float)v, width, height, totalTextureWidth, totalTextureHeight);
        }

        public static void blitNineSlicedTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int outerSliceWidth, int outerSliceHeight, int centerSliceWidth, int centerSliceHeight, int u, int v, int totalTextureWidth, int totalTextureHeight) {
            blitNineSlicedTexture(guiGraphics, texture, x, y, width, height, outerSliceWidth, outerSliceHeight, outerSliceWidth, outerSliceHeight, centerSliceWidth, centerSliceHeight, u, v, totalTextureWidth, totalTextureHeight);
        }

        public static void blitNineSlicedTexture(
                GuiGraphics guiGraphics, ResourceLocation texture,
                int x, int y,
                int width, int height,
                int leftSliceWidth,
                int topSliceHeight,
                int rightSliceWidth,
                int bottomSliceHeight,
                int centerSliceWidth, int centerSliceHeight,
                int u, int v,
                int totalTextureWidth, int totalTextureHeight
        ) {
            leftSliceWidth = Math.min(leftSliceWidth, width / 2);
            rightSliceWidth = Math.min(rightSliceWidth, width / 2);
            topSliceHeight = Math.min(topSliceHeight, height / 2);
            bottomSliceHeight = Math.min(bottomSliceHeight, height / 2);

            if (width == centerSliceWidth && height == centerSliceHeight) {
                blit(guiGraphics, texture, x, y, u, v, width, height, totalTextureWidth, totalTextureHeight);
            } else if (height == centerSliceHeight) {
                blit(guiGraphics, texture, x, y, u, v, leftSliceWidth, height, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x + leftSliceWidth, y, width - rightSliceWidth - leftSliceWidth, height, u + leftSliceWidth, v, centerSliceWidth - rightSliceWidth - leftSliceWidth, centerSliceHeight, totalTextureWidth, totalTextureHeight);
                blit(guiGraphics, texture, x + width - rightSliceWidth, y, u + centerSliceWidth - rightSliceWidth, v, rightSliceWidth, height, totalTextureWidth, totalTextureHeight);
            } else if (width == centerSliceWidth) {
                blit(guiGraphics, texture, x, y, u, v, width, topSliceHeight, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x, y + topSliceHeight, width, height - bottomSliceHeight - topSliceHeight, u, v + topSliceHeight, centerSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight, totalTextureWidth, totalTextureHeight);
                blit(guiGraphics, texture, x, y + height - bottomSliceHeight, u, v + centerSliceHeight - bottomSliceHeight, width, bottomSliceHeight, totalTextureWidth, totalTextureHeight);
            } else {
                blit(guiGraphics, texture, x, y, u, v, leftSliceWidth, topSliceHeight, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x + leftSliceWidth, y, width - rightSliceWidth - leftSliceWidth, topSliceHeight, u + leftSliceWidth, v, centerSliceWidth - rightSliceWidth - leftSliceWidth, topSliceHeight, totalTextureWidth, totalTextureHeight);
                blit(guiGraphics, texture, x + width - rightSliceWidth, y, u + centerSliceWidth - rightSliceWidth, v, rightSliceWidth, topSliceHeight, totalTextureWidth, totalTextureHeight);
                blit(guiGraphics, texture, x, y + height - bottomSliceHeight, u, v + centerSliceHeight - bottomSliceHeight, leftSliceWidth, bottomSliceHeight, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x + leftSliceWidth, y + height - bottomSliceHeight, width - rightSliceWidth - leftSliceWidth, bottomSliceHeight, u + leftSliceWidth, v + centerSliceHeight - bottomSliceHeight, centerSliceWidth - rightSliceWidth - leftSliceWidth, bottomSliceHeight, totalTextureWidth, totalTextureHeight);
                blit(guiGraphics, texture, x + width - rightSliceWidth, y + height - bottomSliceHeight, u + centerSliceWidth - rightSliceWidth, v + centerSliceHeight - bottomSliceHeight, rightSliceWidth, bottomSliceHeight, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x, y + topSliceHeight, leftSliceWidth, height - bottomSliceHeight - topSliceHeight, u, v + topSliceHeight, leftSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x + leftSliceWidth, y + topSliceHeight, width - rightSliceWidth - leftSliceWidth, height - bottomSliceHeight - topSliceHeight, u + leftSliceWidth, v + topSliceHeight, centerSliceWidth - rightSliceWidth - leftSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight, totalTextureWidth, totalTextureHeight);
                blitRepeating(guiGraphics, texture, x + width - rightSliceWidth, y + topSliceHeight, leftSliceWidth, height - bottomSliceHeight - topSliceHeight, u + centerSliceWidth - rightSliceWidth, v + topSliceHeight, rightSliceWidth, centerSliceHeight - bottomSliceHeight - topSliceHeight, totalTextureWidth, totalTextureHeight);
            }
        }

        public static void blitRepeating(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int totalTextureWidth, int totalTextureHeight) {
            int newX = x;
            int newWidth;

            for(IntIterator var11 = GuiGraphics.slices(width, textureWidth); var11.hasNext(); newX += newWidth) {
                newWidth = var11.nextInt();

                int uOffset = (textureWidth - newWidth) / 2;
                int newY = y;
                int newHeight;

                for(IntIterator var15 = GuiGraphics.slices(height, textureHeight); var15.hasNext(); newY += newHeight) {
                    newHeight = var15.nextInt();
                    int vOffset = (textureHeight - newHeight) / 2;
                    blit(guiGraphics, texture, newX, newY, u + uOffset, v + vOffset, newWidth, newHeight, totalTextureWidth, totalTextureHeight);
                }
            }
        }
    }
}
