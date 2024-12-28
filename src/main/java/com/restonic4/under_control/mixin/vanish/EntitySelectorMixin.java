package com.restonic4.under_control.mixin.vanish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.restonic4.under_control.api.vanish.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

import static com.restonic4.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {
    @WrapOperation(
            method = "pushableBy",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
    )
    private static Predicate<Entity> preventEntityCollision(Operation<Predicate<Entity>> original, Entity entity) {
        return NO_SPECTATORS_AND_NO_VANISH.and(currentEntity -> !VanishAPI.isVanished(entity));
    }
}
