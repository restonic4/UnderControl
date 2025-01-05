package com.chaotic_loom.under_control.api.server;

import com.chaotic_loom.under_control.networking.packets.server_to_client.ConnectToServer;
import com.chaotic_loom.under_control.networking.services.ApiClient;
import com.chaotic_loom.under_control.networking.services.ApiResponse;
import com.chaotic_loom.under_control.util.ServerData;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class ServerAPI {
    private static ApiClient apiClient = new ApiClient("https://api.mcstatus.io/v2/status/java/");

    public static void transferPlayer(ServerPlayer player, String ip, int port) {
        ConnectToServer.sendToClient(player, ip, port);
    }

    public static ServerData getServerData(String ip) {
        ApiResponse apiResponse = apiClient.get(ip);
        if (apiResponse.isSuccess()) {
            String body = apiResponse.getBody();
            Map<String, Object> response = ApiClient.jsonToMap(body);

            ServerData serverData = new ServerData();

            try {
                serverData.setOnline((Boolean) response.get("online"));
                serverData.setHost((String) response.get("host"));
                serverData.setPort(((Double) response.get("port")).intValue());
                serverData.setIpAddress((String) response.get("ip_address"));
                serverData.setEulaBlocked((Boolean) response.get("eula_blocked"));
                serverData.setRetrievedAt(((Double) response.get("retrieved_at")).intValue());
                serverData.setExpiresAt(((Double) response.get("expires_at")).intValue());
                serverData.setSrvRecord(response.get("srv_record"));
                serverData.setIcon((String) response.get("icon"));
                serverData.setMods((List<String>) response.get("mods"));
                serverData.setSoftware((String) response.get("software"));
                serverData.setPlugins((List<String>) response.get("plugins"));

                LinkedTreeMap<?, ?> versionMap = (LinkedTreeMap<?, ?>) response.get("version");
                ServerData.Version version = new ServerData.Version();

                version.setName_raw((String) versionMap.get("name_raw"));
                version.setName_clean((String) versionMap.get("name_clean"));
                version.setName_html((String) versionMap.get("name_html"));
                version.setProtocol(((Double) versionMap.get("protocol")).intValue());

                serverData.setVersion(version);

                LinkedTreeMap<?, ?> playersMap = (LinkedTreeMap<?, ?>) response.get("players");
                ServerData.Players players = new ServerData.Players();

                players.setOnline(((Double) playersMap.get("online")).intValue());
                players.setMax(((Double) playersMap.get("max")).intValue());
                players.setList((List<String>) playersMap.get("list"));

                serverData.setPlayers(players);

                LinkedTreeMap<?, ?> motdMap = (LinkedTreeMap<?, ?>) response.get("motd");
                ServerData.Motd motd = new ServerData.Motd();

                motd.setRaw((String) motdMap.get("raw"));
                motd.setClean((String) motdMap.get("clean"));
                motd.setHtml((String) motdMap.get("html"));

                serverData.setMotd(motd);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return serverData;
        }

        return null;
    }
}
