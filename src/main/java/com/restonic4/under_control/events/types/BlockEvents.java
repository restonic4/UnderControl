package com.restonic4.under_control.events.types;

import com.restonic4.under_control.events.Event;
import com.restonic4.under_control.events.EventFactory;
import com.restonic4.under_control.events.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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

    public static final Event<FarmTrample> FARM_TRAMPLE = EventFactory.createArray(FarmTrample.class, callbacks -> (level, blockState, blockPos, entity, fallHeight) -> {
        for (FarmTrample callback : callbacks) {
            EventResult result = callback.onFarmTrample(level, blockState, blockPos, entity, fallHeight);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface FarmTrample {
        EventResult onFarmTrample(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallHeight);
    }

    public static final Event<TurtleEggTrample> TURTLE_EGG_TRAMPLE = EventFactory.createArray(TurtleEggTrample.class, callbacks -> (level, blockState, blockPos, entity, fallHeight) -> {
        for (TurtleEggTrample callback : callbacks) {
            EventResult result = callback.onTurtleEggTrample(level, blockState, blockPos, entity, fallHeight);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface TurtleEggTrample {
        EventResult onTurtleEggTrample(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallHeight);
    }

    public static final Event<PressurePlatePressed> PRESSURE_PLATE_PRESSED = EventFactory.createArray(PressurePlatePressed.class, callbacks -> (level, blockState, blockPos, entity) -> {
        for (PressurePlatePressed callback : callbacks) {
            EventResult result = callback.onPressurePlatePressed(level, blockState, blockPos, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface PressurePlatePressed {
        EventResult onPressurePlatePressed(Level level, BlockState blockState, BlockPos blockPos, Entity entity);
    }

    public static final Event<DripleafPressed> DRIPLEAF_PRESSED = EventFactory.createArray(DripleafPressed.class, callbacks -> (level, blockState, blockPos, entity) -> {
        for (DripleafPressed callback : callbacks) {
            EventResult result = callback.onDripleafPressed(level, blockState, blockPos, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface DripleafPressed {
        EventResult onDripleafPressed(Level level, BlockState blockState, BlockPos blockPos, Entity entity);
    }

    public static final Event<TripWirePressed> TRIP_WIRE_PRESSED = EventFactory.createArray(TripWirePressed.class, callbacks -> (level, blockState, blockPos, entity) -> {
        for (TripWirePressed callback : callbacks) {
            EventResult result = callback.onTripWirePressed(level, blockState, blockPos, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface TripWirePressed {
        EventResult onTripWirePressed(Level level, BlockState blockState, BlockPos blockPos, Entity entity);
    }

    public static final Event<RedStoneOrePressed> RED_STONE_ORE_PRESSED = EventFactory.createArray(RedStoneOrePressed.class, callbacks -> (level, blockState, blockPos, entity) -> {
        for (RedStoneOrePressed callback : callbacks) {
            EventResult result = callback.onRedStoneOrePressed(level, blockState, blockPos, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface RedStoneOrePressed {
        EventResult onRedStoneOrePressed(Level level, BlockState blockState, BlockPos blockPos, Entity entity);
    }

    public static final Event<SculkLikeStepped> SCULK_LIKE_STEPPED = EventFactory.createArray(SculkLikeStepped.class, callbacks -> (level, blockState, blockPos, entity) -> {
        for (SculkLikeStepped callback : callbacks) {
            EventResult result = callback.onSculkLikeStepped(level, blockState, blockPos, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface SculkLikeStepped {
        EventResult onSculkLikeStepped(Level level, BlockState blockState, BlockPos blockPos, Entity entity);
    }
}
