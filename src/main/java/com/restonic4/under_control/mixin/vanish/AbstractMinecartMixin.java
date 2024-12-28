package com.restonic4.under_control.mixin.vanish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

import static com.restonic4.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<Entity> preventMinecartCollision(Level instance, Entity entity, AABB aABB, Operation<List<Entity>> original) {
        return instance.getEntities(entity, aABB, NO_SPECTATORS_AND_NO_VANISH);
    }
}
