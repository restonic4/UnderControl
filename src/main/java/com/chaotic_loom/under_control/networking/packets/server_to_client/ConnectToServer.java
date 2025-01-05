package com.chaotic_loom.under_control.networking.packets.server_to_client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.config.ConfigAPI;
import com.chaotic_loom.under_control.client.ClientCacheData;
import com.chaotic_loom.under_control.config.ConfigProvider;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.net.InetSocketAddress;

public class ConnectToServer {
    public static void receive(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        String ip = friendlyByteBuf.readUtf();
        int port = friendlyByteBuf.readInt();

        String fullIp = ip + ":" + port;

        minecraft.execute(() -> {
            if (minecraft.level != null) {
                minecraft.level.disconnect();
            }

            ServerData serverData = new ServerData("", fullIp, false);
            ServerAddress serverAddress = ServerAddress.parseString(fullIp);
            ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), minecraft, serverAddress, serverData, true);
        });
    }

    public static void sendToClient(ServerPlayer serverPlayer, String ip, int port) {
        FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();

        friendlyByteBuf.writeUtf(ip);
        friendlyByteBuf.writeInt(port);

        ServerPlayNetworking.send(serverPlayer, getId(), friendlyByteBuf);
    }

    public static ResourceLocation getId() {
        return new ResourceLocation(UnderControl.MOD_ID, "connect_to_server");
    }
}
