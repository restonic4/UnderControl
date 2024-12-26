package com.restonic4.under_control.api.incompatibilities;

import java.util.ArrayList;
import java.util.List;

public class IncompatibilityData {
    private final String modID;
    private final List<String> mods;

    public IncompatibilityData(String modID) {
        this.modID = modID;
        this.mods = new ArrayList<>();
    }

    public String getModID() {
        return modID;
    }

    public List<String> getIncompatibleMods() {
        return mods;
    }

    public void addMod(String modID) {
        mods.add(modID);
    }
}