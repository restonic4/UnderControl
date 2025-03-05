package com.chaotic_loom.under_control.client.gui;

import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DynamicScreen extends Screen {
    private boolean shouldBeRendered = false;
    private boolean shouldCancelMainScreenClicks = false;

    protected DynamicScreen(Component component) {
        super(component);
    }

    public final void preRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (this.shouldBeRendered) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 100);

            renderWithTooltip(guiGraphics, mouseX, mouseY, delta);

            guiGraphics.pose().popPose();
        }
    }

    public boolean isShouldBeRendered() {
        return shouldBeRendered;
    }

    public void setShouldBeRendered(boolean shouldBeRendered) {
        this.shouldBeRendered = shouldBeRendered;

        if (shouldBeRendered) {
            added();

            Minecraft minecraft = Minecraft.getInstance();

            minecraft.mouseHandler.releaseMouse();
            KeyMapping.releaseAll();

            init(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());

            minecraft.updateTitle();
        } else {
            removed();
        }
    }

    @Override
    public void added() {
        super.added();

        RenderingHelper.GUI.registerDynamicScreen(this);
    }

    @Override
    public void removed() {
        super.removed();

        RenderingHelper.GUI.removeDynamicScreen(this);
    }

    public boolean shouldCancelMainScreenClicks() {
        return shouldCancelMainScreenClicks;
    }

    public void setShouldCancelMainScreenClicks(boolean shouldCancelMainScreenClicks) {
        this.shouldCancelMainScreenClicks = shouldCancelMainScreenClicks;
    }
}
