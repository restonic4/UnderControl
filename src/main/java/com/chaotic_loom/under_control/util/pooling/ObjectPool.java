package com.chaotic_loom.under_control.util.pooling;

import java.util.Stack;
import java.util.function.Supplier;

public class ObjectPool<T extends Poolable> {
    private final Stack<T> pool;
    private final Supplier<T> objectFactory;

    public ObjectPool(Supplier<T> objectFactory) {
        this.objectFactory = objectFactory;
        this.pool = new Stack<>();
    }

    public T acquire() {
        if (pool.isEmpty()) {
            return objectFactory.get();
        }
        return pool.pop();
    }

    public void release(T object) {
        object.reset();
        pool.push(object);
    }

    public int getSize() {
        return pool.size();
    }
}
