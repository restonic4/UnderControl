package com.chaotic_loom.under_control.networking.packets.server_to_client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.networking.packets.client_to_server.ServerJoinRequest;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AskClientForMods {
    public static void receive(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        ServerJoinRequest.sendToServer();
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();
        ServerPlayNetworking.send(serverPlayer, getId(), friendlyByteBuf);
    }

    public static ResourceLocation getId() {
        return new ResourceLocation(UnderControl.MOD_ID, "ask_client_for_mods");
    }
}
