package com.restonic4.under_control.mixin.vanish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

import static com.restonic4.under_control.util.EntitySelectors.CAN_BE_COLLIDED_WITH_AND_NO_VANISH;
import static com.restonic4.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
    @WrapOperation(
            method = "getEntityCollisions",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/EntitySelector;CAN_BE_COLLIDED_WITH:Ljava/util/function/Predicate;"
            )
    )
    default Predicate<Entity> preventEntityCollisions1(Operation<Predicate<Entity>> original) {
        return NO_SPECTATORS_AND_NO_VANISH;
    }

    @WrapOperation(
            method = "getEntityCollisions",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
            )
    )
    default Predicate<Entity> preventEntityCollisions2(Operation<Predicate<Entity>> original) {
        return CAN_BE_COLLIDED_WITH_AND_NO_VANISH;
    }
}
