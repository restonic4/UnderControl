package com.restonic4.under_control.client.rendering.shader;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class Shader {
    public final ResourceLocation shaderLocation;
    public final VertexFormat vertexFormat;

    protected DynamicShaderInstance shaderInstance;
    public Collection<String> uniformsToCache;
    private final RenderStateShard.ShaderStateShard renderStateShard = new RenderStateShard.ShaderStateShard(getInstance());

    public Shader(ResourceLocation shaderLocation, VertexFormat vertexFormat, String... uniformsToCache) {
        this.shaderLocation = shaderLocation;
        this.vertexFormat = vertexFormat;
        this.uniformsToCache = new ArrayList<>(List.of(uniformsToCache));
    }

    public DynamicShaderInstance createInstance(ResourceProvider provider) throws IOException {
        Shader shader = this;
        DynamicShaderInstance shaderInstance = new DynamicShaderInstance(provider, shaderLocation, vertexFormat) {
            @Override
            public Shader getShader() {
                return shader;
            }
        };
        this.shaderInstance = shaderInstance;
        return shaderInstance;
    }

    public Supplier<net.minecraft.client.renderer.ShaderInstance> getInstance() {
        return () -> shaderInstance;
    }

    public void setShaderInstance(net.minecraft.client.renderer.ShaderInstance reloadedShaderInstance) {
        this.shaderInstance = (DynamicShaderInstance) reloadedShaderInstance;
    }

    public RenderStateShard.ShaderStateShard getRenderStateShard() {
        return renderStateShard;
    }
}
