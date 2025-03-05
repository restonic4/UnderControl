package com.chaotic_loom.under_control.api.registry;

import com.chaotic_loom.under_control.client.gui.DynamicScreen;
import com.chaotic_loom.under_control.debug.Debugger;
import com.chaotic_loom.under_control.registries.core.RegistryGroup;
import com.chaotic_loom.under_control.registries.core.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class UnderControlRegistries {
    // List of built-in groups for you to use, yippie!

    public static final RegistryGroup<Debugger> DEBUGGER = new RegistryGroup<>("debugger");

    // Custom key system, where you can register your own groups

    private static final List<RegistryGroup<?>> customGroups = new ArrayList<>();

    public static <T extends RegistryObject> RegistryGroup<T> registerCustomGroup(String id) {
        return registerCustomGroup(id, null);
    }

    public static <T extends RegistryObject> RegistryGroup<T> registerCustomGroup(String id, String rootDirectory) {
        RegistryGroup<T> customGroup = new RegistryGroup<>(id, rootDirectory);
        customGroups.add(customGroup);

        return customGroup;
    }

    public static <T extends RegistryObject> RegistryGroup<T> getCustomGroup(String id) {
        for (RegistryGroup<?> group : customGroups) {
            if (group.getKey().equals(id)) {
                RegistryGroup<T> foundGroup = (RegistryGroup<T>) group;
                return foundGroup;
            }
        }

        return null;
    }
}