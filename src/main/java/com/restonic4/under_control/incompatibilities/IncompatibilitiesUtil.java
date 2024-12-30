package com.restonic4.under_control.incompatibilities;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.config.ConfigAPI;
import com.restonic4.under_control.api.incompatibilities.IncompatibilitiesAPI;
import com.restonic4.under_control.incompatibilities.IncompatibilityData;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class IncompatibilitiesUtil {
    public static void getIncompatibleModsOnUse(List<String> mods, List<String> result) {
        for (IncompatibilityData incompatibilityData : IncompatibilitiesAPI.getIncompatibilityDataList()) {
            for (String modID : incompatibilityData.getIncompatibleMods()) {
                if (FabricLoader.getInstance().isModLoaded(modID)) {
                    result.add(modID);

                    if (!mods.contains(incompatibilityData.getModID())) {
                        mods.add(incompatibilityData.getModID());
                    }
                }
            }
        }

        IncompatibilitiesList incompatibilitiesListFromServerConfig = ConfigAPI.getServerProvider(UnderControl.MOD_ID).get("mod_incompatibilities", IncompatibilitiesList.class);
        result.addAll(incompatibilitiesListFromServerConfig.getIncompatibleMods());
    }
}
