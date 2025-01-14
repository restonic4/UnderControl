package com.chaotic_loom.under_control.client.rendering.shader;

public class UniformValue {
    private final Object value;

    public UniformValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
