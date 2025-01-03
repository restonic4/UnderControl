package com.chaotic_loom.under_control.api.incompatibilities;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.incompatibilities.IncompatibilitiesUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ServerIncompatibilitiesAPI {
    public static void executeServerIncompatibilitiesCheckForClient(ServerPlayer serverPlayer, List<String> mods, String serverModPackRequiredVersion) {
        List<String> incompatibleMods = IncompatibilitiesUtil.getIncompatibleModsOnUse(mods);

        if (!IncompatibilitiesAPI.getServerModPackRequiredVersion().isEmpty() && !IncompatibilitiesAPI.getServerModPackRequiredVersion().isBlank() && !IncompatibilitiesAPI.getServerModPackRequiredVersion().equals(serverModPackRequiredVersion)) {
            UnderControl.LOGGER.warn("Modpack version mismatch for " + serverPlayer.getName() + "; " + serverModPackRequiredVersion + " is not " + IncompatibilitiesAPI.getServerModPackRequiredVersion());

            serverPlayer.connection.disconnect(
                    Component.translatable("gui.under_control.incompatibility_pack.message.server")
            );

            return;
        }

        if (!incompatibleMods.isEmpty()) {
            UnderControl.LOGGER.warn(incompatibleMods.size() + " mod incompatibilities found for " + serverPlayer.getName());

            serverPlayer.connection.disconnect(
                    Component.translatable("gui.under_control.incompatibility.message.server").append(String.join(", ", incompatibleMods))
            );
        } else {
            UnderControl.LOGGER.info("0 mod incompatibilities found for " + serverPlayer.getName());
        }
    }
}
