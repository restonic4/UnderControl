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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Environment(value = EnvType.CLIENT)
public abstract class ExtendedShaderInstance extends ShaderInstance {

    protected Map<String, Consumer<Uniform>> defaultUniformData;

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ResourceLocation location, VertexFormat pVertexFormat) throws IOException {
        super(pResourceProvider, location.toString(), pVertexFormat);
    }

    public void setUniformDefaults() {
        for (Map.Entry<String, Consumer<Uniform>> defaultDataEntry : getDefaultUniformData().entrySet()) {
            final Uniform t = super.uniformMap.get(defaultDataEntry.getKey());
            defaultDataEntry.getValue().accept(t);
            float f = 0;
        }
    }

    public abstract ShaderHolder getShader();

    public Map<String, Consumer<Uniform>> getDefaultUniformData() {
        if (defaultUniformData == null) {
            defaultUniformData = new HashMap<>();
        }
        return defaultUniformData;
    }

    @Override
    public void parseUniformNode(JsonElement pJson) throws ChainedJsonException {
        super.parseUniformNode(pJson);

        JsonObject jsonobject = GsonHelper.convertToJsonObject(pJson, "uniform");
        String uniformName = GsonHelper.getAsString(jsonobject, "name");
        if (getShader().uniformsToCache.contains(uniformName)) {
            Uniform uniform = super.uniforms.get(super.uniforms.size() - 1);

            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                final IntBuffer buffer = uniform.getIntBuffer();
                buffer.position(0);
                int[] array = new int[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            } else {
                final FloatBuffer buffer = uniform.getFloatBuffer();
                buffer.position(0);
                float[] array = new float[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            }

            getDefaultUniformData().put(uniformName, consumer);
        }
    }
}
