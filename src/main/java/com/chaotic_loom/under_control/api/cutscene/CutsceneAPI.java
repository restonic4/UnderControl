package com.chaotic_loom.under_control.api.cutscene;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CutsceneAPI {
    private static boolean playing = false;
    private static Vector3f position = null;
    private static Vector2f rotation = null;

    public static void play() {
        playing = true;
    }

    public static void stop() {
        playing = false;
    }

    public static void setPosition(Vector3f newPosition) {
        position = newPosition;
    }

    public static void setPosition(float x, float y, float z) {
        createPositionIfNull();
        position.set(x, y, z);
    }

    public static void setRotation(Vector2f newRotation) {
        rotation = newRotation;
    }

    public static void setRotation(float x, float y) {
        createRotationIfNull();
        rotation.set(x, y);
    }

    public static Vector3f getPosition() {
        return position;
    }

    public static Vector2f getRotation() {
        return rotation;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void clear() {
        position = null;
        rotation = null;
    }

    public static void createPositionIfNull() {
        if (position == null) {
            position = new Vector3f();
        }
    }

    public static void createRotationIfNull() {
        if (rotation == null) {
            rotation = new Vector2f();
        }
    }

    public static float[] calculateCameraRotation(float targetX, float targetY, float targetZ) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        // Posición actual de la cámara (evita usar Vector3f si no es necesario)
        Vec3 cameraPos = camera.getPosition();

        // Vector desde la cámara al objetivo
        double dx = targetX - cameraPos.x;
        double dy = targetY - cameraPos.y;
        double dz = targetZ - cameraPos.z;

        // Cálculo horizontal (yaw)
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz)); // ¡Clave el signo negativo!

        // Cálculo vertical (pitch)
        float pitch = (float) Math.toDegrees(Math.atan2(-dy, horizontalDistance));

        // Asegurar ángulos dentro del rango de Minecraft
        yaw = Mth.wrapDegrees(yaw);
        pitch = Mth.wrapDegrees(pitch);

        return new float[]{yaw, pitch};
    }
}
