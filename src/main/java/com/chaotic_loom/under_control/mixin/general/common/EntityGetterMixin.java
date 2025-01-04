package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
    @WrapOperation(
            method = "isUnobstructed",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;blocksBuilding:Z"
            )
    )
    default boolean shouldObstructBlockPlacement(Entity entity, Operation<Boolean> original) {
        if (LivingEntityExtraEvents.ENTITY_OBSTRUCTION.invoker().onEntityObstruction(entity) == EventResult.CANCELED) {
            return false;
        }

        return original.call(entity);
    }
}
