package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

import static com.chaotic_loom.under_control.util.EntitySelectors.CAN_BE_COLLIDED_WITH_AND_NO_VANISH;
import static com.chaotic_loom.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
    @Redirect(
            method = "getEntityCollisions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/EntityGetter;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    default List<Entity> preventEntityCollisions(EntityGetter instance, @Nullable Entity entity, AABB aabb, Predicate<? super Entity> predicate) {
        List<Entity> entities = instance.getEntities(entity, aabb.inflate(1.0E-7), predicate);

        entities.removeIf(currentEntity -> {
            return LivingEntityExtraEvents.ENTITY_COLLISIONS.invoker().onEntityCollisions(entity, currentEntity, aabb, predicate) == EventResult.CANCELED;
        });

        return entities;
    }

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
