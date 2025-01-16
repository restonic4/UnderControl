package com.chaotic_loom.under_control.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

@Environment(value = EnvType.CLIENT)
public class FatalErrorScreen extends Screen {
    private final Component title;
    private final Component message;
    private final Component close;

    public FatalErrorScreen(Component title, Component message) {
        super(title);
        this.title = title;
        this.message = message;
        this.close = Component.translatable("gui.under_control.fatal_error.close");
    }

    public FatalErrorScreen(Screen parent) {
        super(Component.literal("Fatal error!"));
        this.title = Component.literal("Fatal error!");
        this.message = Component.literal("Something went terribly wrong and we don't know why...");
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

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -12574688, -11530224);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 90, 16777215);

        int messageWidth = this.width - 40;
        int lineHeight = this.font.lineHeight;
        int messageY = 110;

        for (FormattedCharSequence line : this.font.split(this.message, messageWidth)) {
            guiGraphics.drawCenteredString(this.font, line, this.width / 2, messageY, 16777215);
            messageY += lineHeight;
        }

        super.render(guiGraphics, i, j, f);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public Component getMessage() {
        return this.message;
    }
}