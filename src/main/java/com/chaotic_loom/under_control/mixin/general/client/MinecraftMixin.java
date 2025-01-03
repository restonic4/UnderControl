package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ClientEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public class MinecraftMixin {
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
}
