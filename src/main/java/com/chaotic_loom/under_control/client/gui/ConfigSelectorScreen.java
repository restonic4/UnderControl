package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.config.ConfigAPI;
import com.chaotic_loom.under_control.config.ConfigProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(value = EnvType.CLIENT)
public class ConfigSelectorScreen extends Screen {
    private final Screen parent;
    private String modID = UnderControl.MOD_ID;

    private final Component clientSide;
    private final Component serverSide;
    private final Component closeMenu;

    public ConfigSelectorScreen(final Screen parent) {
        super(Component.translatable("gui.under_control.config_selector.title"));
        this.parent = parent;

        this.clientSide = Component.translatable("gui.under_control.config.client_side");
        this.serverSide = Component.translatable("gui.under_control.config.server_side");
        this.closeMenu = Component.translatable("gui.under_control.config.back");
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
        ConfigProvider clientProvider = ConfigAPI.getClientProvider(this.modID);
        if (clientProvider != null) {
            this.addRenderableWidget(Button.builder(clientSide, this::onClientButton).bounds(horizontalCenter, verticalStart, buttonWidth, buttonHeight).build());
        }

        // Server Side
        ConfigProvider serverProvider = ConfigAPI.getServerProvider(this.modID);
        if (serverProvider != null) {
            this.addRenderableWidget(Button.builder(serverSide, this::onServerButton).bounds(horizontalCenter, verticalStart + buttonHeight + buttonSpacing, buttonWidth, buttonHeight).build());
        }

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

    private void onClientButton(Button button) {
        ConfigProvider configProvider = ConfigAPI.getClientProvider(this.modID);
        if (configProvider == null) {
            return;
        }

        this.minecraft.setScreen(new GenericConfigScreen(this, configProvider));
    }

    private void onServerButton(Button button) {
        ConfigProvider configProvider = ConfigAPI.getServerProvider(this.modID);
        if (configProvider == null) {
            return;
        }

        this.minecraft.setScreen(new GenericConfigScreen(this, configProvider));
    }

    public String getModID() {
        return modID;
    }

    public void setModID(String modID) {
        this.modID = modID;
    }

    public static class Builder {
        private final Screen parent;
        private final String modID;

        public Builder(Screen parent, String modID) {
            this.parent = parent;
            this.modID = modID;
        }

        public ConfigSelectorScreen build() {
            ConfigSelectorScreen screen = new ConfigSelectorScreen(parent);
            screen.setModID(this.modID);

            return screen;
        }
    }
}
