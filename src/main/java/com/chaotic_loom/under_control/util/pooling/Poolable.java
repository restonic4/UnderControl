package com.chaotic_loom.under_control.util.pooling;

public interface Poolable {
    default void reset() {
        return;
    }
}
