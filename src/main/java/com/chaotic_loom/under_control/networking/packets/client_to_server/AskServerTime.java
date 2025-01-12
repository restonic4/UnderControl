package com.chaotic_loom.under_control.networking.packets.client_to_server;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.chaotic_loom.under_control.api.incompatibilities.ServerIncompatibilitiesAPI;
import com.chaotic_loom.under_control.core.annotations.Packet;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.networking.packets.server_to_client.SendCurrentServerTime;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

@Packet(direction = PacketDirection.CLIENT_TO_SERVER)
public class AskServerTime {
    public static ResourceLocation getId() {
        return new ResourceLocation(UnderControl.MOD_ID, "ask_server_time");
    }

    public static void receive(MinecraftServer server, Player player, ServerPacketListener serverPacketListener, FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        long synchronizationID = friendlyByteBuf.readLong();
        long clientTime = friendlyByteBuf.readLong();

        if (player instanceof ServerPlayer serverPlayer) {
            SendCurrentServerTime.sendToClient(serverPlayer, synchronizationID, clientTime);
        }
    }

    public static void sendToServer(long synchronizationID) {
        FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();

        friendlyByteBuf.writeLong(synchronizationID);
        friendlyByteBuf.writeLong(System.currentTimeMillis());

        ClientPlayNetworking.send(getId(), friendlyByteBuf);
    }
}
