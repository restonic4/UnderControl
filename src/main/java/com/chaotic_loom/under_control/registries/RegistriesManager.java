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
    public static void startRegistrationAnnotationCollection(ExecutionSide executionSide) {
        List<String> modPackages = collectModPackages(executionSide);

        Reflections reflections = new Reflections(modPackages);
        Set<Class<?>> registrarsFound = reflections.getTypesAnnotatedWith(Registration.class);

        for (Class<?> registrar : registrarsFound) {
            Registration annotation = registrar.getAnnotation(Registration.class);

            if (annotation != null && annotation.side() == executionSide) {
                try {
                    Method registerMethod = registrar.getDeclaredMethod("register");

                    if (Modifier.isStatic(registerMethod.getModifiers())) {
                        UnderControl.LOGGER.info("Executing registrar: {}", registrar.getName());
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

    public static List<String> collectModPackages(ExecutionSide executionSide) {
        if (executionSide == ExecutionSide.CLIENT) {
            return collectPackagesForEntrypoint("client", ClientModInitializer.class);
        } else if (executionSide == ExecutionSide.COMMON) {
            return collectPackagesForEntrypoint("main", ModInitializer.class);
        } else if (executionSide == ExecutionSide.SERVER) {
            return collectPackagesForEntrypoint("server", DedicatedServerModInitializer.class);
        }

        return new ArrayList<>();
    }

    private static List<String> collectPackagesForEntrypoint(String entrypointName, Class<?> entrypointType) {
        List<String> modPackages = new ArrayList<>();

        FabricLoader.getInstance().getEntrypointContainers(entrypointName, entrypointType).forEach(entrypoint -> {
            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            String modId = metadata.getId();

            try {
                Class<?> mainClass = entrypoint.getEntrypoint().getClass();
                String mainPackage = mainClass.getPackage().getName();

                String modBasePackage = truncatePackage(mainPackage, modId);

                if (!modPackages.contains(modBasePackage)) {
                    modPackages.add(modBasePackage);
                }
            } catch (Exception e) {
                UnderControl.LOGGER.error("Could not load the main class {} (ID: {})",  metadata.getName(), modId);
            }
        });

        return modPackages;
    }

    private static String truncatePackage(String fullPackage, String modId) {
        int modIndex = fullPackage.indexOf(modId);
        return (modIndex != -1) ? fullPackage.substring(0, modIndex + modId.length()) : fullPackage;
    }
}
