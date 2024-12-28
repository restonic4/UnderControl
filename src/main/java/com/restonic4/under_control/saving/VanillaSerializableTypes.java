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
        net.minecraft.core.BlockPos blockPos = null;

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

        public net.minecraft.core.BlockPos getBlockPos() {
            if (this.blockPos == null) {
                this.blockPos = new net.minecraft.core.BlockPos(this.x, this.y, this.z);
            }

            return this.blockPos;
        }

        public void applyChanges() {
            this.x = this.blockPos.getX();
            this.y = this.blockPos.getY();
            this.z = this.blockPos.getZ();
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

            this.x = Integer.parseInt(axis[0]);
            this.y = Integer.parseInt(axis[1]);
            this.z = Integer.parseInt(axis[2]);

            return this.getBlockPos();
        }
    }
}
