package com.chaotic_loom.under_control.util.data_holders;

@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {
    void accept(T t, U u, V v, W w);
}
