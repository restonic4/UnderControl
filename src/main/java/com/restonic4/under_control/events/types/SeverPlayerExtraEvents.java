package com.restonic4.under_control.events.types;

import com.restonic4.under_control.events.Event;
import com.restonic4.under_control.events.EventFactory;
import com.restonic4.under_control.events.EventResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SeverPlayerExtraEvents {
    public static final Event<VillageRaidStarted> VILLAGE_RAID_STARTED = EventFactory.createArray(VillageRaidStarted.class, callbacks -> (player, badOmenLevel) -> {
        for (VillageRaidStarted callback : callbacks) {
            EventResult result = callback.onVillageRaidStarted(player, badOmenLevel);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface VillageRaidStarted {
        EventResult onVillageRaidStarted(ServerPlayer player, int badOmenLevel);
    }

    public static final Event<TouchEntity> TOUCH_ENTITY = EventFactory.createArray(TouchEntity.class, callbacks -> (player, entity) -> {
        for (TouchEntity callback : callbacks) {
            EventResult result = callback.onTouchEntity(player, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface TouchEntity {
        EventResult onTouchEntity(Player player, Entity entity);
    }

    public static final Event<CanBeHitByProjectiles> CAN_BEW_HIT_BY_PROJECTILES = EventFactory.createArray(CanBeHitByProjectiles.class, callbacks -> (player) -> {
        for (CanBeHitByProjectiles callback : callbacks) {
            EventResult result = callback.onCanBeHitByProjectiles(player);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface CanBeHitByProjectiles {
        EventResult onCanBeHitByProjectiles(Player player);
    }
}
