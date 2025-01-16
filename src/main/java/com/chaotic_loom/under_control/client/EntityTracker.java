package com.chaotic_loom.under_control.client;

import com.chaotic_loom.under_control.util.data_holders.QuadConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Environment(value = EnvType.CLIENT)
public class EntityTracker {
    private static final List<Class<? extends Entity>> entityTypesToTrack = new ArrayList<>();
    private static final List<Entity> trackedEntities = new ArrayList<>();
    private static final List<QuadConsumer<PoseStack, Matrix4f, Camera, Entity>> consumers = new ArrayList<>();

    public static void trackEntityType(Class<? extends Entity> entity) {
        if (!entityTypesToTrack.contains(entity)) {
            entityTypesToTrack.add(entity);
        }
    }

    public static void trackEntity(Entity entity) {
        if (!trackedEntities.contains(entity) && existsInClientWorld(entity)) {
            trackedEntities.add(entity);
        }
    }

    public static void discardEntity(Entity entity) {
        trackedEntities.remove(entity);
    }

    public static void addConsumer(QuadConsumer<PoseStack, Matrix4f, Camera, Entity> consumer) {
        consumers.add(consumer);
    }

    public static void render(PoseStack poseStack, Matrix4f matrix4f, Camera camera) {
        Iterator<Entity> iterator = trackedEntities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            if (!existsInClientWorld(entity)) {
                iterator.remove();
                continue;
            }

            for (QuadConsumer<PoseStack, Matrix4f, Camera, Entity> consumer : consumers) {
                consumer.accept(poseStack, matrix4f, camera, entity);
            }
        }
    }

    private static boolean existsInClientWorld(Entity itemEntity) {
        if (itemEntity == null) return false;
        return Minecraft.getInstance().level != null && Minecraft.getInstance().level.getEntity(itemEntity.getId()) instanceof Entity;
    }

    public static boolean shouldTrackEntity(Entity entity) {
        for (Class<? extends Entity> clazz : entityTypesToTrack) {
            if (clazz.isAssignableFrom(entity.getClass())) {
                return true;
            }
        }

        return false;
    }
}
