package com.restonic4.under_control.registries.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.client.rendering.shader.ShaderHolder;
import com.restonic4.under_control.events.types.ShaderEvents;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class UnderControlShaders {
    private static final List<ShaderHolder> SHADER_HOLDERS = new ArrayList<>();

    public static ShaderHolder SIMPLE_COLOR = registerShader(new ShaderHolder(
            new ResourceLocation(UnderControl.MOD_ID, "program/simple_color"),
            DefaultVertexFormat.POSITION
    ));

    public static void register() {
        ShaderEvents.REGISTRATION.register((resourceProvider, shaderPairList) -> {
            for (ShaderHolder shaderHolderToRegister : SHADER_HOLDERS) {
                ShaderHolder.register(resourceProvider, shaderPairList, shaderHolderToRegister);
            }
        });
    }

    public static ShaderHolder registerShader(ShaderHolder shaderHolder) {
        SHADER_HOLDERS.add(shaderHolder);
        return shaderHolder;
    }
}
