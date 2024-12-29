package com.restonic4.under_control.client.gui;

import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.config.ConfigProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericConfigScreen extends Screen {
    private final Screen parent;
    private final ConfigProvider configProvider;

    private final int scrollBarWidth = 10;
    private final int scrollBottomMargin = 10;
    private final int buttonHeight = 20;
    private final int buttonSpacing = 5;
    private final int padding = 10;

    private int scrollOffset = 0;
    private int scrollHeight;
    private int visibleAreaHeight;

    private List<AbstractWidget> scrollableButtons;
    private Map<AbstractWidget, Component> widgetComments;

    public GenericConfigScreen(final Screen parent, ConfigProvider configProvider) {
        super(Component.translatable("gui.under_control.config_selector.title"));
        this.parent = parent;
        this.configProvider = configProvider;

        this.scrollableButtons = new ArrayList<>();
        this.widgetComments = new HashMap<>();
    }

    @Override
    protected void init() {
        super.init();
        updateLayout();
    }

    private void updateLayout() {
        visibleAreaHeight = this.height - (padding * 2 + buttonHeight + 30 + scrollBottomMargin);
        int minButtonWidth = 100;
        int maxButtonWidth = 200;

        int buttonWidth = this.width - 2 * padding - scrollBarWidth;

        buttonWidth = Math.min(buttonWidth, maxButtonWidth);
        buttonWidth = Math.max(buttonWidth, minButtonWidth);

        int buttonHorizontalPosition = this.width - padding - scrollBarWidth - buttonWidth;

        scrollableButtons.clear();
        widgetComments.clear();

        Map<String, Object> savedConfigs = configProvider.getDataStore();

        int y = padding;
        for (Map.Entry<String, Object> entry : savedConfigs.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Component label = Component.translatable("gui." + configProvider.getModID() + ".config.option." + key);
            int labelHeight = this.font.lineHeight;

            // Widgets types
            if (value instanceof Boolean) {
                final boolean[] currentValue = {(Boolean) value};

                Button boolButton = Button.builder(Component.literal(value.toString()), (button) -> {
                    currentValue[0] = !currentValue[0];

                    button.setMessage(Component.literal(String.valueOf(currentValue[0])));
                    configProvider.save(key, currentValue[0]);
                }).bounds(buttonHorizontalPosition, y, buttonWidth, buttonHeight).tooltip(Tooltip.create(Component.literal(configProvider.getComment(key)))).build();

                scrollableButtons.add(boolButton);
                widgetComments.put(boolButton, label);
                this.addRenderableWidget(boolButton);
            } else if (value instanceof Integer || value instanceof Float || value instanceof Long || value instanceof Double) {
                String initialText = value.toString();
                EditBox textField = new EditBox(this.font, buttonHorizontalPosition, y, buttonWidth, buttonHeight, Component.literal(initialText));

                textField.setValue(initialText);
                textField.setTooltip(Tooltip.create(Component.literal(configProvider.getComment(key))));

                if (value instanceof Integer) {
                    textField.setFilter(s -> s.matches("-?\\d*"));
                } else if (value instanceof Float || value instanceof Double) {
                    textField.setFilter(s -> s.matches("-?\\d*\\.?\\d*"));
                } else if (value instanceof Long) {
                    textField.setFilter(s -> s.matches("-?\\d*"));
                }

                textField.setResponder(newValue -> {
                    configProvider.save(key, newValue);
                });

                scrollableButtons.add(textField);
                widgetComments.put(textField, label);
                this.addRenderableWidget(textField);
            } else if (value instanceof String) {
                EditBox textField = new EditBox(this.font, buttonHorizontalPosition, y, buttonWidth, buttonHeight, Component.literal((String) value));

                textField.setValue((String) value);
                textField.setTooltip(Tooltip.create(Component.literal(configProvider.getComment(key))));

                textField.setResponder(newValue -> {
                    configProvider.save(key, newValue);
                });

                scrollableButtons.add(textField);
                widgetComments.put(textField, label);
                this.addRenderableWidget(textField);
            } else {
                Button openConfigButton = Button.builder(Component.translatable("gui." + UnderControl.MOD_ID + ".config.open_file"), (button) -> {
                    openConfigFile();
                }).bounds(buttonHorizontalPosition, y, buttonWidth, buttonHeight).tooltip(Tooltip.create(Component.literal(configProvider.getComment(key)))).build();

                scrollableButtons.add(openConfigButton);
                widgetComments.put(openConfigButton, label);
                this.addRenderableWidget(openConfigButton);
            }

            y += labelHeight + buttonHeight + buttonSpacing;
        }

        scrollHeight = (buttonHeight + buttonSpacing) * scrollableButtons.size() - buttonSpacing;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.under_control.config.back"), (button) -> {
            this.onClose();
        }).bounds((this.width - buttonWidth) / 2, this.height - buttonHeight - padding, buttonWidth, buttonHeight).build());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollOffset -= delta * 20;
        scrollOffset = Math.max(0, Math.min(scrollOffset, Math.max(0, scrollHeight - visibleAreaHeight)));
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0xFF0069B9, 0xFF00B0EF);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, padding, 16777215);

        // Scrolling

        int scrollTop = padding + 20 + padding;
        int scrollBottom = scrollTop + visibleAreaHeight;

        guiGraphics.enableScissor(padding, scrollTop, this.width - padding, scrollBottom);

        // Elements

        int y = -scrollOffset + scrollTop;
        for (AbstractWidget abstractWidget : scrollableButtons) {
            if (y + buttonHeight > scrollTop && y < scrollBottom) {
                drawWrappedText(guiGraphics, widgetComments.get(abstractWidget), padding, y, abstractWidget.getX() - padding * 2);

                abstractWidget.setY(y);
                abstractWidget.visible = true;
            } else {
                abstractWidget.visible = false;
            }
            y += buttonHeight + buttonSpacing;
        }

        guiGraphics.disableScissor();

        // Scroll bar

        if (scrollHeight > visibleAreaHeight) {
            int barHeight = (int) ((float) visibleAreaHeight / scrollHeight * visibleAreaHeight);
            int barY = scrollTop + (int) ((float) scrollOffset / scrollHeight * visibleAreaHeight);
            guiGraphics.fill(this.width - scrollBarWidth - padding, barY, this.width - padding, barY + barHeight, 0xFFCCCCCC);
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    private void drawWrappedText(GuiGraphics guiGraphics, Component text, int x, int y, int maxWidth) {
        int currentWidth = 0;
        StringBuilder currentLine = new StringBuilder();
        for (char c : text.getString().toCharArray()) {
            currentLine.append(c);
            currentWidth = this.font.width(currentLine.toString());
            if (currentWidth >= maxWidth) {
                guiGraphics.drawString(this.font, currentLine.toString(), x, y, 16777215);
                currentLine.setLength(0);
                y += this.font.lineHeight;
            }
        }
        if (!currentLine.isEmpty()) {
            guiGraphics.drawString(this.font, currentLine.toString(), x, y, 16777215);
        }
    }

    @Override
    public void onClose() {
        this.configProvider.saveToFile();
        this.minecraft.setScreen(this.parent);
    }

    private void openConfigFile() {
        try {
            String filePath = configProvider.getSaveFilePath();

            File configFile = new File(filePath);

            if (configFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(configFile);
                } else {
                    UnderControl.LOGGER.warn("Desktop is not supported, cannot open the config file.");
                }
            } else {
                UnderControl.LOGGER.warn("Config file not found at path: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            UnderControl.LOGGER.error("An error occurred while trying to open the config file.", e);
        }
    }
}
