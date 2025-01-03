package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.chaotic_loom.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<Entity> onGetEntities(Level level, Entity entity, AABB aabb) {
        List<Entity> entities = level.getEntities(entity, aabb);

        entities.removeIf(pusher -> {
            AbstractMinecart minecart = (AbstractMinecart) (Object) this;
            return LivingEntityExtraEvents.MINECART_PUSHED.invoker().onMinecartPushed(minecart, pusher) == EventResult.CANCELED;
        });

        return entities;
    }
}
