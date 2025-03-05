package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.client.gui.BetterImageButton;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractButton.class)
public class AbstractButtonMixin {
    @Inject(method = "getTextureY", at = @At("RETURN"), cancellable = true)
    public final void getTextureY(CallbackInfoReturnable<Integer> cir) {
        AbstractButton self = (AbstractButton) (Object) this;
        int original = cir.getReturnValue();

        if (self instanceof BetterImageButton) {
            cir.setReturnValue(original - 46);
            cir.cancel();
        }
    }
}
