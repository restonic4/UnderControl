package com.restonic4.under_control.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;

    private final Component close;

    public ConfigScreen(final Screen parent) {
        super(Component.translatable("config.betterf3.title.config"));
        this.parent = parent;

        this.close = Component.translatable("gui.under_control.fatal_error.close");
    }

    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int horizontalCenter = (this.width - buttonWidth) / 2;
        int verticalMargin = 20;

        int buttonY = this.height - buttonHeight - verticalMargin;

        this.addRenderableWidget(Button.builder(close, (button) -> {
            this.minecraft.close();
        }).bounds(horizontalCenter, buttonY, buttonWidth, buttonHeight).build());
    }
}
