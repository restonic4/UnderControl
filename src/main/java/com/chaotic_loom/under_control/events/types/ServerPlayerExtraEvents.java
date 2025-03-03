package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.chaotic_loom.under_control.events.EventResult;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.function.Predicate;

public class ServerPlayerExtraEvents {
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

    public static final Event<CanBeHitByProjectiles> CAN_BE_HIT_BY_PROJECTILES = EventFactory.createArray(CanBeHitByProjectiles.class, callbacks -> (player) -> {
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

    public static final Event<AddedToMap> ADDED_TO_MAP = EventFactory.createArray(AddedToMap.class, callbacks -> (mapItemSavedData, holdingPlayer) -> {
        for (AddedToMap callback : callbacks) {
            EventResult result = callback.onAddedToMap(mapItemSavedData, holdingPlayer);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface AddedToMap {
        EventResult onAddedToMap(MapItemSavedData mapItemSavedData, MapItemSavedData.HoldingPlayer holdingPlayer);
    }

    public static final Event<Msg> MSG = EventFactory.createArray(Msg.class, callbacks -> (commandSourceStack, collection, playerChatMessage) -> {
        for (Msg callback : callbacks) {
            EventResult result = callback.onMsg(commandSourceStack, collection, playerChatMessage);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface Msg {
        EventResult onMsg(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, PlayerChatMessage playerChatMessage);
    }

    public static final Event<TellRawReceived> TELLRAW_RECEIVED = EventFactory.createArray(TellRawReceived.class, callbacks -> (sender, serverPlayer, commandContext, command) -> {
        for (TellRawReceived callback : callbacks) {
            EventResult result = callback.onTellRawReceived(sender, serverPlayer, commandContext, command);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface TellRawReceived {
        EventResult onTellRawReceived(ServerPlayer sender, ServerPlayer serverPlayer, CommandContext<CommandSourceStack> commandContext, Command<CommandSourceStack> command);
    }

    public static final Event<AdvancementMessage> ADVANCEMENT_MESSAGE = EventFactory.createArray(AdvancementMessage.class, callbacks -> (player, playerList, component, bl) -> {
        for (AdvancementMessage callback : callbacks) {
            EventResult result = callback.onAdvancementMessage(player, playerList, component, bl);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface AdvancementMessage {
        EventResult onAdvancementMessage(ServerPlayer player, PlayerList playerList, Component component, boolean bl);
    }

    public static final Event<StartedJoining> STARTED_JOINING = EventFactory.createArray(StartedJoining.class, callbacks -> (connection, actor) -> {
        for (StartedJoining callback : callbacks) {
            callback.onStartedJoining(connection, actor);
        }
    });

    @FunctionalInterface
    public interface StartedJoining {
        void onStartedJoining(Connection connection, ServerPlayer actor);
    }

    public static final Event<JoinMessage> JOIN_MESSAGE = EventFactory.createArray(JoinMessage.class, callbacks -> (playerList, component, bl, connection, actor) -> {
        for (JoinMessage callback : callbacks) {
            EventResult result = callback.onJoinMessage(playerList, component, bl, connection, actor);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface JoinMessage {
        EventResult onJoinMessage(PlayerList playerList, Component component, boolean bl, Connection connection, ServerPlayer actor);
    }

    public static final Event<LeaveMessage> LEAVE_MESSAGE = EventFactory.createArray(LeaveMessage.class, callbacks -> (playerList, component, bl, actor) -> {
        for (LeaveMessage callback : callbacks) {
            EventResult result = callback.onLeaveMessage(playerList, component, bl, actor);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface LeaveMessage {
        EventResult onLeaveMessage(PlayerList playerList, Component component, boolean bl, ServerPlayer actor);
    }

    public static final Event<DeathMessage> DEATH_MESSAGE = EventFactory.createArray(DeathMessage.class, callbacks -> (playerList, player, component, actor) -> {
        for (DeathMessage callback : callbacks) {
            EventResult result = callback.onDeathMessage(playerList, player, component, actor);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface DeathMessage {
        EventResult onDeathMessage(PlayerList playerList, Player player, Component component, ServerPlayer actor);
    }

    public static final Event<GameEvent> GAME_EVENT = EventFactory.createArray(GameEvent.class, callbacks -> (packetListener, packet, player) -> {
        for (GameEvent callback : callbacks) {
            EventResult result = callback.onGameEvent(packetListener, packet, player);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface GameEvent {
        EventResult onGameEvent(ServerGamePacketListenerImpl packetListener, Packet<?> packet, Player player);
    }

    public static final Event<SweepingEdgeAttack> SWEEPING_EDGE_ATTACK = EventFactory.createArray(SweepingEdgeAttack.class, callbacks -> (livingEntity, aabb) -> {
        for (SweepingEdgeAttack callback : callbacks) {
            EventResult result = callback.onSweepingEdgeAttack(livingEntity, aabb);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface SweepingEdgeAttack {
        EventResult onSweepingEdgeAttack(LivingEntity livingEntity, AABB aabb);
    }

    public static final Event<InvulnerabilityTo> INVULNERABILITY_TO = EventFactory.createArray(InvulnerabilityTo.class, callbacks -> (player, original) -> {
        for (InvulnerabilityTo callback : callbacks) {
            EventResult result = callback.onInvulnerabilityTo(player, original);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface InvulnerabilityTo {
        EventResult onInvulnerabilityTo(Player player, boolean original);
    }

    public static final Event<SleepingCount> SLEEPING_COUNT = EventFactory.createArray(SleepingCount.class, callbacks -> (player) -> {
        for (SleepingCount callback : callbacks) {
            EventResult result = callback.onSleepingCount(player);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface SleepingCount {
        EventResult onSleepingCount(ServerPlayer player);
    }

    public static final Event<GotNearestPlayer> GOT_NEAREST_PLAYER = EventFactory.createArray(GotNearestPlayer.class, callbacks -> (player, originalPredicate) -> {
        for (GotNearestPlayer callback : callbacks) {
            EventResult result = callback.onGotNearestPlayer(player, originalPredicate);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface GotNearestPlayer {
        EventResult onGotNearestPlayer(Player player, Predicate<Entity> originalPredicate);
    }

    public static final Event<Jumped> JUMPED = EventFactory.createArray(Jumped.class, callbacks -> (serverPlayer) -> {
        for (Jumped callback : callbacks) {
            callback.onJumped(serverPlayer);
        }
    });

    @FunctionalInterface
    public interface Jumped {
        void onJumped(ServerPlayer serverPlayer);
    }

    public static final Event<JumpKeyPressed> JUMP_KEY_PRESSED = EventFactory.createArray(JumpKeyPressed.class, callbacks -> (serverPlayer) -> {
        for (JumpKeyPressed callback : callbacks) {
            callback.onJumped(serverPlayer);
        }
    });

    @FunctionalInterface
    public interface JumpKeyPressed {
        void onJumped(ServerPlayer serverPlayer);
    }

    public static final Event<AdvancementGranted> ADVANCEMENT_GRANTED = EventFactory.createArray(AdvancementGranted.class, callbacks -> (player, advancement, criterionName) -> {
        for (AdvancementGranted callback : callbacks) {
            EventResult result = callback.onEvent(player, advancement, criterionName);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface AdvancementGranted {
        EventResult onEvent(ServerPlayer player, Advancement advancement, String criterionName);
    }
}
