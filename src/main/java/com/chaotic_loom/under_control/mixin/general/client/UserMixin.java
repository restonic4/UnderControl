package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.api.client.ClientAPI;
import com.mojang.authlib.GameProfile;
import net.minecraft.SharedConstants;
import net.minecraft.client.User;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(User.class)
public abstract class UserMixin {
    @Shadow @Nullable public abstract UUID getProfileId();

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    public void getName(CallbackInfoReturnable<String> cir) {
        if (SharedConstants.IS_RUNNING_IN_IDE && !ClientAPI.getUsername().isEmpty()) {
            cir.setReturnValue(ClientAPI.getUsername());
            cir.cancel();
        }
    }

    @Inject(method = "getUuid", at = @At("RETURN"), cancellable = true)
    public void getUuid(CallbackInfoReturnable<String> cir) {
        if (SharedConstants.IS_RUNNING_IN_IDE && !ClientAPI.getUuid().isEmpty()) {
            cir.setReturnValue(ClientAPI.getUuid());
            cir.cancel();
        }
    }

    @Inject(method = "getGameProfile", at = @At("RETURN"), cancellable = true)
    public void getGameProfile(CallbackInfoReturnable<GameProfile> cir) {
        if (SharedConstants.IS_RUNNING_IN_IDE && (!ClientAPI.getUsername().isEmpty() || !ClientAPI.getUuid().isEmpty())) {
            cir.setReturnValue(new GameProfile(this.getProfileId(), ClientAPI.getUsername()));
            cir.cancel();
        }
    }
}
