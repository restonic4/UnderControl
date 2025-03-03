package com.chaotic_loom.under_control.registries.core;

import com.chaotic_loom.under_control.api.registry.UnderControlRegistry;
import com.chaotic_loom.under_control.util.StringHelper;

import java.util.Objects;

//This is used to locate assets
public class ObjectIdentifier {
    private String namespace;
    private String path;
    private RegistryGroup<?> registryGroup;

    public ObjectIdentifier(String namespace, String path) {
        setUp(namespace, path);
    }

    public ObjectIdentifier(String compressed) {
        if (!compressed.contains(":")) {
            throw new IllegalStateException("Illegal compressed object identifier. It should be like: \"namespace:path\"; But we found: " + compressed);
        }

        String[] parts = compressed.split(":");

        if (parts.length < 2) {
            throw new IllegalStateException("Illegal compressed object identifier. It is missing a part. It should be like: \"namespace:path\"; But we found: " + compressed);
        }

        setUp(parts[0], parts[1]);
    }

    private void setUp(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;

        if (!isValidNamespace(namespace)) {
            throw new IllegalStateException("Illegal namespace character in: " + this);
        }

        if (!isValidNPath(namespace)) {
            throw new IllegalStateException("Illegal path character in: " + this);
        }
    }

    public static boolean isValidNamespace(String string) {
        return isValidString(string, UnderControlRegistry.VALID_NAMESPACE_CHARS);
    }

    public static boolean isValidNPath(String string) {
        return isValidString(string, UnderControlRegistry.VALID_PATH_CHARS);
    }

    private static boolean isValidString(String string, String validChars) {
        for (int i = 0; i < string.length(); ++i) {
            if (validChars.indexOf(string.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    public long getMemorySize() {
        long size = 8;
        size += 3 * 8;

        size += StringHelper.getMemorySize(getNamespace());
        size += StringHelper.getMemorySize(getPath());
        size += (getRegistryGroup() != null) ? getRegistryGroup().getMemorySize() : 0;

        return size;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ObjectIdentifier that = (ObjectIdentifier) obj;
        return namespace.equals(that.namespace) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public void setRegistryGroup(RegistryGroup<?> registryGroup) {
        this.registryGroup = registryGroup;
    }

    public RegistryGroup<?> getRegistryGroup() {
        return this.registryGroup;
    }
}