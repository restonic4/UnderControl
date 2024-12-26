package com.restonic4.under_control.mixin.vanish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.restonic4.under_control.api.vanish.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

public class EntitySelectors {
    public static final Predicate<Entity> NO_SPECTATORS_AND_NO_VANISH = EntitySelector.NO_SPECTATORS.and(entity -> !VanishAPI.isVanished(entity));
    public static final Predicate<Entity> CAN_BE_COLLIDED_WITH_AND_NO_VANISH = NO_SPECTATORS_AND_NO_VANISH.and(Entity::canBeCollidedWith);

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
            return CAN_BE_COLLIDED_WITH_AND_NO_VANISH;
        }

        @WrapOperation(
                method = "getEntityCollisions",
                at = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
                )
        )
        default Predicate<Entity> preventEntityCollisions2(Operation<Predicate<Entity>> original) {
            return NO_SPECTATORS_AND_NO_VANISH;
        }
    }

    @Mixin(EntitySelector.class)
    public abstract static class EntitySelectorMixin {
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

    @Mixin(ArmorItem.class)
    public abstract static class ArmorItemMixin {
        @WrapOperation(
                method = "dispenseArmor",
                at = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/world/entity/EntitySelector;NO_SPECTATORS:Ljava/util/function/Predicate;"
                )
        )
        private static Predicate<Entity> preventArmorEquip(Operation<Predicate<Entity>> original) {
            return NO_SPECTATORS_AND_NO_VANISH;
        }
    }

    @Mixin(AbstractMinecart.class)
    public abstract static class AbstractMinecartMixin {
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

    @Mixin(Player.class)
    public abstract static class PlayerMixin {
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

    @Mixin(BeehiveBlock.class)
    public abstract static class BeehiveBlockMixin {
        @Redirect(
                method = "angerNearbyBees",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
                )
        )
        private List<LivingEntity> preventAngerNearbyBees(Level instance, Class<LivingEntity> aClass, AABB aabb) {
            return instance.getEntitiesOfClass(aClass, aabb, NO_SPECTATORS_AND_NO_VANISH);
        }
    }
}
