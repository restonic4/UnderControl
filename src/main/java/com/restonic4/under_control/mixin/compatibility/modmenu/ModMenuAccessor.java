package com.restonic4.under_control.mixin.compatibility.modmenu;

import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ModMenu.class)
public interface ModMenuAccessor {
    @Accessor("delayedScreenFactoryProviders")
    static List<Map<String, ConfigScreenFactory<?>>> getDelayedScreenFactoryProviders() {
        throw new UnsupportedOperationException();
    }

    @Accessor("delayedScreenFactoryProviders")
    static void setDelayedScreenFactoryProviders(List<Map<String, ConfigScreenFactory<?>>> value) {
        throw new UnsupportedOperationException();
    }
}
