package com.chaotic_loom.under_control.registries;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.core.annotations.ExecutionSide;
import com.chaotic_loom.under_control.core.annotations.Packet;
import com.chaotic_loom.under_control.core.annotations.PacketDirection;
import com.chaotic_loom.under_control.core.annotations.Registration;
import com.chaotic_loom.under_control.networking.packets.client_to_server.ServerJoinRequest;
import com.chaotic_loom.under_control.networking.packets.server_to_client.ConnectToServer;
import com.chaotic_loom.under_control.registries.client.UnderControlClientCommands;
import com.chaotic_loom.under_control.registries.client.UnderControlShaders;
import com.chaotic_loom.under_control.registries.common.UnderControlCommonCommands;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class RegistriesManager {
    private static final String RECEIVE_METHOD_NAME = "receive";

    private static final List<String> modPackages = new ArrayList<>();

    public static void startRegistrationAnnotationCollection(ExecutionSide executionSide) {
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

    public static void startPacketAnnotationCollection(PacketDirection packetDirection) {
        Reflections reflections = new Reflections(modPackages);
        Set<Class<?>> packetsFound = reflections.getTypesAnnotatedWith(Packet.class);

        for (Class<?> packetClass : packetsFound) {
            Packet packetAnnotation = packetClass.getAnnotation(Packet.class);

            if (packetAnnotation != null && packetAnnotation.direction() == packetDirection) {
                try {
                    Method getLocationMethod = packetClass.getMethod("getId");

                    if (!Modifier.isStatic(getLocationMethod.getModifiers()) || !ResourceLocation.class.isAssignableFrom(getLocationMethod.getReturnType())) {
                        throw new IllegalStateException(String.format(
                                "Class '%s' must define a static method 'getId' returning ResourceLocation!",
                                packetClass.getName()
                        ));
                    }

                    ResourceLocation packetLocation = (ResourceLocation) getLocationMethod.invoke(null);

                    Method receiveMethod;
                    if (packetDirection == PacketDirection.CLIENT_TO_SERVER) {
                        receiveMethod = packetClass.getDeclaredMethod(
                                "receive",
                                MinecraftServer.class, Player.class, ServerPacketListener.class, FriendlyByteBuf.class, PacketSender.class
                        );
                    } else if (packetDirection == PacketDirection.SERVER_TO_CLIENT) {
                        receiveMethod = packetClass.getDeclaredMethod(
                                "receive",
                                Minecraft.class, ClientPacketListener.class, FriendlyByteBuf.class, PacketSender.class
                        );
                    } else {
                        throw new IllegalStateException("Unsupported packet direction: " + packetDirection);
                    }

                    if (!Modifier.isStatic(receiveMethod.getModifiers())) {
                        throw new IllegalStateException(String.format(
                                "Method '%s' in class '%s' must be static!",
                                RECEIVE_METHOD_NAME, packetClass.getName()
                        ));
                    }

                    if (packetDirection == PacketDirection.CLIENT_TO_SERVER) {
                        UnderControl.LOGGER.info("Packet registered: {} -> {}", packetLocation, packetClass.getName());
                        ServerPlayNetworking.registerGlobalReceiver(packetLocation, (server, player, packetListener, buf, packetSender) -> {
                            try {
                                receiveMethod.invoke(null, server, player, packetListener, buf, packetSender);
                            } catch (Exception e) {
                                throw new RuntimeException("Error invoking receive method for packet: " + packetLocation, e);
                            }
                        });
                    } else if (packetDirection == PacketDirection.SERVER_TO_CLIENT) {
                        UnderControl.LOGGER.info("Packet registered: {} -> {}", packetLocation, packetClass.getName());
                        ClientPlayNetworking.registerGlobalReceiver(packetLocation, (minecraft, clientPacketListener, friendlyByteBuf, packetSender) -> {
                            try {
                                receiveMethod.invoke(null, minecraft, clientPacketListener, friendlyByteBuf, packetSender);
                            } catch (Exception e) {
                                throw new RuntimeException("Error invoking receive method for packet: " + packetLocation, e);
                            }
                        });
                    }
                } catch (NoSuchMethodException e) {
                    System.err.printf("Class '%s' does not define the required methods!%n", packetClass.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void collectModPackages() {
        collectPackagesForEntrypoint("client", ClientModInitializer.class);
        collectPackagesForEntrypoint("main", ModInitializer.class);
        collectPackagesForEntrypoint("server", DedicatedServerModInitializer.class);
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
                }
            } catch (Exception e) {
                UnderControl.LOGGER.error("Could not load the main class {} (ID: {})",  metadata.getName(), modId);
            }
        });
    }

    private static String truncatePackage(String fullPackage, String modId) {
        int modIndex = fullPackage.indexOf(modId);
        return (modIndex != -1) ? fullPackage.substring(0, modIndex + modId.length()) : fullPackage;
    }
}
