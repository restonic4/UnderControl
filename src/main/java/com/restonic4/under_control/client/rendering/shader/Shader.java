package com.restonic4.under_control.client.rendering.shader;

/*
 * Copyright 2024 Lodestar's team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is part of UnderControl, derived from Lodestone
 * by Lodestar's team. Credit goes to the original authors for their work.
 */

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Shader {
    public final ResourceLocation shaderLocation;
    public final VertexFormat vertexFormat;

    protected ExtendedShaderInstance shaderInstance;
    public Collection<String> uniformsToCache;
    private final RenderStateShard.ShaderStateShard renderStateShard = new RenderStateShard.ShaderStateShard(getInstance());

    public Shader(ResourceLocation shaderLocation, VertexFormat vertexFormat, String... uniformsToCache) {
        this.shaderLocation = shaderLocation;
        this.vertexFormat = vertexFormat;
        this.uniformsToCache = new ArrayList<>(List.of(uniformsToCache));
    }

    public ExtendedShaderInstance createInstance(ResourceProvider provider) throws IOException {
        Shader shader = this;
        ExtendedShaderInstance shaderInstance = new ExtendedShaderInstance(provider, shaderLocation, vertexFormat) {
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
        this.shaderInstance = (ExtendedShaderInstance) reloadedShaderInstance;
    }

    public RenderStateShard.ShaderStateShard getRenderStateShard() {
        return renderStateShard;
    }

    public static void register(ResourceProvider resourceProvider, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderPairList, Shader shaderToRegister) throws IOException {
        shaderPairList.add(Pair.of(
                shaderToRegister.createInstance(resourceProvider),
                (shader) -> ((ExtendedShaderInstance) shader).getShader()
        ));
    }
}
