package com.chaotic_loom.under_control.mixin.vanish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(
            method = "tickNonPassenger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;tick()V"
            )
    )
    public void beforeEntityTickNonPassenger(Entity entity, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(entity);
    }

    @Inject(
            method = "tickNonPassenger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    public void afterEntityTickNonPassenger(Entity entity, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.remove();
    }

    @Inject(
            method = "tickPassenger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;rideTick()V"
            )
    )
    public void beforeEntityTick(Entity entity, Entity entity2, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.set(entity2);
    }

    @Inject(
            method = "tickPassenger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;rideTick()V",
                    shift = At.Shift.AFTER
            )
    )
    public void afterEntityTick(Entity entity, Entity entity2, CallbackInfo ci) {
        VanishAPI.ACTIVE_ENTITY.remove();
    }
}
