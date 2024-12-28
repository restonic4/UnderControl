package com.restonic4.under_control.api.incompatibilities;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.incompatibilities.IncompatibilitiesUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class ServerIncompatibilitiesAPI {
    public static void executeServerIncompatibilitiesCheckForClient(ServerPlayer serverPlayer, List<String> mods) {
        List<String> incompatibleMods = new ArrayList<>();

        IncompatibilitiesUtil.getIncompatibleModsOnUse(mods, incompatibleMods);

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
