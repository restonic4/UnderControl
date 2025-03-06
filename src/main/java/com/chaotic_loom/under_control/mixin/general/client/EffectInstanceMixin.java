package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.client.rendering.shader.ExtendedShaderInstance;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Program;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EffectInstance.class)
public class EffectInstanceMixin {
    // Why is this even a thing, pls mojang

    @WrapOperation(
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/resources/ResourceLocation",
                    ordinal = 0
            ),
            method = "<init>"
    )
    ResourceLocation constructProgramIdentifier(String string, Operation<ResourceLocation> original) {
        if (!id.contains(":")) {
            return original.call(arg);
        }
        ResourceLocation split = new ResourceLocation(id);
        return new ResourceLocation(split.getNamespace(), "shaders/program/" + split.getPath() + ".json");
    }
    /*ResourceLocation constructProgramIdentifier(String arg, Operation<ResourceLocation> original, ResourceProvider unused, String id) {
        if (!id.contains(":")) {
            return original.call(arg);
        }
        ResourceLocation split = new ResourceLocation(id);
        return new ResourceLocation(split.getNamespace(), "shaders/program/" + split.getPath() + ".json");
    }*/


    @WrapOperation(
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/resources/ResourceLocation",
                    ordinal = 0
            ),
            method = "getOrCreate"
    )
    private static ResourceLocation constructProgramIdentifier(String arg, Operation<ResourceLocation> original, ResourceProvider unused, Program.Type shaderType, String id) {
        if (!arg.contains(":")) {
            return original.call(arg);
        }
        ResourceLocation split = new ResourceLocation(id);
        return new ResourceLocation(split.getNamespace(), "shaders/program/" + split.getPath() + shaderType.getExtension());
    }
}
