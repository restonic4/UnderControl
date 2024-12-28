package com.restonic4.under_control.compatibility.modmenu;

import com.restonic4.under_control.client.gui.ConfigScreen;
import com.restonic4.under_control.client.gui.FatalErrorScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;

public class ModMenuAPIImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }
}
