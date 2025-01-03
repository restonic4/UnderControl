package com.chaotic_loom.under_control.client.gui;

import net.minecraft.network.chat.Component;

public class ModIncompatibilityScreen extends FatalErrorScreen {
    public ModIncompatibilityScreen(String mods) {
        super(Component.translatable("gui.under_control.incompatibility.title"), Component.translatable("gui.under_control.incompatibility.message").append(mods));
    }
}
