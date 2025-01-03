package com.chaotic_loom.under_control.compatibility.modmenu;

import com.chaotic_loom.under_control.client.gui.ConfigSelectorScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuAPIImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigSelectorScreen::new;
    }
}
