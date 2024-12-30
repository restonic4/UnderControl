package com.restonic4.under_control.saving;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;

public abstract class VanillaSerializableTypes {
    public static class BlockPos implements ClassProvider<net.minecraft.core.BlockPos> {
        int x, y, z;

        public BlockPos() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public BlockPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockPos(net.minecraft.core.BlockPos blockPos) {
            this.x = blockPos.getX();
            this.y = blockPos.getY();
            this.z = blockPos.getZ();
        }

        @Override
        public String getIdentifier() {
            return "BlockPos";
        }

        @Override
        public String serialize(net.minecraft.core.BlockPos object) {
            return object.getX() + "," + object.getY() + "," + object.getZ();
        }

        @Override
        public net.minecraft.core.BlockPos deserialize(String data) {
            String[] axis = data.split(",");

            return new net.minecraft.core.BlockPos(Integer.parseInt(axis[0]), Integer.parseInt(axis[1]), Integer.parseInt(axis[2]));
        }
    }
}
