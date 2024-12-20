package com.restonic4.under_control.registries.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import com.restonic4.under_control.UnderControl;
import com.restonic4.under_control.client.rendering.DynamicShaderInstance;
import com.restonic4.under_control.client.rendering.Shader;
import com.restonic4.under_control.events.types.ShaderEvents;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UnderControlShaders {
    private static final List<Shader> shaders = new ArrayList<>();

    public static Shader SIMPLE_COLOR = registerShader(new Shader(new ResourceLocation(UnderControl.MOD_ID, "program/simple_color"), DefaultVertexFormat.POSITION));

    public static void register() {
        ShaderEvents.REGISTRATION.register((resourceProvider, shaderPairList) -> {
            for (Shader shaderToRegister : shaders) {
                shaderPairList.add(Pair.of(
                        shaderToRegister.createInstance(resourceProvider),
                        (shader) -> ((DynamicShaderInstance) shader).getShader()
                ));
            }
        });
    }

    public static Shader registerShader(Shader shader) {
        shaders.add(shader);
        return shader;
    }
}
