package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.BlockEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

import static com.chaotic_loom.under_control.util.EntitySelectors.NO_SPECTATORS_AND_NO_VANISH;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {
    @Redirect(
            method = "dispenseArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private static <T extends Entity> List<LivingEntity> getEntitiesOfClass(ServerLevel instance, Class<T> aClass, AABB aabb, Predicate<? super T> predicate, @Local(argsOnly = true) BlockSource blockSource, @Local BlockPos blockPos, @Local(argsOnly = true) ItemStack itemStack) {
        List<LivingEntity> entities = blockSource.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(itemStack)));

        entities.removeIf(livingEntity -> {
            return BlockEvents.ARMOR_DISPENSED_ON_ENTITY.invoker().onArmorDispensed(livingEntity) == EventResult.CANCELED;
        });

        return entities;
    }
}
