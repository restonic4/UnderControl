package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.BlockEvents;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.chaotic_loom.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {
    @Redirect(
            method = "angerNearbyBees",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<LivingEntity> preventAngerNearbyBees(Level instance, Class<LivingEntity> aClass, AABB aabb) {
        List<LivingEntity> entities = instance.getEntitiesOfClass(aClass, aabb);

        entities.removeIf(livingEntity -> {
            return LivingEntityExtraEvents.BEE_ANGERED.invoker().onBeeAngered(livingEntity) == EventResult.CANCELED;
        });

        return entities;
    }
}
