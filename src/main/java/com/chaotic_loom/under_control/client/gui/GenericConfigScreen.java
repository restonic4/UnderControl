package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.config.ConfigProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PopupScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(value = EnvType.CLIENT)
public class GenericConfigScreen extends Screen {
    public static final ResourceLocation buttonsTexture = new ResourceLocation(UnderControl.MOD_ID, "textures/gui/buttons.png");
    public static final int buttonsTextureWidth = 200;
    public static final int buttonsTextureHeight = 60;

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
    private Map<AbstractWidget, Button> resetButtons;

    private PopUpScreen currentPopUp = null;

    public GenericConfigScreen(final Screen parent, ConfigProvider configProvider) {
        super(Component.translatable("gui.under_control.config_selector.title"));
        this.parent = parent;
        this.configProvider = configProvider;

        this.scrollableButtons = new ArrayList<>();
        this.resetButtons = new HashMap<>();
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

        int buttonHorizontalPosition = this.width - padding * 2 - scrollBarWidth - buttonWidth;

        scrollableButtons.clear();
        widgetComments.clear();
        resetButtons.clear();

        Map<String, Object> savedConfigs = configProvider.getConfigs();

        int y = padding;
        for (Map.Entry<String, Object> entry : savedConfigs.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Component label = Component.translatable("gui." + configProvider.getModID() + ".config.option." + key);
            int labelHeight = this.font.lineHeight;

            AbstractWidget abstractWidget;

            // Widgets types
            if (value instanceof Boolean) {
                final boolean[] currentValue = {(Boolean) value};

                Button boolButton = Button.builder(Component.literal(value.toString()), (button) -> {
                    currentValue[0] = !currentValue[0];

                    button.setMessage(Component.literal(String.valueOf(currentValue[0])));
                    configProvider.save(key, currentValue[0]);
                }).bounds(buttonHorizontalPosition, y, buttonWidth, buttonHeight).tooltip(Tooltip.create(Component.literal(configProvider.getComment(key)))).build();

                abstractWidget = boolButton;
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
                    try {
                        if (value instanceof Integer) {
                            textField.setFilter(s -> s.matches("-?\\d*"));
                            configProvider.save(key, Integer.parseInt(newValue));
                        } else if (value instanceof Long) {
                            textField.setFilter(s -> s.matches("-?\\d*"));
                            configProvider.save(key, Long.parseLong(newValue));
                        } else if (value instanceof Float) {
                            textField.setFilter(s -> s.matches("-?\\d*\\.?\\d*"));
                            configProvider.save(key, Float.parseFloat(newValue));
                        } else if (value instanceof Double) {
                            textField.setFilter(s -> s.matches("-?\\d*\\.?\\d*"));
                            configProvider.save(key, Double.parseDouble(newValue));
                        }
                    } catch (NumberFormatException e) {
                        textField.setValue(initialText);
                    }
                });

                abstractWidget = textField;
            } else if (value instanceof String) {
                EditBox textField = new EditBox(this.font, buttonHorizontalPosition, y, buttonWidth, buttonHeight, Component.literal((String) value));

                textField.setValue((String) value);
                textField.setTooltip(Tooltip.create(Component.literal(configProvider.getComment(key))));

                textField.setResponder(newValue -> {
                    configProvider.save(key, newValue);
                });

                abstractWidget = textField;
            } else if (value instanceof BlockPos blockPos) {
                String initialText = value.toString();

                String formattedText;

                int start = initialText.indexOf('{');
                int end = initialText.indexOf('}');

                if (start != -1 && end != -1 && start < end) {
                    formattedText = initialText.substring(start + 1, end);
                } else {
                    formattedText = initialText;
                }

                EditBox textField = new EditBox(this.font, buttonHorizontalPosition, y, buttonWidth, buttonHeight, Component.literal(formattedText));

                textField.setValue(formattedText);
                textField.setTooltip(Tooltip.create(Component.literal(configProvider.getComment(key))));

                textField.setFilter(s -> s.matches("^x=-?\\d+,\\s*y=-?\\d+,\\s*z=-?\\d+$"));

                textField.setResponder(newValue -> {
                    if (newValue.matches("^x=-?\\d+,\\s*y=-?\\d+,\\s*z=-?\\d+$")) {
                        String[] parts = newValue.split("[=,]");

                        try {
                            int bx = Integer.parseInt(parts[1].trim());
                            int by = Integer.parseInt(parts[3].trim());
                            int bz = Integer.parseInt(parts[5].trim());

                            configProvider.save(key, new BlockPos(bx, by, bz));
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            textField.setValue(formattedText);
                        }
                    } else {
                        textField.setValue(formattedText);
                    }
                });

                abstractWidget = textField;

            } else {
                Button openConfigButton = Button.builder(Component.translatable("gui." + UnderControl.MOD_ID + ".config.open_file"), (button) -> {
                    openConfigFile();
                }).bounds(buttonHorizontalPosition, y, buttonWidth, buttonHeight).tooltip(Tooltip.create(Component.literal(configProvider.getComment(key)))).build();

                abstractWidget = openConfigButton;
            }

            /*Button resetButton = Button.builder(Component.literal("X"), (button) -> {
                configProvider.resetOption(key);
                rebuildWidgets();
            }).bounds(buttonHorizontalPosition + buttonWidth, y, buttonHeight, buttonHeight).tooltip(Tooltip.create(Component.translatable("gui.under_control.config.reset_option"))).build();*/

            BetterImageButton resetButton = new BetterImageButton(
                    buttonsTexture,
                    buttonHorizontalPosition + buttonWidth,
                    y,
                    buttonHeight, buttonHeight,
                    buttonsTextureWidth, buttonsTextureHeight,
                    Component.literal("X"),
                    button -> {
                        configProvider.resetOption(key);
                        rebuildWidgets();
                    }
            );
            resetButton.setTooltip(Tooltip.create(Component.translatable("gui.under_control.config.reset_option")));

            scrollableButtons.add(abstractWidget);
            widgetComments.put(abstractWidget, label);
            this.addRenderableWidget(abstractWidget);

            this.resetButtons.put(abstractWidget, resetButton);
            this.addRenderableWidget(resetButton);

            y += labelHeight + buttonHeight + buttonSpacing;
        }

        scrollHeight = (buttonHeight + buttonSpacing) * scrollableButtons.size() - buttonSpacing;

        int buttonY = this.height - buttonHeight - padding;
        int spacing = 5;
        int bottomButtonWidth = 150;

        int totalWidth = bottomButtonWidth * 2 + spacing;
        int startX = (this.width - totalWidth) / 2;

        Button backButton = Button.builder(Component.translatable("gui.under_control.config.back"), button -> {
                    this.onClose();
                })
                .bounds(startX, buttonY, bottomButtonWidth, buttonHeight)
                .build();

        BetterImageButton resetAllButton = new BetterImageButton(
                buttonsTexture,
                startX + bottomButtonWidth + spacing,
                buttonY,
                bottomButtonWidth, buttonHeight,
                buttonsTextureWidth, buttonsTextureHeight,
                Component.translatable("gui.under_control.config.reset_all"),
                button -> {
                    onResetAllButton();
                }
        );
        resetAllButton.setTooltip(Tooltip.create(Component.translatable("gui.under_control.config.reset_all.tooltip")));

        this.addRenderableWidget(backButton);
        this.addRenderableWidget(resetAllButton);
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
            Button resetButton = resetButtons.get(abstractWidget);

            if (y + buttonHeight > scrollTop && y < scrollBottom) {
                drawWrappedText(guiGraphics, widgetComments.get(abstractWidget), padding, y, abstractWidget.getX() - padding * 2);

                abstractWidget.setY(y);
                abstractWidget.visible = true;

                if (resetButton != null) {
                    resetButton.setY(y);
                    resetButton.visible = true;
                }
            } else {
                abstractWidget.visible = false;

                if (resetButton != null) {
                    resetButton.visible = false;
                }
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

    private void onResetAllButton() {
        if (this.currentPopUp == null) {
            this.currentPopUp = new PopUpScreen(
                    Component.translatable("gui.under_control.config.reset_all.title"),
                    Component.translatable("gui.under_control.config.reset_all.message"),
                    () -> {
                        configProvider.resetAll();
                        rebuildWidgets();
                    }
            );
        }

        this.currentPopUp.setShouldBeRendered(true);
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
