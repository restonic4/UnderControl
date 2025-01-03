package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

import static com.chaotic_loom.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

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
        Predicate<Entity> predicate = original.call();

        return entity2 -> {
            EventResult result = LivingEntityExtraEvents.PUSHABLE.invoker().onPushable(entity, entity2, predicate);

            if (result == EventResult.CANCELED) {
                return false;
            } else if(result == EventResult.SUCCEEDED) {
                return true;
            }

            return original.call().test(entity2);
        };
    }
}
