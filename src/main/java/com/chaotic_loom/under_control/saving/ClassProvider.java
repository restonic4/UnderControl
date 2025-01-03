package com.chaotic_loom.under_control.saving;

public interface ClassProvider<T> {
    String getIdentifier();
    String serialize(T object);
    T deserialize(String value);
}
