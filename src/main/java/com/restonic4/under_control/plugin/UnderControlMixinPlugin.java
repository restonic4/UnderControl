package com.restonic4.under_control.plugin;

import net.fabricmc.loader.api.FabricLoader;

public class UnderControlMixinPlugin extends MixinActivatorPlugin {
    @Override
    public void beforeMixinApplication() {
        addLoadCondition(
                "com.restonic4.under_control.mixin.compatibility.modmenu.ModMenuAccessor",
                () -> FabricLoader.getInstance().isModLoaded("modmenu")
        );

        addLoadConditions(
                () -> !FabricLoader.getInstance().isModLoaded("melius-vanish"),
                "com.restonic4.under_control.mixin.vanish.AbstractMinecartMixin",
                "com.restonic4.under_control.mixin.vanish.ArmorItemMixin",
                "com.restonic4.under_control.mixin.vanish.BeehiveBlockMixin",
                "com.restonic4.under_control.mixin.vanish.EntityGetterMixin",
                "com.restonic4.under_control.mixin.vanish.EntitySelectorMixin",
                "com.restonic4.under_control.mixin.vanish.PlayerMixin",
                "com.restonic4.under_control.mixin.vanish.VibrationSystemMixin"
        );
    }
}
