package com.chaotic_loom.under_control.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(value = EnvType.CLIENT)
public class ModIncompatibilityScreen extends FatalErrorScreen {
    public ModIncompatibilityScreen(String mods) {
        super(Component.translatable("gui.under_control.incompatibility.title"), Component.translatable("gui.under_control.incompatibility.message").append(mods));
    }
}
