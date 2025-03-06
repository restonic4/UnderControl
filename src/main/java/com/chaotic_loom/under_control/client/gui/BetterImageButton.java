package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class BetterImageButton extends Button {
    private ResourceLocation texture;
    private int textureWidth, textureHeight;
    private int totalTextureWidth, totalTextureHeight;

    private int u = 0;
    private int outerSliceWidth = 20;
    private int outerSliceHeight = 4;

    public BetterImageButton(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int totalTextureWidth, int totalTextureHeight, Component text, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, text, onPress, createNarration);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.totalTextureWidth = totalTextureWidth;
        this.totalTextureHeight = totalTextureHeight;
    }

    public BetterImageButton(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight, int totalTextureWidth, int totalTextureHeight, Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.totalTextureWidth = totalTextureWidth;
        this.totalTextureHeight = totalTextureHeight;
    }

    public BetterImageButton(ResourceLocation texture, int x, int y, int width, int height, int totalTextureWidth, int totalTextureHeight, Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.textureWidth = 200;
        this.textureHeight = 20;
        this.totalTextureWidth = totalTextureWidth;
        this.totalTextureHeight = totalTextureHeight;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        Minecraft minecraft = Minecraft.getInstance();

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        
        RenderingHelper.GUI.blitNineSlicedTexture(
                guiGraphics,
                this.texture,
                this.getX(), this.getY(),
                this.getWidth(), this.getHeight(),
                this.outerSliceWidth, this.outerSliceHeight,
                this.textureWidth, this.textureHeight,
                this.u, getTextureY(),
                this.totalTextureWidth, this.totalTextureHeight
        );

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        int k = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, k | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public void setU(int u) {
        this.u = u;
    }

    public void setOuterSliceWidth(int outerSliceWidth) {
        this.outerSliceWidth = outerSliceWidth;
    }

    public void setOuterSliceHeight(int outerSliceHeight) {
        this.outerSliceHeight = outerSliceHeight;
    }

    public BetterImageButton setTooltip(MutableComponent component) {
        super.setTooltip(Tooltip.create(component));
        return this;
    }
}
