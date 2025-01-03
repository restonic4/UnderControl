package com.chaotic_loom.under_control.incompatibilities;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.config.ConfigAPI;
import com.chaotic_loom.under_control.api.incompatibilities.IncompatibilitiesAPI;

import java.util.ArrayList;
import java.util.List;

public class IncompatibilitiesUtil {
    public static List<String> getIncompatibleModsOnUse(List<String> loadedMods) {
        List<String> result = new ArrayList<>();

        for (IncompatibilityData incompatibilityData : IncompatibilitiesAPI.getIncompatibilityDataList()) {
            for (String incompatibleModID : incompatibilityData.getIncompatibleMods()) {
                for (String loadedModID : loadedMods) {
                    if (loadedModID.equals(incompatibleModID)) {
                        result.add(incompatibleModID);
                    }
                }
            }
        }

        IncompatibilitiesList incompatibilitiesListFromServerConfig = ConfigAPI.getServerProvider(UnderControl.MOD_ID).get("mod_incompatibilities", IncompatibilitiesList.class);
        for (String incompatibleMod : incompatibilitiesListFromServerConfig.getIncompatibleMods()) {
            for (String loadedModID : loadedMods) {
                if (loadedModID.equals(incompatibleMod)) {
                    result.add(incompatibleMod);
                }
            }
        }

        return result;
    }
}
