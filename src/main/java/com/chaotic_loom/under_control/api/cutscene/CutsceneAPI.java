package com.chaotic_loom.under_control.api.cutscene;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class CutsceneAPI {
    private static boolean playing = false;
    private static Vector3f position = new Vector3f();
    private static Vector2f rotation = new Vector2f();

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
        position.set(x, y, z);
    }

    public static void setRotation(Vector2f newRotation) {
        rotation = newRotation;
    }

    public static void setRotation(float x, float y) {
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
}
