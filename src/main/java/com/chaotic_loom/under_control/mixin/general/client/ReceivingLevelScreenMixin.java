package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.networking.packets.client_to_server.ServerJoinRequest;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReceivingLevelScreen.class)
public class ReceivingLevelScreenMixin {
    @Inject(method = "onClose", at = @At("HEAD"))
    public void onClose(CallbackInfo ci) {
        ServerJoinRequest.sendToServer();
    }
}
