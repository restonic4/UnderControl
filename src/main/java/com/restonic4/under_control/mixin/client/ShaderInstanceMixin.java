package com.restonic4.under_control.mixin.client;

import com.restonic4.under_control.client.rendering.DynamicShaderInstance;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
    @Shadow
    @Final
    private String name;

    // Why is this even a thing, pls mojang
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;<init>(Ljava/lang/String;)V"), allow = 1)
    private String modifyProgramId(String id) {
        Object current = (Object) this;

        if (current instanceof DynamicShaderInstance) {
            return FabricShaderProgram.rewriteAsId(id, name);
        }

        return id;
    }
}
