package com.chaotic_loom.under_control.api.server;

import com.chaotic_loom.under_control.networking.packets.server_to_client.ConnectToServer;
import com.chaotic_loom.under_control.networking.services.ApiClient;
import com.chaotic_loom.under_control.networking.services.ApiResponse;
import com.chaotic_loom.under_control.util.data_holders.ServerInfo;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;

public class ServerAPI {
    private static ApiClient apiClient = new ApiClient("https://api.mcstatus.io/v2/status/java/");

    public static void transferPlayer(ServerPlayer player, String ip, int port) {
        ConnectToServer.sendToClient(player, ip, port);
    }

    public static ServerInfo getServerData(String ip) {
        ApiResponse apiResponse = apiClient.get(ip);
        if (apiResponse.isSuccess()) {
            String body = apiResponse.getBody();
            Map<String, Object> response = ApiClient.jsonToMap(body);

            ServerInfo serverInfo = new ServerInfo();

            try {
                serverInfo.setOnline((Boolean) response.get("online"));
                serverInfo.setHost((String) response.get("host"));
                serverInfo.setPort(((Double) response.get("port")).intValue());
                serverInfo.setIpAddress((String) response.get("ip_address"));
                serverInfo.setEulaBlocked((Boolean) response.get("eula_blocked"));
                serverInfo.setRetrievedAt(((Double) response.get("retrieved_at")).intValue());
                serverInfo.setExpiresAt(((Double) response.get("expires_at")).intValue());
                serverInfo.setSrvRecord(response.get("srv_record"));
                serverInfo.setIcon((String) response.get("icon"));
                serverInfo.setMods((List<String>) response.get("mods"));
                serverInfo.setSoftware((String) response.get("software"));
                serverInfo.setPlugins((List<String>) response.get("plugins"));

                LinkedTreeMap<?, ?> versionMap = (LinkedTreeMap<?, ?>) response.get("version");
                ServerInfo.Version version = new ServerInfo.Version();

                if (versionMap != null) {
                    version.setName_raw((String) versionMap.get("name_raw"));
                    version.setName_clean((String) versionMap.get("name_clean"));
                    version.setName_html((String) versionMap.get("name_html"));
                    version.setProtocol(((Double) versionMap.get("protocol")).intValue());
                }

                serverInfo.setVersion(version);

                LinkedTreeMap<?, ?> playersMap = (LinkedTreeMap<?, ?>) response.get("players");
                ServerInfo.Players players = new ServerInfo.Players();

                if (playersMap != null) {
                    players.setOnline(((Double) playersMap.get("online")).intValue());
                    players.setMax(((Double) playersMap.get("max")).intValue());
                    players.setList((List<String>) playersMap.get("list"));
                }

                serverInfo.setPlayers(players);

                LinkedTreeMap<?, ?> motdMap = (LinkedTreeMap<?, ?>) response.get("motd");
                ServerInfo.Motd motd = new ServerInfo.Motd();

                if (motdMap != null) {
                    motd.setRaw((String) motdMap.get("raw"));
                    motd.setClean((String) motdMap.get("clean"));
                    motd.setHtml((String) motdMap.get("html"));
                }

                serverInfo.setMotd(motd);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return serverInfo;
        }

        return null;
    }
}
