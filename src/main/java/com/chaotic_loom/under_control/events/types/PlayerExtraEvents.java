package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.chaotic_loom.under_control.events.EventResult;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class PlayerExtraEvents {
    public static final Event<Damaged> DAMAGED = EventFactory.createArray(Damaged.class, callbacks -> (player, damageSource, amount) -> {
        for (Damaged callback : callbacks) {
           callback.onEvent(player, damageSource, amount);
        }
    });

    @FunctionalInterface
    public interface Damaged {
        void onEvent(Player player, DamageSource damageSource, float amount);
    }

    public static final Event<SleepingStart> SLEEPING_START = EventFactory.createArray(SleepingStart.class, callbacks -> (player, blockPos) -> {
        for (SleepingStart callback : callbacks) {
            callback.onEvent(player, blockPos);
        }
    });

    @FunctionalInterface
    public interface SleepingStart {
        void onEvent(Player player, BlockPos blockPos);
    }

    public static final Event<SleepingStopped> SLEEPING_STOPPED = EventFactory.createArray(SleepingStopped.class, callbacks -> (player, bl, bl2) -> {
        for (SleepingStopped callback : callbacks) {
            callback.onEvent(player, bl, bl2);
        }
    });

    @FunctionalInterface
    public interface SleepingStopped {
        void onEvent(Player player, boolean bl, boolean bl2);
    }
}
