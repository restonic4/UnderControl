package com.chaotic_loom.under_control.util;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper {
    public static float randomBetween(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("The min value can't be bigger than the max value.");
        }
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }

    public static int randomBetween(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("The min value can't be bigger than the max value.");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static float randomBetween(float value) {
        return ThreadLocalRandom.current().nextFloat()  * (value - (-value)) + (-value);
    }

    public static int randomBetween(int value) {
        return ThreadLocalRandom.current().nextInt(-value, value + 1);
    }

    public static <T> T getRandomFromArray(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(array.length);
        return array[index];
    }

    public static <T> T getRandomFromTwo(T obj1, T obj2) {
        return ThreadLocalRandom.current().nextBoolean() ? obj1 : obj2;
    }

    public static <T> T getRandomFromThree(T obj1, T obj2, T obj3) {
        int index = ThreadLocalRandom.current().nextInt(3);
        return switch (index) {
            case 0 -> obj1;
            case 1 -> obj2;
            default -> obj3;
        };
    }

    public static <T> T getRandomFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty.");
        }
        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }
}
