package com.chaotic_loom.under_control.networking;

import com.chaotic_loom.under_control.networking.packets.client_to_server.ServerJoinRequest;
import com.chaotic_loom.under_control.networking.packets.server_to_client.AskClientForMods;
import com.chaotic_loom.under_control.networking.packets.server_to_client.ConnectToServer;
import com.chaotic_loom.under_control.networking.packets.server_to_client.UpdateServerDataOnClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PacketManager {
    public static void registerServerToClient() {
        ClientPlayNetworking.registerGlobalReceiver(AskClientForMods.getId(), AskClientForMods::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateServerDataOnClient.getId(), UpdateServerDataOnClient::receive);
        ClientPlayNetworking.registerGlobalReceiver(ConnectToServer.getId(), ConnectToServer::receive);
    }

    public static void registerClientToServer() {
        ServerPlayNetworking.registerGlobalReceiver(ServerJoinRequest.getId(), ServerJoinRequest::receive);
    }
}
