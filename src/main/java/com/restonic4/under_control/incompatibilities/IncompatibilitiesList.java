package com.restonic4.under_control.incompatibilities;

import com.restonic4.under_control.saving.ClassProvider;

import java.util.ArrayList;
import java.util.List;

public class IncompatibilitiesList implements ClassProvider<IncompatibilitiesList> {
    private final List<String> mods;

    public IncompatibilitiesList() {
        this.mods = new ArrayList<>();
    }

    public List<String> getIncompatibleMods() {
        return mods;
    }

    public void addMod(String modID) {
        mods.add(modID);
    }

    @Override
    public String getIdentifier() {
        return "IncompatibilitiesList";
    }

    @Override
    public String serialize(IncompatibilitiesList object) {
        return String.join(",", mods);
    }

    @Override
    public IncompatibilitiesList deserialize(String data) {
        String[] mods = data.split(",");

        for (String mod : mods) {
            addMod(mod);
        }

        return this;
    }
}
