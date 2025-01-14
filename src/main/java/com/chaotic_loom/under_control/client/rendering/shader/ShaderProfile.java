package com.chaotic_loom.under_control.client.rendering.shader;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShaderProfile {
    private final ShaderHolder shaderHolder;
    private final Map<String, UniformValue> uniformData;

    public ShaderProfile(ShaderHolder shaderHolder) {
        this.shaderHolder = shaderHolder;
        this.uniformData = new HashMap<>();
    }

    public ShaderHolder getShaderHolder() {
        return shaderHolder;
    }

    public void setUniformData(String uniform, float value) {
        uniformData.put(uniform, new UniformValue(value));
    }

    public void setUniformData(String uniform, float value1, float value2) {
        uniformData.put(uniform, new UniformValue(new float[]{value1, value2}));
    }

    public void setUniformData(String uniform, float value1, float value2, float value3) {
        uniformData.put(uniform, new UniformValue(new float[]{value1, value2, value3}));
    }

    public void setUniformData(String uniform, float value1, float value2, float value3, float value4) {
        uniformData.put(uniform, new UniformValue(new float[]{value1, value2, value3, value4}));
    }

    public void setUniformData(String uniform, int value) {
        uniformData.put(uniform, new UniformValue(value));
    }

    public void setUniformData(String uniform, int value1, int value2) {
        uniformData.put(uniform, new UniformValue(new int[]{value1, value2}));
    }

    public void setUniformData(String uniform, int value1, int value2, int value3) {
        uniformData.put(uniform, new UniformValue(new int[]{value1, value2, value3}));
    }

    public void setUniformData(String uniform, int value1, int value2, int value3, int value4) {
        uniformData.put(uniform, new UniformValue(new int[]{value1, value2, value3, value4}));
    }

    public void setUniformData(String uniform, float[] values) {
        uniformData.put(uniform, new UniformValue(values));
    }

    public void setUniformData(String uniform, int[] values) {
        uniformData.put(uniform, new UniformValue(values));
    }

    public void setUniformData(String uniform, Vector3f vector) {
        uniformData.put(uniform, new UniformValue(vector));
    }

    public void setUniformData(String uniform, Vector4f vector) {
        uniformData.put(uniform, new UniformValue(vector));
    }

    public void setUniformData(String uniform, Matrix3f matrix) {
        uniformData.put(uniform, new UniformValue(matrix));
    }

    public void setUniformData(String uniform, Matrix4f matrix) {
        uniformData.put(uniform, new UniformValue(matrix));
    }

    public void apply() {
        for (Map.Entry<String, UniformValue> entry : uniformData.entrySet()) {
            String uniform = entry.getKey();
            Object data = entry.getValue().getValue();

            var uniformInstance = shaderHolder.getInstance().get().safeGetUniform(uniform);

            if (data instanceof Float) {
                uniformInstance.set((float) data);
            } else if (data instanceof Integer) {
                uniformInstance.set((int) data);
            } else if (data instanceof float[]) {
                uniformInstance.set((float[]) data);
            } else if (data instanceof Vector3f) {
                uniformInstance.set((Vector3f) data);
            } else if (data instanceof Matrix4f) {
                uniformInstance.set((Matrix4f) data);
            } else if (data instanceof Matrix3f) {
                uniformInstance.set((Matrix3f) data);
            } else {
                throw new IllegalArgumentException("Unsupported uniform type for: " + uniform);
            }
        }
    }
}
