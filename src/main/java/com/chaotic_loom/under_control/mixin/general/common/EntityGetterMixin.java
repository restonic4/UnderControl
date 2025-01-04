package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

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

    @WrapOperation(
            method = "hasNearbyAlivePlayer",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
    )
    private Predicate<Entity> interceptHasNearbyAlivePlayer(Operation<Predicate<Entity>> original) {
        return entity -> {
            if (entity instanceof Player player) {
                if (ServerPlayerExtraEvents.GOT_NEAREST_PLAYER.invoker().onGotNearestPlayer(player, original.call()) == EventResult.CANCELED) {
                    return false;
                }
            }
            return original.call().test(entity);
        };
    }

    @WrapOperation(
            method = "getNearestPlayer(DDDDZ)Lnet/minecraft/world/entity/player/Player;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
    )
    private Predicate<Entity> interceptGetNearestPlayer(Operation<Predicate<Entity>> original) {
        return entity -> {
            if (entity instanceof Player player) {
                if (ServerPlayerExtraEvents.GOT_NEAREST_PLAYER.invoker().onGotNearestPlayer(player, original.call()) == EventResult.CANCELED) {
                    return false;
                }
            }
            return original.call().test(entity);
        };
    }
}
