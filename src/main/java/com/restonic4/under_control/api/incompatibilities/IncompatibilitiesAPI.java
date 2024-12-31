package com.restonic4.under_control.api.incompatibilities;

import com.restonic4.under_control.incompatibilities.IncompatibilitiesList;
import com.restonic4.under_control.incompatibilities.IncompatibilityData;

import java.util.ArrayList;
import java.util.List;

public class IncompatibilitiesAPI {
    private static List<IncompatibilityData> incompatibilityDataList = new ArrayList<>();
    private static String serverModPackRequiredVersion = "";

    public static void registerIncompatibleMods(String modID, String... incompatibleModID) {
        for (String foundModID : incompatibleModID) {
            registerIncompatibleMod(modID, foundModID);
        }
    }

    public static void registerIncompatibleMod(String modID, String incompatibleModID) {
        IncompatibilityData incompatibilityData = null;

        for (int i = 0; i < incompatibilityDataList.size() && incompatibilityData == null; i++) {
            if (incompatibilityDataList.get(i).getModID().equals(modID)) {
                incompatibilityData = incompatibilityDataList.get(i);
            }
        }

        if (incompatibilityData == null) {
            incompatibilityData = new IncompatibilityData(modID);
            incompatibilityDataList.add(incompatibilityData);
        }

        incompatibilityData.addMod(incompatibleModID);
    }

    public static List<IncompatibilityData> getIncompatibilityDataList() {
        return incompatibilityDataList;
    }

    public static void setModPackRequiredVersion(String version) {
        serverModPackRequiredVersion = version;
    }

    public static String getServerModPackRequiredVersion() {
        return serverModPackRequiredVersion;
    }
}
