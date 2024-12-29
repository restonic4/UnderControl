package com.restonic4.under_control.compatibility.modmenu;

import com.google.common.collect.ImmutableMap;
import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.client.gui.ConfigSelectorScreen;
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

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, Class<? extends Screen>> screens = ConfigScreenManager.getScreens();

        ImmutableMap.Builder<String, ConfigScreenFactory<?>> map = ImmutableMap.builder();

        for (Map.Entry<String, Class<? extends Screen>> entry : screens.entrySet()) {
            String modID = entry.getKey();
            Class<? extends Screen> screenClass = entry.getValue();
            map.put(modID, parent -> {
                try {
                    return screenClass.getConstructor(Screen.class).newInstance(parent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            });

            UnderControl.LOGGER.info("Loading config screen to modmenu: " + modID);
        }

        Map<String, ConfigScreenFactory<?>> factoryMap = map.build();

        UnderControl.LOGGER.info("Screens size: " + factoryMap.size());

        return factoryMap;
    }
}
