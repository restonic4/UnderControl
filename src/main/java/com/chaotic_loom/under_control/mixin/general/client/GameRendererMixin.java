package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.api.cutscene.CutsceneAPI;
import com.chaotic_loom.under_control.client.gui.DynamicScreen;
import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.chaotic_loom.under_control.events.types.ShaderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "reloadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private void registerShaders(ResourceProvider resourceProvider, CallbackInfo ci, @Local(ordinal = 1) List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderPairList) throws IOException {
        ShaderEvents.REGISTRATION.invoker().onRegistration(resourceProvider, shaderPairList);
    }

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void bobHurt(PoseStack poseStack, float f, CallbackInfo ci) {
        if (CutsceneAPI.isPlaying()) {
            ci.cancel();
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(PoseStack poseStack, float f, CallbackInfo ci) {
        if (CutsceneAPI.isPlaying()) {
            ci.cancel();
        }
    }

    int i = 0;
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V",
                    ordinal = 1
            )
    )
    private void render(float f, long l, boolean bl, CallbackInfo ci, @Local GuiGraphics guiGraphics) {
        List<DynamicScreen> dynamicScreens = RenderingHelper.GUI.getDynamicScreens();

        int x = (int)(
                this.minecraft.mouseHandler.xpos() * (double)this.minecraft.getWindow().getGuiScaledWidth() / (double)this.minecraft.getWindow().getScreenWidth()
        );
        int y = (int)(
                this.minecraft.mouseHandler.ypos() * (double)this.minecraft.getWindow().getGuiScaledHeight() / (double)this.minecraft.getWindow().getScreenHeight()
        );

        for (i = 0; i < dynamicScreens.size(); i++) {
            dynamicScreens.get(i).preRender(guiGraphics, x, y, this.minecraft.getDeltaFrameTime());
        }
    }
}
