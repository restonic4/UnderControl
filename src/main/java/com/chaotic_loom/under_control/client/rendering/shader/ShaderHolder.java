package com.chaotic_loom.under_control.client.rendering.shader;

/*
 * Copyright 2024 Lodestar's team
 *
 * This class is based on code from Lodestone,
 * developed by Lodestar's team
 *
 * Original library: Lodestone
 * License: GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 * Original URL: https://github.com/LodestarMC/Lodestone?tab=LGPL-3.0-1-ov-file.
 *
 * Modifications:
 * Some fields where renamed.
 *
 * License Notice:
 * This file is licensed under the GNU Lesser General Public License, version 3.
 * You should have received a copy of the license along with this code.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 */

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(value = EnvType.CLIENT)
public class ShaderHolder {
    public final ResourceLocation shaderLocation;
    public final VertexFormat vertexFormat;

    protected ExtendedShaderInstance shaderInstance;
    public Collection<String> uniformsToCache;
    private final RenderStateShard.ShaderStateShard renderStateShard;

    public ShaderHolder(ShaderInstance shaderInstance) throws IOException {
        Minecraft client = Minecraft.getInstance();
        ResourceLocation shaderLocation = new ResourceLocation("minecraft", shaderInstance.getName());
        VertexFormat vertexFormat = shaderInstance.getVertexFormat();

        ShaderHolder shaderHolder = this;

        this.shaderLocation = shaderLocation;
        this.vertexFormat = vertexFormat;
        this.uniformsToCache = new ArrayList<>();

        for (Uniform uniform : shaderInstance.uniforms) {
            this.uniformsToCache.add(uniform.getName());
        }

        this.shaderInstance = new ExtendedShaderInstance(client.getVanillaPackResources().asProvider(), shaderLocation, vertexFormat) {
            @Override
            public ShaderHolder getShader() {
                return shaderHolder;
            }
        };

        this.renderStateShard = new RenderStateShard.ShaderStateShard(getInstance());
    }

    public ShaderHolder(ResourceLocation shaderLocation, VertexFormat vertexFormat, String... uniformsToCache) {
        this.shaderLocation = shaderLocation;
        this.vertexFormat = vertexFormat;
        this.uniformsToCache = new ArrayList<>(List.of(uniformsToCache));
        this.renderStateShard = new RenderStateShard.ShaderStateShard(getInstance());
    }

    public ExtendedShaderInstance createInstance(ResourceProvider provider) throws IOException {
        ShaderHolder shaderHolder = this;
        ExtendedShaderInstance shaderInstance = new ExtendedShaderInstance(provider, shaderLocation, vertexFormat) {
            @Override
            public ShaderHolder getShader() {
                return shaderHolder;
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

    public static void register(ResourceProvider resourceProvider, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderPairList, ShaderHolder shaderHolderToRegister) throws IOException {
        shaderPairList.add(Pair.of(
                shaderHolderToRegister.createInstance(resourceProvider),
                (shader) -> ((ExtendedShaderInstance) shader).getShader()
        ));
    }
}
