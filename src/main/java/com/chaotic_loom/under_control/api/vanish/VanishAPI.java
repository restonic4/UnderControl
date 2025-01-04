package com.chaotic_loom.under_control.api.vanish;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.saving.SavingAPI;
import com.chaotic_loom.under_control.saving.SavingProvider;
import com.chaotic_loom.under_control.saving.custom.VanishList;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VanishAPI {
    public static final ThreadLocal<Entity> ACTIVE_ENTITY = ThreadLocal.withInitial(() -> null);

    @Deprecated
    public static boolean isVanished(Entity actor) {
        if (actor == null) {
            return false;
        }

        if (actor.level().isClientSide()) {
            return false;
        }

        if (actor instanceof Player player) {
            SavingProvider savingProvider = SavingAPI.getWorldProvider(UnderControl.MOD_ID);

            VanishList vanishList = savingProvider.get("vanish_list", VanishList.class);
            return vanishList != null && vanishList.contains(player);
        }

        return false;
    }

    public static void setVanished(Player actor, boolean state) {
        if (actor == null) {
            return;
        }

        if (state) {
            vanish(actor);
        } else {
            unVanish(actor);
        }
    }

    @Deprecated
    public static void vanish(Player actor) {
        if (actor == null) {
            return;
        }

        if (actor.level().isClientSide()) {
            return;
        }

        ServerPlayer serverActor = (ServerPlayer) actor;

        SavingProvider savingProvider = SavingAPI.getWorldProvider(UnderControl.MOD_ID);

        VanishList vanishList = savingProvider.get("vanish_list", VanishList.class);
        if (vanishList == null) {
            vanishList = new VanishList();
        }

        if (vanishList.contains(serverActor.getGameProfile().getId())) {
            return;
        }

        vanishList.addUUID(serverActor.getGameProfile().getId().toString());
        savingProvider.save("vanish_list", vanishList);

        broadcastToOthers(serverActor, new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverActor.getUUID())));
        serverActor.server.getPlayerList().broadcastSystemMessage(Component.translatable("multiplayer.player.left", serverActor.getDisplayName()).withStyle(ChatFormatting.YELLOW), false);
    }

    @Deprecated
    public static void unVanish(Player actor) {
        if (actor.level().isClientSide()) {
            return;
        }

        ServerPlayer serverActor = (ServerPlayer) actor;

        SavingProvider savingProvider = SavingAPI.getWorldProvider(UnderControl.MOD_ID);

        VanishList vanishList = savingProvider.get("vanish_list", VanishList.class);
        if (vanishList == null) {
            return;
        }

        if (!vanishList.contains(serverActor.getGameProfile().getId())) {
            return;
        }

        vanishList.removeUUID(serverActor.getGameProfile().getId().toString());
        savingProvider.save("vanish_list", vanishList);

        broadcastToOthers(serverActor, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(serverActor)));
        serverActor.server.getPlayerList().broadcastSystemMessage(Component.translatable("multiplayer.player.joined", serverActor.getDisplayName()).withStyle(ChatFormatting.YELLOW), false);
    }

    public static boolean hasPermissionsViewingVanishedActor(ServerPlayer actor, CommandSourceStack observer) {
        return false; // TODO: finish this
    }

    public static boolean canSeePlayer(ServerPlayer actor, ServerPlayer observer) {
        return canSeePlayer(actor, observer.createCommandSourceStack());
    }

    public static boolean canSeePlayer(ServerPlayer actor, CommandSourceStack observer) {
        if (isVanished(actor)) {
            if (isItself(actor, observer)) {
                return true;
            } else {
                return hasPermissionsViewingVanishedActor(actor, observer);
            }
        } else {
            return true;
        }
    }

    private static boolean isItself(ServerPlayer actor, CommandSourceStack observer) {
        return observer != null && isItself(actor, observer.getEntity());
    }

    private static boolean isItself(ServerPlayer actor, Entity observer) {
        return actor.equals(observer);
    }

    public static List<ServerPlayer> getVisiblePlayers(MinecraftServer server) {
        return getVisiblePlayers(server.createCommandSourceStack().withPermission(0));
    }

    public static List<ServerPlayer> getVisiblePlayers(CommandSourceStack observer) {
        MinecraftServer server = observer.getServer();
        List<ServerPlayer> list = new ArrayList<>();

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (VanishAPI.canSeePlayer(player, observer)) {
                list.add(player);
            }
        }

        return list;
    }

    public static void broadcastHiddenMessage(ServerPlayer actor, Component message) {
        MutableComponent component = message.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

        for (ServerPlayer observer : actor.server.getPlayerList().getPlayers()) {
            if (canSeePlayer(actor, observer)) {
                observer.sendSystemMessage(component);
            }
        }
    }

    public static void sendHiddenMessage(ServerPlayer actor, ServerPlayer observer, Component message) {
        MutableComponent component = message.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

        if (canSeePlayer(actor, observer)) {
            observer.sendSystemMessage(component);
        }
    }

    private static void broadcastToOthers(ServerPlayer actor, Packet<?> packet) {
        for (ServerPlayer observer : actor.server.getPlayerList().getPlayers()) {
            if (!VanishAPI.hasPermissionsViewingVanishedActor(actor, observer.createCommandSourceStack()) && !observer.equals(actor)) {
                observer.connection.send(packet);
            }
        }
    }
}
