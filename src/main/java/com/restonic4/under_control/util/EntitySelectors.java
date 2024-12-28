package com.restonic4.under_control.util;

import com.restonic4.under_control.api.vanish.VanishAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;

import java.util.function.Predicate;

public class EntitySelectors {
    public static final Predicate<Entity> NO_SPECTATORS_AND_NO_VANISH = EntitySelector.NO_SPECTATORS.and(entity -> !VanishAPI.isVanished(entity));
    public static final Predicate<Entity> CAN_BE_COLLIDED_WITH_AND_NO_VANISH = NO_SPECTATORS_AND_NO_VANISH.and(Entity::canBeCollidedWith);
}
