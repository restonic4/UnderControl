package com.restonic4.under_control.incompatibilities;

import com.restonic4.under_control.saving.ClassProvider;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IncompatibilitiesList implements ClassProvider<IncompatibilitiesList>, Serializable {
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
        IncompatibilitiesList newInstance = new IncompatibilitiesList();
        String[] mods = data.split(",");

        for (String mod : mods) {
            if (!mod.isBlank() && !mod.isEmpty()) {
                newInstance.addMod(mod);
            }
        }

        return newInstance;
    }
}
