package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.BlockEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BarrelBlockEntity.class, ChestBlockEntity.class, PlayerEnderChestContainer.class, ShulkerBoxBlockEntity.class})
public class ContainersMixins {
    @Inject(method = "startOpen", at = @At("HEAD"), cancellable = true)
    public void cancelOpenAnimation(Player player, CallbackInfo ci) {
        if (BlockEvents.CONTAINER_ANIMATED.invoker().onContainerAnimated(player, true) == EventResult.CANCELED) {
            ci.cancel();
        }
    }

    @Inject(method = "stopOpen", at = @At("HEAD"), cancellable = true)
    public void cancelCloseAnimation(Player player, CallbackInfo ci) {
        if (BlockEvents.CONTAINER_ANIMATED.invoker().onContainerAnimated(player, false) == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
