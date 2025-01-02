package com.restonic4.under_control.plugin;

import net.fabricmc.loader.api.FabricLoader;

public class UnderControlMixinPlugin extends MixinActivatorPlugin {
    @Override
    public void beforeMixinApplication() {
        addLoadCondition(
                "com.restonic4.under_control.mixin.compatibility.modmenu.ModMenuAccessor",
                () -> FabricLoader.getInstance().isModLoaded("modmenu")
        );
        addLoadCondition(
                "com.restonic4.under_control.mixin.vanish.PlayerMixin",
                () -> !FabricLoader.getInstance().isModLoaded("vanish")
        );
    }
}
