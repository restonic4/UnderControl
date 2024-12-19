package com.restonic4.under_control.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.restonic4.under_control.client.rendering.RenderingHelper;
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

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    Vector3f pos = new Vector3f(0, 100, 0);
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void renderManagers(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        RenderingHelper.renderSphere(poseStack, matrix4f, camera, pos, 20);
    }
}
