package com.restonic4.under_control.saving;

public interface ClassProvider<T> {
    String getIdentifier();
    String serialize(T object);
    T deserialize(String value);
}
