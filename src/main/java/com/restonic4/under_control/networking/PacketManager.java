package com.restonic4.under_control.networking;

import com.restonic4.under_control.networking.packets.client_to_server.ServerJoinRequest;
import com.restonic4.under_control.networking.packets.server_to_client.AskClientForMods;
import com.restonic4.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class PacketManager {
    public static void registerServerToClient() {
        ClientPlayNetworking.registerGlobalReceiver(AskClientForMods.getId(), AskClientForMods::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateServerDataOnClient.getId(), UpdateServerDataOnClient::receive);
    }

    public static void registerClientToServer() {
        ServerPlayNetworking.registerGlobalReceiver(ServerJoinRequest.getId(), ServerJoinRequest::receive);
    }
}
