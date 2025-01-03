package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;

import java.util.List;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "touch", at = @At("HEAD"), cancellable = true)
    private void touch(Entity entity, CallbackInfo ci) {
        EventResult eventResult = ServerPlayerExtraEvents.TOUCH_ENTITY.invoker().onTouchEntity((Player) (Object) this, entity);
        if (eventResult == EventResult.CANCELED) {
            ci.cancel();
        }
    }

    @Inject(method = "canBeHitByProjectile", at = @At("HEAD"), cancellable = true)
    public void canBeHitByProjectile(CallbackInfoReturnable<Boolean> cir) {
        EventResult eventResult = ServerPlayerExtraEvents.CAN_BE_HIT_BY_PROJECTILES.invoker().onCanBeHitByProjectiles((Player) (Object) this);
        if (eventResult == EventResult.CANCELED) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (eventResult == EventResult.SUCCEEDED) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<LivingEntity> preventSweepingEdge(Level instance, Class<LivingEntity> aClass, AABB aabb) {
        List<LivingEntity> entities = instance.getEntitiesOfClass(aClass, aabb);

        entities.removeIf(livingEntity -> {
            return ServerPlayerExtraEvents.SWEEPING_EDGE_ATTACK.invoker().onSweepingEdgeAttack(livingEntity, aabb) == EventResult.CANCELED;
        });

        return entities;
    }

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean vanish_invulnerablePlayers(boolean original) {
        EventResult eventResult = ServerPlayerExtraEvents.INVULNERABILITY_TO.invoker().onInvulnerabilityTo((Player) (Object) this, original);
        if (eventResult == EventResult.CANCELED) {
            return false;
        } else if (eventResult == EventResult.SUCCEEDED) {
            return true;
        }

        return original;
    }
}
