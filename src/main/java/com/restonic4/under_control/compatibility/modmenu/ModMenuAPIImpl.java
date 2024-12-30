package com.restonic4.under_control.compatibility.modmenu;

import com.google.common.collect.ImmutableMap;
import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.client.gui.ConfigSelectorScreen;
import com.restonic4.under_control.client.gui.FatalErrorScreen;
import com.restonic4.under_control.client.gui.GenericConfigScreen;
import com.restonic4.under_control.config.ConfigProvider;
import com.restonic4.under_control.config.ConfigScreenManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.util.Map;

public class ModMenuAPIImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigSelectorScreen::new;
    }
}
