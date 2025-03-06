package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.config.ConfigProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
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

//TODO: NUMBER CONFIGS BROKEN, CANT CHANGE THEM

@Environment(value = EnvType.CLIENT)
public class GenericConfigScreen extends Screen {
    public static final ResourceLocation BUTTONS_TEXTURE = new ResourceLocation(UnderControl.MOD_ID, "textures/gui/buttons.png");
    public static final int BUTTONS_TEXTURE_WIDTH = 200;
    public static final int BUTTONS_TEXTURE_HEIGHT = 60;

    private static final int SCROLLBAR_WIDTH = 10;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;
    private static final int TITLE_SPACING = 15;
    private static final int GROUP_SPACING = 20;
    private static final int ELEMENT_SPACING = 5;

    private static final int GAP = 5;
    private static final int LABEL_WIDTH = 250;

    private final Screen parent;
    private final ConfigProvider configProvider;

    private int scrollOffset = 0;
    private int totalContentHeight = 0;
    private int visibleAreaHeight;

    private final List<ConfigGroup> configGroups = new ArrayList<>();
    private final List<AbstractWidget> dynamicWidgets = new ArrayList<>();
    private PopUpScreen currentPopUp = null;

    public GenericConfigScreen(Screen parent, ConfigProvider configProvider) {
        super(Component.translatable("gui.under_control.config_selector.title"));
        this.parent = parent;
        this.configProvider = configProvider;
    }

    @Override
    protected void init() {
        super.init();
        rebuildConfigStructure();
        setupStaticButtons();
    }

    private void rebuildConfigStructure() {
        configGroups.clear();
        dynamicWidgets.clear();

        int yCounter = PADDING;
        int resetButtonSize = BUTTON_HEIGHT;
        int widgetX = PADDING + LABEL_WIDTH + GAP;
        int availableWidth = width - widgetX - PADDING - SCROLLBAR_WIDTH;
        int widgetWidth = availableWidth - resetButtonSize - GAP;
        int resetButtonX = widgetX + widgetWidth + GAP;

        int staticButtonsHeight = BUTTON_HEIGHT + PADDING;
        int headerHeight = PADDING + font.lineHeight + PADDING;
        visibleAreaHeight = height - headerHeight - staticButtonsHeight;

        for (Map.Entry<String, Map<String, Object>> groupEntry : configProvider.getConfigs().entrySet()) {
            ConfigGroup group = new ConfigGroup(
                    Component.translatable("gui." + configProvider.getModID() + ".config.option.group." + groupEntry.getKey())
                            .withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE),
                    yCounter
            );

            yCounter += font.lineHeight + TITLE_SPACING;

            for (Map.Entry<String, Object> entry : groupEntry.getValue().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                Component label = Component.translatable("gui." + configProvider.getModID() + ".config.option." + key);
                Component comment = Component.translatable("gui." + configProvider.getModID() + ".config.option." + key + ".tooltip");

                ConfigEntry configEntry = new ConfigEntry(
                        createValueWidget(key, value, comment, widgetX, yCounter, widgetWidth, BUTTON_HEIGHT),
                        createResetButton(key, resetButtonX, yCounter, resetButtonSize, resetButtonSize),
                        label,
                        comment
                );

                group.addEntry(configEntry);
                dynamicWidgets.add(configEntry.widget);
                dynamicWidgets.add(configEntry.resetButton);

                yCounter += BUTTON_HEIGHT + ELEMENT_SPACING;
            }

            configGroups.add(group);
            yCounter += GROUP_SPACING;
        }

        totalContentHeight = yCounter + BUTTON_HEIGHT;
        visibleAreaHeight = height - 3 * PADDING - BUTTON_HEIGHT;

        for (AbstractWidget widget : dynamicWidgets) {
            addRenderableWidget(widget);
        }
    }

    private AbstractWidget createValueWidget(String key, Object value, Component comment, int x, int y, int width, int height) {
        if (value instanceof Boolean) {
            return createBooleanWidget(key, (Boolean) value, comment, x, y, width, height);
        }
        if (value instanceof Number) {
            return createNumberWidget(key, (Number) value, comment, x, y, width, height);
        }
        if (value instanceof String) {
            return createStringWidget(key, (String) value, comment, x, y, width, height);
        }
        if (value instanceof BlockPos) {
            return createBlockPosWidget(key, (BlockPos) value, comment, x, y, width, height);
        }
        return createDefaultWidget(key, comment, x, y, width, height);
    }

    private AbstractWidget createBooleanWidget(String key, Boolean value, Component comment, int x, int y, int width, int height) {
        return Button.builder(Component.literal(value.toString()), btn -> {
                    boolean newValue = !Boolean.parseBoolean(btn.getMessage().getString());
                    btn.setMessage(Component.literal(String.valueOf(newValue)));
                    configProvider.save(key, newValue);
                })
                .pos(x, y)
                .size(width, height)
                .tooltip(Tooltip.create(comment))
                .build();
    }

    private AbstractWidget createNumberWidget(String key, Number value, Component comment, int x, int y, int width, int height) {
        String initialText = value.toString();
        EditBox textField = new EditBox(font, x, y, width, height, Component.literal(initialText));
        textField.setValue(initialText);
        textField.setTooltip(Tooltip.create(comment));

        // Filter
        if (value instanceof Integer || value instanceof Long) {
            textField.setFilter(s -> s.matches("-?\\d*"));
        } else if (value instanceof Float || value instanceof Double) {
            textField.setFilter(s -> s.matches("-?\\d*\\.?\\d*"));
        }

        textField.setResponder(newValue -> {
            try {
                if (value instanceof Integer) {
                    configProvider.save(key, Integer.parseInt(newValue));
                } else if (value instanceof Long) {
                    configProvider.save(key, Long.parseLong(newValue));
                } else if (value instanceof Float) {
                    configProvider.save(key, Float.parseFloat(newValue));
                } else if (value instanceof Double) {
                    configProvider.save(key, Double.parseDouble(newValue));
                }
            } catch (NumberFormatException e) {
                textField.setValue(initialText);
            }
        });

        return textField;
    }

    private AbstractWidget createDefaultWidget(String key, Component comment, int x, int y, int width, int height) {
        Button openConfigButton = Button.builder(Component.translatable("gui." + UnderControl.MOD_ID + ".config.open_file"), (button) -> {
            openConfigFile();
        }).bounds(x, y, width, height).tooltip(Tooltip.create(comment)).build();

        return openConfigButton;
    }

    private AbstractWidget createBlockPosWidget(String key, BlockPos value, Component comment, int x, int y, int width, int height) {
        String initialText = value.toString();

        String formattedText;

        int start = initialText.indexOf('{');
        int end = initialText.indexOf('}');

        if (start != -1 && end != -1 && start < end) {
            formattedText = initialText.substring(start + 1, end);
        } else {
            formattedText = initialText;
        }

        EditBox textField = new EditBox(font, x, y, width, height, Component.literal(formattedText));

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

        return textField;
    }

    private AbstractWidget createStringWidget(String key, String value, Component comment, int x, int y, int width, int height) {
        EditBox textField = new EditBox(font, x, y, width, height, Component.literal((String) value));

        textField.setValue((String) value);
        textField.setTooltip(Tooltip.create(comment));

        textField.setResponder(newValue -> {
            configProvider.save(key, newValue);
        });

        return textField;
    }

    private Button createResetButton(String key, int x, int y, int resetButtonSize, int resetButtonSize1) {
        BetterImageButton resetButton = new BetterImageButton(
                BUTTONS_TEXTURE,
                x,
                y,
                resetButtonSize, resetButtonSize1,
                BUTTONS_TEXTURE_WIDTH, BUTTONS_TEXTURE_HEIGHT,
                Component.literal("X"),
                button -> {
                    configProvider.resetOption(key);
                    rebuildWidgets();
                }
        );
        resetButton.setTooltip(Tooltip.create(Component.translatable("gui.under_control.config.reset_option")));

        return resetButton;
    }

    private Number parseNumber(String text, Number original) {
        if (original instanceof Integer) return Integer.parseInt(text);
        if (original instanceof Long) return Long.parseLong(text);
        if (original instanceof Float) return Float.parseFloat(text);
        if (original instanceof Double) return Double.parseDouble(text);
        return original;
    }

    private void setupStaticButtons() {
        int buttonY = height - BUTTON_HEIGHT - PADDING;
        int buttonWidth = 150;
        int spacing = 5;
        int leftButtonX = (width - (buttonWidth * 2 + spacing)) / 2;
        int rightButtonX = leftButtonX + buttonWidth + spacing;

        addRenderableWidget(
                Button.builder(Component.translatable("gui.under_control.config.back"), btn -> onClose())
                        .pos(leftButtonX, buttonY)
                        .size(buttonWidth, BUTTON_HEIGHT)
                        .build()
        );

        addRenderableWidget(
                new BetterImageButton(
                        BUTTONS_TEXTURE,
                        rightButtonX,
                        buttonY,
                        buttonWidth, BUTTON_HEIGHT,
                        200, 60,
                        Component.translatable("gui.under_control.config.reset_all"),
                        btn -> onResetAllButton()
                ).setTooltip(Component.translatable("gui.under_control.config.reset_all.tooltip"))
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        renderScrollableContent(guiGraphics, mouseX, mouseY, delta);
        renderScrollBar(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, width, height, 0xFF0069B9, 0xFF00B0EF);
        guiGraphics.drawCenteredString(font, title, width / 2, PADDING, 0xFFFFFF);
    }

    private void renderScrollableContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int scrollTop = PADDING + font.lineHeight + PADDING;
        int scrollBottom = height - BUTTON_HEIGHT - PADDING - BUTTON_HEIGHT;

        guiGraphics.enableScissor(PADDING, scrollTop, width - PADDING, scrollBottom);

        int currentY = scrollTop - scrollOffset;
        for (ConfigGroup group : configGroups) {
            // Render group title
            if (currentY + font.lineHeight > scrollTop && currentY < scrollBottom) {
                guiGraphics.drawString(font, group.title(), PADDING, currentY, 0xFFFFFF);
            }
            currentY += font.lineHeight + TITLE_SPACING;

            // Render group entries
            for (ConfigEntry entry : group.entries()) {
                if (currentY + BUTTON_HEIGHT > scrollTop && currentY < scrollBottom) {
                    entry.widget.setY(currentY);
                    entry.resetButton.setY(currentY);
                    renderEntryLabel(guiGraphics, entry, currentY);
                } else {
                    entry.widget.visible = false;
                    entry.resetButton.visible = false;
                }
                currentY += BUTTON_HEIGHT + ELEMENT_SPACING;
            }
            currentY += GROUP_SPACING;
        }

        guiGraphics.disableScissor();
    }

    private void renderEntryLabel(GuiGraphics guiGraphics, ConfigEntry entry, int y) {
        entry.widget.visible = true;
        entry.resetButton.visible = true;
        guiGraphics.drawWordWrap(
                font,
                entry.label(),
                PADDING,
                y + (BUTTON_HEIGHT - font.lineHeight) / 2,
                entry.widget.getX() - 2 * PADDING,
                0xFFFFFF
        );
    }

    private void renderScrollBar(GuiGraphics guiGraphics) {
        if (totalContentHeight <= visibleAreaHeight) return;

        float scrollProgress = (float) scrollOffset / (totalContentHeight - visibleAreaHeight);
        int scrollBarHeight = (int) (visibleAreaHeight * (visibleAreaHeight / (float) totalContentHeight));
        int scrollY = PADDING + (int) ((visibleAreaHeight - scrollBarHeight) * scrollProgress);

        guiGraphics.fill(
                width - SCROLLBAR_WIDTH - PADDING,
                scrollY,
                width - PADDING,
                scrollY + scrollBarHeight,
                0xFFCCCCCC
        );
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollOffset = Mth.clamp(
                scrollOffset - (int) (delta * 20),
                0,
                Math.max(0, totalContentHeight - visibleAreaHeight)
        );
        return true;
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

    private record ConfigGroup(Component title, List<ConfigEntry> entries, int initialY) {
        ConfigGroup(Component title, int initialY) {
            this(title, new ArrayList<>(), initialY);
        }

        void addEntry(ConfigEntry entry) {
            entries.add(entry);
        }
    }

    private record ConfigEntry(AbstractWidget widget, Button resetButton, Component label, Component comment) {}
}
