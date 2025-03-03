package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow
    @Nullable
    public abstract Player getPlayerOwner();

    @Inject(
            method = "retrieve",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/critereon/FishingRodHookedTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/projectile/FishingHook;Ljava/util/Collection;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void retrieveWithCatch(ItemStack itemStack, CallbackInfoReturnable<Integer> cir, @Local List<ItemStack> items) {
        FishingHook hook = (FishingHook) (Object) this;
        Player player = this.getPlayerOwner();

        EventResult result = LivingEntityExtraEvents.FISHING_HOOK_RETRIEVED.invoker().onEvent(player, hook, items);
        if (result == EventResult.CANCELED) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }

    @Inject(
            method = "retrieve",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/projectile/FishingHook;hookedIn:Lnet/minecraft/world/entity/Entity;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            ),
            cancellable = true
    )
    private void retrieveButEntityOnHook(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        FishingHook hook = (FishingHook) (Object) this;
        Player player = this.getPlayerOwner();

        EventResult result = LivingEntityExtraEvents.FISHING_HOOK_RETRIEVED.invoker().onEvent(player, hook, null);
        if (result == EventResult.CANCELED) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
