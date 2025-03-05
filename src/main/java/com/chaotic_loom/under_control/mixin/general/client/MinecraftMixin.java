package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.client.gui.DynamicScreen;
import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ClientEvents;
import com.chaotic_loom.under_control.events.types.ClientLifeExtraEvents;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final private Window window;

    @Inject(method = "reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"), cancellable = true)
    private void reloadResourcePacks(boolean forceReload, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        EventResult eventResult = ClientEvents.RESOURCE_PACK_RELOAD.invoker().onResourcePackReload(forceReload);
        if (eventResult == EventResult.CANCELED) {
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);

            cir.setReturnValue(completableFuture);
            cir.cancel();
        }
    }

    @Inject(method = "onGameLoadFinished", at = @At("RETURN"))
    private void onStart(CallbackInfo ci) {
        ClientLifeExtraEvents.CLIENT_STARTED_DELAYED.invoker().onClientStartedDelayed((Minecraft) (Object) this);
    }

    @Inject(method = "resizeDisplay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;resize(Lnet/minecraft/client/Minecraft;II)V"))
    private void resizeDisplay(CallbackInfo ci) {
        List<DynamicScreen> dynamicScreens = RenderingHelper.GUI.getDynamicScreens();

        for (int i = 0; i < dynamicScreens.size(); i++) {
            dynamicScreens.get(i).resize((Minecraft) (Object) this, this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight());
        }
    }
}
