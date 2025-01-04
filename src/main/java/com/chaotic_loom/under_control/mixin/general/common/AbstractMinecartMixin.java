package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;push(Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private void onGetEntities(Entity pusher, Entity entity, Operation<Void> original) {
        AbstractMinecart minecart = (AbstractMinecart) (Object) this;

        if (LivingEntityExtraEvents.MINECART_PUSHED.invoker().onMinecartPushed(minecart, pusher) == EventResult.CANCELED) {
            return;
        }

        original.call(pusher, entity);
    }
}
