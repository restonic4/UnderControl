package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.client.gui.DynamicScreen;
import com.chaotic_loom.under_control.client.rendering.RenderingHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow private double xpos;

    @Shadow private double ypos;

    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "onPress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                    ordinal = 0
            )
    )
    private void interceptMouseClick(
            long windowId, int button, int action, int mods,
            CallbackInfo ci,
            @Local(ordinal = 3) int mouseButton,
            @Local boolean[] bls, @Local(ordinal = 0) double scaledX, @Local(ordinal = 1) double scaledY
    ) {
        Minecraft client = Minecraft.getInstance();
        if (windowId != client.getWindow().getWindow()) return;

        for (DynamicScreen dynamicScreen : RenderingHelper.GUI.getDynamicScreens()) {
            if (dynamicScreen.isShouldBeRendered() && dynamicScreen.mouseClicked(scaledX, scaledY, mouseButton)) {
                bls[0] = true; // Evento consumido
                break;
            }
        }
    }

    @WrapOperation(
            method = "onPress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V"
            )
    )
    private void wrapScreenError(Runnable runnable, String string, String string2, Operation<Void> original, @Local boolean[] bls) {
        boolean shouldCancelClick = false;

        // Consumed by dynamic screen
        if (bls[0]) {
            return;
        }

        List<DynamicScreen> activeScreens = RenderingHelper.GUI.getDynamicScreens();
        for (int i = activeScreens.size() - 1; i >= 0 && !shouldCancelClick; i--) {
            DynamicScreen dynamicScreen = activeScreens.get(i);
            if (dynamicScreen.isShouldBeRendered() && dynamicScreen.shouldCancelMainScreenClicks()) {
                shouldCancelClick = true;
            }
        }

        if (!shouldCancelClick) {
            original.call(runnable, string, string2);
        }
    }
}
