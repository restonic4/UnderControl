package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class PopUpScreen extends DynamicScreen {
    // Añadir estas constantes para el diseño
    private static final int POPUP_WIDTH = 400;
    private static final int POPUP_HEIGHT = 200;
    private static final int SLICE_SIZE = 10;

    private static final ResourceLocation POPUP_TEXTURE = new ResourceLocation("under_control", "textures/gui/popup.png");
    private static final int POPUP_TEXTURE_SLICE_WIDTH = 128;
    private static final int POPUP_TEXTURE_SLICE_HEIGHT = 128;
    private static final int POPUP_TEXTURE_WIDTH = 128;
    private static final int POPUP_TEXTURE_HEIGHT = 128;

    private final Component title;
    private final Component message;
    private final Component close;
    private final Component confirmText;

    private final Runnable confirmRunnable;

    protected PopUpScreen(Component title, Component message, Runnable confirmRunnable) {
        super(title);
        this.title = title;
        this.message = message;
        this.close = Component.translatable("gui.under_control.config.back");
        this.confirmText = Component.translatable("gui.under_control.config.confirm");
        this.confirmRunnable = confirmRunnable;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
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
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    protected void init() {
        int popupX = (this.width - POPUP_WIDTH) / 2;
        int popupY = (this.height - POPUP_HEIGHT) / 2;

        int buttonWidth = 180;
        int buttonHeight = 25;
        int spacing = 15;

        int totalWidthNeeded = (buttonWidth * 2) + spacing;
        int buttonsStartX = popupX + (POPUP_WIDTH - totalWidthNeeded) / 2;

        int buttonsY = popupY + POPUP_HEIGHT - (buttonHeight / 2);

        // Close
        this.addRenderableWidget(
                Button.builder(close, btn -> this.setShouldBeRendered(false))
                        .bounds(
                                buttonsStartX,
                                buttonsY - 2,
                                buttonWidth,
                                buttonHeight
                        )
                        .build()
        );

        // Confirm
        this.addRenderableWidget(
                Button.builder(confirmText, btn -> {
                            this.confirmRunnable.run();
                            this.setShouldBeRendered(false);
                        })
                        .bounds(
                                buttonsStartX + buttonWidth + spacing,
                                buttonsY - 2,
                                buttonWidth,
                                buttonHeight
                        )
                        .build()
        );

        this.addRenderableWidget(
                new BetterImageButton(
                        GenericConfigScreen.buttonsTexture,
                        buttonsStartX + buttonWidth + spacing,
                        buttonsY - 2,
                        buttonWidth, buttonHeight,
                        GenericConfigScreen.buttonsTextureWidth, GenericConfigScreen.buttonsTextureHeight,
                        confirmText,
                        button -> {
                            this.confirmRunnable.run();
                            this.setShouldBeRendered(false);
                        }
                )
        );

        setShouldCancelMainScreenClicks(true);
    }
}
