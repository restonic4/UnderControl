package com.chaotic_loom.under_control.networking.packets.client_to_server;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.core.annotations.Packet;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@Packet(direction = PacketDirection.CLIENT_TO_SERVER)
public class ClientJumpKeyPressed {
    public static ResourceLocation getId() {
        return new ResourceLocation(UnderControl.MOD_ID, "client_jump_key_pressed");
    }

    public static void receive(MinecraftServer server, Player player, ServerPacketListener serverPacketListener, FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayerExtraEvents.JUMP_KEY_PRESSED.invoker().onJumped(serverPlayer);
        }
    }

    public static void sendToServer() {
        FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();
        ClientPlayNetworking.send(getId(), friendlyByteBuf);
    }
}
