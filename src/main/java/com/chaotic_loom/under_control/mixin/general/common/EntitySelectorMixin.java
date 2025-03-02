package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin {
    /*@Redirect(
            method = "pushableBy",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/Predicate;and(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;"
            )
    )
    private static Predicate<Entity> pushableBy(Predicate<Entity> instance, Predicate<Entity> other, @Local(argsOnly = true) Entity entity) {
        return entity2 -> {
            boolean original = instance.test(entity2) && other.test(entity2);
            EventResult eventResult = LivingEntityExtraEvents.PUSHABLE.invoker().onPushable(entity, entity2);

            if (eventResult == EventResult.CANCELED) {
                return false;
            } else if (eventResult == EventResult.SUCCEEDED) {
                return true;
            }

            return original;
        };
    }*/

    @Inject(
            method = "pushableBy",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void onPushableBy(Entity entity, CallbackInfoReturnable<Predicate<Entity>> cir) {
        Predicate<Entity> original = cir.getReturnValue();

        cir.setReturnValue(entity2 -> {
            // Resultado original incluyendo posibles modificaciones del otro mod
            boolean originalResult = original.test(entity2);

            // Tu lógica personalizada
            EventResult eventResult = LivingEntityExtraEvents.PUSHABLE.invoker().onPushable(entity, entity2);

            if (eventResult == EventResult.CANCELED) return false;
            if (eventResult == EventResult.SUCCEEDED) return true;

            return originalResult;
        });
    }
}
