package com.chaotic_loom.under_control.networking.packets.client_to_server;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.chaotic_loom.under_control.api.incompatibilities.ServerIncompatibilitiesAPI;
import com.chaotic_loom.under_control.core.annotations.Packet;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Packet(direction = PacketDirection.CLIENT_TO_SERVER)
public class ServerJoinRequest {
    public static ResourceLocation getId() {
        return new ResourceLocation(UnderControl.MOD_ID, "server_join_request");
    }

    public static void receive(MinecraftServer server, Player player, ServerPacketListener serverPacketListener, FriendlyByteBuf friendlyByteBuf, PacketSender packetSender) {
        if (player instanceof ServerPlayer serverPlayer) {
            List<String> mods = getMods(friendlyByteBuf);
            String serverModPackRequiredVersion = friendlyByteBuf.readUtf();

            ServerIncompatibilitiesAPI.executeServerIncompatibilitiesCheckForClient(serverPlayer, mods, serverModPackRequiredVersion);
        }
    }

    private static List<String> getMods(FriendlyByteBuf friendlyByteBuf) {
        String[] modsRaw = friendlyByteBuf.readUtf().split("/");

        List<String> mods = new ArrayList<>(Arrays.asList(modsRaw));

        if (mods.get(mods.size() - 1).isEmpty() || mods.get(mods.size() - 1).isBlank()) {
            mods.remove(mods.size() - 1);
        }

        UnderControl.LOGGER.info("Client joined with: " + String.join(", ", mods));

        return mods;
    }

    public static void sendToServer() {
        FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();

        StringBuilder modsString = new StringBuilder();

        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            modsString.append(modContainer.getMetadata().getId()).append("/");
        }

        friendlyByteBuf.writeUtf(modsString.toString());
        friendlyByteBuf.writeUtf(IncompatibilitiesAPI.getServerModPackRequiredVersion());

        ClientPlayNetworking.send(getId(), friendlyByteBuf);
    }
}
