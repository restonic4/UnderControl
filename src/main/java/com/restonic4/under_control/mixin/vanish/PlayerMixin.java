package com.restonic4.under_control.mixin.vanish;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.restonic4.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<LivingEntity> preventSweepingEdge(Level instance, Class<LivingEntity> aClass, AABB aabb) {
        return instance.getEntitiesOfClass(aClass, aabb, NO_SPECTATORS_AND_NO_VANISH);
    }
}
