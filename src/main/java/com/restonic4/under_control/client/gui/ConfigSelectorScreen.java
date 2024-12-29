package com.restonic4.under_control.client.gui;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.api.config.ConfigAPI;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class ConfigSelectorScreen extends Screen {
    private final Screen parent;

    private final Component clientSide;
    private final Component serverSide;
    private final Component closeMenu;

    public ConfigSelectorScreen(final Screen parent) {
        super(Component.translatable("gui.under_control.config_selector.title"));
        this.parent = parent;

        this.clientSide = Component.translatable("gui.under_control.config.client_side");
        this.serverSide = Component.translatable("gui.under_control.config.server_side");
        this.closeMenu = Component.translatable("gui.under_control.config.close_menu");
    }

    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonSpacing = 10;
        int bottomOffset = 30;

        int horizontalCenter = (this.width - buttonWidth) / 2;

        int totalHeight = (buttonHeight * 2) + buttonSpacing;
        int verticalStart = (this.height - totalHeight) / 2;

        // Client Side
        this.addRenderableWidget(Button.builder(clientSide, (button) -> {
            this.minecraft.setScreen(new GenericConfigScreen(this, ConfigAPI.getClientProvider(UnderControl.MOD_ID)));
        }).bounds(horizontalCenter, verticalStart, buttonWidth, buttonHeight).build());

        // Server Side
        this.addRenderableWidget(Button.builder(serverSide, (button) -> {
            this.minecraft.setScreen(new GenericConfigScreen(this, ConfigAPI.getServerProvider(UnderControl.MOD_ID)));
        }).bounds(horizontalCenter, verticalStart + buttonHeight + buttonSpacing, buttonWidth, buttonHeight).build());

        // Close
        this.addRenderableWidget(Button.builder(closeMenu, (button) -> {
            this.onClose();
        }).bounds(horizontalCenter, this.height - buttonHeight - bottomOffset, buttonWidth, buttonHeight).build());
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0xFF0069B9, 0xFF00B0EF);

        int titleOffset = 30;
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2,
                (this.height - (2 * 20 + 10)) / 2 - titleOffset, 16777215);

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
