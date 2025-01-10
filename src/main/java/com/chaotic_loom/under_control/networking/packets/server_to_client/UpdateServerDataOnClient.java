package com.chaotic_loom.under_control.networking.packets.server_to_client;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.config.ConfigAPI;
import com.chaotic_loom.under_control.client.ClientCacheData;
import com.chaotic_loom.under_control.config.ConfigProvider;
import com.chaotic_loom.under_control.core.annotations.Packet;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@Packet(direction = PacketDirection.SERVER_TO_CLIENT)
public class UpdateServerDataOnClient {
    public static ResourceLocation getId() {
        return new ResourceLocation(UnderControl.MOD_ID, "update_server_data_on_client");
    }

    public static void receive(Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        ClientCacheData.isRenderCommandAllowedOnServer = friendlyByteBuf.readBoolean();
    }

    public static void sendToClient(ServerPlayer serverPlayer) {
        FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();

        ConfigProvider serverConfig = ConfigAPI.getServerProvider(UnderControl.MOD_ID);

        friendlyByteBuf.writeBoolean(serverConfig.get("allow_render_command", Boolean.class));

        ServerPlayNetworking.send(serverPlayer, getId(), friendlyByteBuf);
    }
}
