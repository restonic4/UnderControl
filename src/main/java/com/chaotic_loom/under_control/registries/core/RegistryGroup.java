package com.chaotic_loom.under_control.registries.core;

import com.chaotic_loom.under_control.util.StringHelper;

public class RegistryGroup<T> {
    private final String key, rootDirectory;

    public RegistryGroup(String key) {
        this.key = key;
        this.rootDirectory = null;
    }

    public RegistryGroup(String key, String rootDirectory) {
        this.key = key;
        this.rootDirectory = rootDirectory;
    }

    public String getKey() {
        return key;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public long getMemorySize() {
        long size = 8;
        size += 3 * 8;

        size += StringHelper.getMemorySize(getKey());
        size += StringHelper.getMemorySize(getRootDirectory());

        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistryGroup<?> that = (RegistryGroup<?>) o;
        return key.equals(that.key) && rootDirectory.equals(that.rootDirectory);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
