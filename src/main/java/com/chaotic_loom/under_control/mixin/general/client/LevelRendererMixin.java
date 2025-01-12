package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.client.rendering.effects.CubeManager;
import com.chaotic_loom.under_control.client.rendering.effects.CylinderManager;
import com.chaotic_loom.under_control.client.rendering.effects.SphereManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.chaotic_loom.under_control.client.ClientCacheData;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void renderLevel(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        if (ClientCacheData.shouldRenderWireframe) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
    }

    Vector3f pos = new Vector3f(0, 100, 0);
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void renderManagers(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        SphereManager.render(poseStack, matrix4f, camera);
        CubeManager.render(poseStack, matrix4f, camera);
        CylinderManager.render(poseStack, matrix4f, camera);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
}
