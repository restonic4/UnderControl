package com.restonic4.under_control.events.types;

import com.restonic4.under_control.events.Event;
import com.restonic4.under_control.events.EventFactory;
import com.restonic4.under_control.events.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class BlockEvents {
    public static final Event<DropsItems> DROPS_ITEMS = EventFactory.createArray(DropsItems.class, callbacks -> (level, itemEntitySupplier, itemStack) -> {
        for (DropsItems callback : callbacks) {
            EventResult result = callback.onDropItems(level, itemEntitySupplier, itemStack);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface DropsItems {
        EventResult onDropItems(Level level, Supplier<ItemEntity> itemEntitySupplier, ItemStack itemStack);
    }

    public static final Event<DropsExperience> DROPS_EXPERIENCE = EventFactory.createArray(DropsExperience.class, callbacks -> (serverLevel, blockPos, size) -> {
        for (DropsExperience callback : callbacks) {
            EventResult result = callback.onDropExperience(serverLevel, blockPos, size);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface DropsExperience {
        EventResult onDropExperience(ServerLevel serverLevel, BlockPos blockPos, int size);
    }
}
