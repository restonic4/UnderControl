package com.chaotic_loom.under_control.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class ClientCacheData {
    public static boolean shouldRenderWireframe = false;
    public static boolean isRenderCommandAllowedOnServer = true;
}
