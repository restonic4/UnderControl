package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class PopUpScreen extends Screen {
    // Añadir estas constantes para el diseño
    private static final int POPUP_WIDTH = 400;
    private static final int POPUP_HEIGHT = 200;
    private static final int SLICE_SIZE = 10;

    private static final ResourceLocation POPUP_TEXTURE = new ResourceLocation("under_control", "textures/gui/popup.png");
    private static final int POPUP_TEXTURE_SLICE_WIDTH = 32;
    private static final int POPUP_TEXTURE_SLICE_HEIGHT = 32;
    private static final int POPUP_TEXTURE_WIDTH = 32;
    private static final int POPUP_TEXTURE_HEIGHT = 32;

    private final Component title;
    private final Component message;
    private final Component close;

    private final Screen screen;

    protected PopUpScreen(Component title, Component message) {
        super(title);
        this.title = title;
        this.message = message;
        this.close = Component.translatable("gui.under_control.fatal_error.close");
        this.screen = null;
    }

    protected PopUpScreen(Screen parent, Component title, Component message) {
        super(title);
        this.title = title;
        this.message = message;
        this.close = Component.translatable("gui.under_control.fatal_error.close");
        this.screen = parent;
    }

    public PopUpScreen(Screen parent) {
        super(Component.literal("Fatal error!"));
        this.title = Component.literal("Fatal error!");
        this.message = Component.literal("Something went terribly wrong and we don't know why...");
        this.close = Component.translatable("gui.under_control.fatal_error.close");
        this.screen = parent;
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        /*guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.5f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();*/

        // Black background
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);

        int popupX = (this.width - POPUP_WIDTH) / 2;
        int popupY = (this.height - POPUP_HEIGHT) / 2;

        RenderingHelper.GUI.blitNineSlicedTexture(
                guiGraphics,
                POPUP_TEXTURE,
                popupX,
                popupY,
                POPUP_WIDTH,
                POPUP_HEIGHT,
                SLICE_SIZE,
                SLICE_SIZE,
                SLICE_SIZE,
                SLICE_SIZE,
                POPUP_TEXTURE_SLICE_WIDTH,
                POPUP_TEXTURE_SLICE_HEIGHT,
                0, // U
                0, // V
                POPUP_TEXTURE_WIDTH,
                POPUP_TEXTURE_HEIGHT
        );

        //guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Ajustar posición del texto
        int textY = popupY + 30;
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, textY, 0xFF0000);

        // Ajustar posición del mensaje
        int messageWidth = POPUP_WIDTH - 40;
        int messageY = textY + 30;

        for (FormattedCharSequence line : this.font.split(this.message, messageWidth)) {
            guiGraphics.drawCenteredString(this.font, line, this.width / 2, messageY, 0xFFFFFF);
            messageY += this.font.lineHeight;
        }

        // Ajustar posición del botón
        int buttonY = popupY + POPUP_HEIGHT - 30;
        super.render(guiGraphics, i, j, f);
    }

    protected void init() {
        super.init();

        // Ajustar posición del botón relativa al popup
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 2;
        int buttonY = (this.height - POPUP_HEIGHT / 2) + 60;

        this.addRenderableWidget(Button.builder(close, (button) -> {
            this.minecraft.setScreen(this.screen);
        }).bounds(buttonX, buttonY, buttonWidth, buttonHeight).build());
    }
}
