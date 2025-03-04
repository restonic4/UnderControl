package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.chaotic_loom.under_control.events.EventResult;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public class OtherEvents {
    public static final Event<PlayersFoundByCommand> PLAYERS_FOUND_BY_COMMAND = EventFactory.createArray(PlayersFoundByCommand.class, callbacks -> (players, css) -> {
        for (PlayersFoundByCommand callback : callbacks) {
            callback.onPlayersFoundByCommand(players, css);
        }
    });

    @FunctionalInterface
    public interface PlayersFoundByCommand {
        void onPlayersFoundByCommand(List<ServerPlayer> players, CommandSourceStack css);
    }

    public static final Event<BroadcastingToAdmins> BROADCASTING_TO_ADMINS = EventFactory.createArray(BroadcastingToAdmins.class, callbacks -> (entity, observer, component) -> {
        for (BroadcastingToAdmins callback : callbacks) {
            EventResult result = callback.onBroadcastingToAdmins(entity, observer, component);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface BroadcastingToAdmins {
        EventResult onBroadcastingToAdmins(Entity entity, ServerPlayer observer, Component component);
    }

    public static final Event<PlayerListUpdate> PLAYER_LIST_INFO_REQUIRED = EventFactory.createArray(PlayerListUpdate.class, callbacks -> (playerList, observer) -> {
        for (PlayerListUpdate callback : callbacks) {
            List<ServerPlayer> list = callback.onPlayerListUpdate(playerList, observer);

            if (list != null) {
                return list;
            }
        }

        return null;
    });

    @FunctionalInterface
    public interface PlayerListUpdate {
        List<ServerPlayer> onPlayerListUpdate(PlayerList playerList, CommandSourceStack observer);
    }

    public static final Event<ServerSentPacket> SERVER_SENT_PACKET = EventFactory.createArray(ServerSentPacket.class, callbacks -> (unknownListener, packet, packetSendListener, server) -> {
        for (ServerSentPacket callback : callbacks) {
            EventResult result = callback.onServerSentPacket(unknownListener, packet, packetSendListener, server);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface ServerSentPacket {
        EventResult onServerSentPacket(Object unknownListener, Packet<?> packet, PacketSendListener packetSendListener, MinecraftServer server);
    }

    public static final Event<Explode> EXPLODE = EventFactory.createArray(Explode.class, callbacks -> (explosion) -> {
        for (Explode callback : callbacks) {
            EventResult result = callback.onExplode(explosion);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface Explode {
        EventResult onExplode(Explosion explosion);
    }

    public static final Event<LootContainerGeneratedLoot> LOOT_CONTAINER_GENERATED_LOOT = EventFactory.createArray(LootContainerGeneratedLoot.class, callbacks -> (player, lootTable, container, isEntityContainer) -> {
        for (LootContainerGeneratedLoot callback : callbacks) {
            callback.onEvent(player, lootTable, container, isEntityContainer);
        }
    });

    @FunctionalInterface
    public interface LootContainerGeneratedLoot {
        void onEvent(Player player, LootTable lootTable, Container container, boolean isEntityContainer);
    }
}
