package com.chaotic_loom.under_control.api.registry;

import com.chaotic_loom.under_control.debug.Debugger;
import com.chaotic_loom.under_control.registries.core.ObjectIdentifier;
import com.chaotic_loom.under_control.registries.core.RegistryGroup;
import com.chaotic_loom.under_control.registries.core.RegistryObject;

import java.util.*;

public class UnderControlRegistry {
    public static final String VALID_NAMESPACE_CHARS = "_-abcdefghijklmnopqrstuvwxyz0123456789.";
    public static final String VALID_PATH_CHARS = VALID_NAMESPACE_CHARS + "/";

    private static final Map<RegistryGroup<?>, Map<ObjectIdentifier, ?>> registries = new HashMap<>();
    private static final Map<RegistryGroup<?>, Map<ObjectIdentifier, ?>> redirects = new HashMap<>();

    public static <T extends RegistryObject> T redirect(RegistryGroup<T> registryGroup, ObjectIdentifier objectIdentifier, T object) {
        Map<ObjectIdentifier, T> registry = getOrCreateRegistry(registryGroup);
        registry.put(objectIdentifier, object);

        redirects.put(registryGroup, registry);

        return object;
    }

    @SuppressWarnings("unchecked")
    public static <T extends RegistryObject> T register(RegistryGroup<T> registryGroup, ObjectIdentifier objectIdentifier, T object) {
        Map<ObjectIdentifier, T> registry = getOrCreateRegistry(registryGroup);

        objectIdentifier.setRegistryGroup(registryGroup);

        if (registry.containsKey(objectIdentifier)) {
            throw new IllegalArgumentException("Duplicate object identifier: " + objectIdentifier);
        }

        Map<ObjectIdentifier, T> redirectedRegistry = (Map<ObjectIdentifier, T>) redirects.get(registryGroup);
        if (redirectedRegistry != null && redirectedRegistry.containsKey(objectIdentifier)) {
            object = redirectedRegistry.get(objectIdentifier);
            System.out.println("Redirected " + registryGroup.toString() + "/" + objectIdentifier);
        }

        object.setObjectIdentifier(objectIdentifier);
        object.onPopulate();

        registry.put(objectIdentifier, object);

        return object;
    }

    public static <T extends RegistryObject> T getRegistryObject(RegistryGroup<T> registryGroup, ObjectIdentifier objectIdentifier) {
        Map<ObjectIdentifier, T> registry = getRegistry(registryGroup);

        if (registry == null) return null;

        return registry.get(objectIdentifier);
    }

    @SuppressWarnings("unchecked")
    private static <T extends RegistryObject> Map<ObjectIdentifier, T> getOrCreateRegistry(RegistryGroup<T> registryGroup) {
        return (Map<ObjectIdentifier, T>) registries.computeIfAbsent(registryGroup, k -> new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends RegistryObject> Map<ObjectIdentifier, T> getRegistry(RegistryGroup<T> registryGroup) {
        return (Map<ObjectIdentifier, T>) registries.get(registryGroup);
    }

    public static <T extends RegistryObject> List<T> getRegistryValues(RegistryGroup<T> registryGroup) {
        Map<ObjectIdentifier, T> registries = UnderControlRegistry.getRegistry(registryGroup);

        if (registries == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(registries.values());
    }

    public static <T extends RegistryObject> boolean isNamespaceLoaded(String id) {
        for (Map.Entry<RegistryGroup<?>, Map<ObjectIdentifier, ?>> data : registries.entrySet()) {
            Map<ObjectIdentifier, ?> map = data.getValue();

            for (Map.Entry<ObjectIdentifier, ?> registryData : map.entrySet()) {
                ObjectIdentifier objectIdentifier = registryData.getKey();

                if (Objects.equals(objectIdentifier.getNamespace(), id)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Map<RegistryGroup<?>, Map<ObjectIdentifier, ?>> getRegistries() {
        return registries;
    }
}
