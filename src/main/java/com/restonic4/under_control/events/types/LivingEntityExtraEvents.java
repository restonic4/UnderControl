package com.restonic4.under_control.events.types;

import com.restonic4.under_control.events.Event;
import com.restonic4.under_control.events.EventFactory;
import com.restonic4.under_control.events.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LivingEntityExtraEvents {
    public static final Event<Pushing> PUSHING = EventFactory.createArray(Pushing.class, callbacks -> (livingEntity) -> {
        for (Pushing callback : callbacks) {
            EventResult result = callback.onPushing(livingEntity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface Pushing {
        EventResult onPushing(LivingEntity livingEntity);
    }
}
