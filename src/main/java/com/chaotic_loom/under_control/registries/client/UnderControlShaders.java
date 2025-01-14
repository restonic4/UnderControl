package com.chaotic_loom.under_control.registries.client;

import com.chaotic_loom.under_control.client.rendering.shader.ShaderProfile;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.Registration;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.client.rendering.shader.ShaderHolder;
import com.chaotic_loom.under_control.events.types.ShaderEvents;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@Registration(side = ExecutionSide.CLIENT)
public class UnderControlShaders {
    private static final List<ShaderHolder> SHADER_HOLDERS = new ArrayList<>();

    public static ShaderHolder SIMPLE_COLOR = registerShader(new ShaderHolder(
            new ResourceLocation(UnderControl.MOD_ID, "program/simple_color"),
            DefaultVertexFormat.POSITION
    ));

    public static ShaderHolder VERTICAL_GRADIENT = registerShader(new ShaderHolder(
            new ResourceLocation(UnderControl.MOD_ID, "program/vertical_gradient"),
            DefaultVertexFormat.POSITION,
            "TopColor", "BottomColor"
    ));

    public static ShaderProfile VERTICAL_GRADIENT_PROFILE = new ShaderProfile(VERTICAL_GRADIENT);

    static {
        VERTICAL_GRADIENT_PROFILE.setUniformData("TopColor", new float[] {1, 0, 0, 1});
        VERTICAL_GRADIENT_PROFILE.setUniformData("BottomColor", new float[] {0, 1, 0, 1});
    }

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
