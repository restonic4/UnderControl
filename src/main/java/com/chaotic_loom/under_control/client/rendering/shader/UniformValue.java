package com.chaotic_loom.under_control.client.rendering.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value = EnvType.CLIENT)
public class UniformValue {
    private final Object value;

    public UniformValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
