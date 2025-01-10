package com.chaotic_loom.under_control.registries;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.Registration;
import com.chaotic_loom.under_control.registries.client.UnderControlClientCommands;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.registries.common.UnderControlCommonCommands;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class RegistriesManager {
    private static final List<String> modPackages = new ArrayList<>();

    public static void startRegistrationAnnotationCollection(ExecutionSide executionSide) {
        collectModPackages(executionSide);

        Reflections reflections = new Reflections(modPackages);
        Set<Class<?>> registrarsFound = reflections.getTypesAnnotatedWith(Registration.class);

        for (Class<?> registrar : registrarsFound) {
            Registration annotation = registrar.getAnnotation(Registration.class);

            if (annotation != null && annotation.side() == executionSide) {
                try {
                    Method registerMethod = registrar.getDeclaredMethod("register");

                    if (Modifier.isStatic(registerMethod.getModifiers())) {
                        registerMethod.invoke(null);
                    } else {
                        UnderControl.LOGGER.error("Method 'register' in {} is not static!", registrar.getSimpleName());
                    }
                } catch (NoSuchMethodException e) {
                    UnderControl.LOGGER.error("No 'register' method found in: {}", registrar.getSimpleName());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void collectModPackages(ExecutionSide executionSide) {
        if (executionSide == ExecutionSide.CLIENT) {
            collectPackagesForEntrypoint("client", ClientModInitializer.class);
        } else if (executionSide == ExecutionSide.COMMON) {
            collectPackagesForEntrypoint("main", ModInitializer.class);
        } else if (executionSide == ExecutionSide.SERVER) {
            collectPackagesForEntrypoint("server", DedicatedServerModInitializer.class);
        }
    }

    private static void collectPackagesForEntrypoint(String entrypointName, Class<?> entrypointType) {
        FabricLoader.getInstance().getEntrypointContainers(entrypointName, entrypointType).forEach(entrypoint -> {
            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            String modId = metadata.getId();

            try {
                Class<?> mainClass = entrypoint.getEntrypoint().getClass();
                String mainPackage = mainClass.getPackage().getName();

                String modBasePackage = truncatePackage(mainPackage, modId);

                if (!modPackages.contains(modBasePackage)) {
                    modPackages.add(modBasePackage);
                    System.out.printf("Entrypoint: %s -> Mod ID: %s -> Base Package: %s%n", entrypointName, modId, modBasePackage);
                }
            } catch (Exception e) {
                System.err.printf("Could not load the main class %s (ID: %s)%n", metadata.getName(), modId);
            }
        });
    }

    private static String truncatePackage(String fullPackage, String modId) {
        int modIndex = fullPackage.indexOf(modId);
        return (modIndex != -1) ? fullPackage.substring(0, modIndex + modId.length()) : fullPackage;
    }

    public static List<String> getModPackages() {
        return modPackages;
    }
}
