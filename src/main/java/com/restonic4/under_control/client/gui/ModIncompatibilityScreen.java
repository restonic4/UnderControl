package com.restonic4.under_control.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ModIncompatibilityScreen extends FatalErrorScreen {
    public ModIncompatibilityScreen(String originalMod, String mods) {
        super(Component.translatable("gui.under_control.incompatibility.title"), Component.translatable("gui.under_control.incompatibility.message").append(originalMod).append(" -> ").append(mods));
    }
}
