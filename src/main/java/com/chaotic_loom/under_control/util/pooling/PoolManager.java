package com.chaotic_loom.under_control.util.pooling;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PoolManager {
    private static final Map<Class<?>, ObjectPool<?>> pools = new HashMap<>();

    public static <T extends Poolable> void createPool(Class<T> clazz, Supplier<T> factory) {
        pools.put(clazz, new ObjectPool<>(factory));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Poolable> T acquire(Class<T> clazz) {
        ObjectPool<T> pool = (ObjectPool<T>) pools.get(clazz);
        if (pool == null) {
            throw new IllegalStateException("No pool found for class: " + clazz.getName());
        }
        return pool.acquire();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Poolable> void release(T object) {
        ObjectPool<T> pool = (ObjectPool<T>) pools.get(object.getClass());
        if (pool == null) {
            throw new IllegalStateException("No pool found for class: " + object.getClass().getName());
        }
        pool.release(object);
    }
}
