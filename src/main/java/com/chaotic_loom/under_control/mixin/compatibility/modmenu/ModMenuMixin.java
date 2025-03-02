package com.chaotic_loom.under_control.mixin.compatibility.modmenu;

import com.terraformersmc.modmenu.ModMenu;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModMenu.class)
public class ModMenuMixin {
    @Inject(method = "getConfigScreen", at = @At("HEAD"))
    private static void getConfigScreen(String modid, Screen menuScreen, CallbackInfoReturnable<Screen> cir) {
        //System.out.println("Getting " + modid + "'s screen: " + menuScreen);
    }
}
