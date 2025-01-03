package com.chaotic_loom.under_control.vanish;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.saving.SavingAPI;
import com.chaotic_loom.under_control.api.vanish.VanishAPI;
import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.BlockEvents;
import com.chaotic_loom.under_control.events.types.LivingEntityExtraEvents;
import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.chaotic_loom.under_control.saving.SavingProvider;
import com.chaotic_loom.under_control.saving.custom.VanishList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;

public class VanishManager {
    public static void registerServerEvents() {
        //UnderControl.extraInfo("Registering a ton of events, idk, like 1 million or so");

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (VanishAPI.isVanished(sender)) {
                sender.sendSystemMessage(Component.literal("IMPLEMENTATION NEEDED").withStyle(ChatFormatting.RED));
                return false;
            } else {
                return true;
            }
        });

        OtherEvents.PLAYERS_FOUND_BY_COMMAND.register((playerList, css) -> {
            ServerPlayer observer = css.getPlayer();

            if (observer != null) {
                playerList.removeIf((actor) -> !VanishAPI.canSeePlayer(actor, observer));
            }
        });

        OtherEvents.BROADCASTING_TO_ADMINS.register((entity, observer, component) -> {
            if (entity instanceof ServerPlayer actor && VanishAPI.isVanished(actor)) {
                VanishAPI.sendHiddenMessage(actor, observer, component);
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        BlockEvents.CONTAINER_ANIMATED.register((player, isOpening) -> {
            if (player instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        LivingEntityExtraEvents.ENTITY_COLLISIONS.register((entity, actor, aabb, predicate) -> {
            if (VanishAPI.isVanished(actor)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.ENTITY_OBSTRUCTION.register((entity) -> {
            if (entity instanceof ServerPlayer serverPlayer && VanishAPI.isVanished(serverPlayer)) {
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        LivingEntityExtraEvents.BROADCAST_TO_PLAYER.register((entity, observer) -> {
            ServerPlayer actor;

            if (entity instanceof ServerPlayer player) {
                actor = player;
            } else if (entity instanceof TraceableEntity traceableEntity && traceableEntity.getOwner() instanceof ServerPlayer owner) {
                actor = owner;
            } else {
                return EventResult.CONTINUE;
            }

            if (!VanishAPI.canSeePlayer(actor, observer)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.INVISIBLE_TO.register((entity, player) -> {
            if (entity instanceof ServerPlayer actor && player instanceof ServerPlayer observer && !VanishAPI.canSeePlayer(actor, observer)) {
                return EventResult.SUCCEEDED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.PLAY_SOUND.register((entity, soundEvent, f, g) -> {
            ServerPlayer actor;

            if (entity instanceof ServerPlayer player) {
                actor = player;
            } else if (entity instanceof TraceableEntity traceableEntity && traceableEntity.getOwner() instanceof ServerPlayer owner) {
                actor = owner;
            } else {
                return EventResult.CONTINUE;
            }

            if (VanishAPI.isVanished(actor)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.PUSHABLE.register((entity, actor, predicate) -> {
            if (VanishAPI.isVanished(actor)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        OtherEvents.PLAYER_LIST_INFO_REQUIRED.register((playerList, observer) -> {
            return VanishAPI.getVisiblePlayers(observer);
        });

        ServerPlayerExtraEvents.MSG.register((css, collection, playerChatMessage) -> {
            ServerPlayer player = css.getPlayer();
            if (player == null) return EventResult.CANCELED;;

            if (VanishAPI.isVanished(player)) {
                player.sendSystemMessage(Component.translatable("argument.entity.notfound.player").withStyle(ChatFormatting.RED));
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        ServerPlayerExtraEvents.ADVANCEMENT_MESSAGE.register((serverPlayer, playerList, component, bl) -> {
            if (VanishAPI.isVanished(serverPlayer)) {
                VanishAPI.broadcastHiddenMessage(serverPlayer, component);
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        ServerPlayerExtraEvents.STARTED_JOINING.register((connection, actor) -> {
            SavingProvider savingProvider = SavingAPI.getWorldProvider(UnderControl.MOD_ID);

            actor.server.execute(() -> {
                VanishList vanishList = savingProvider.get("vanish_list", VanishList.class);
                if (vanishList != null && vanishList.contains(actor)) {
                    VanishAPI.vanish(actor);
                }
            });
        });

        ServerPlayerExtraEvents.JOIN_MESSAGE.register((playerList, component, bl, connection, actor) -> {
            if (VanishAPI.isVanished(actor)) {
                VanishAPI.broadcastHiddenMessage(actor, component);
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        ServerPlayerExtraEvents.LEAVE_MESSAGE.register((playerList, component, bl, actor) -> {
            if (VanishAPI.isVanished(actor)) {
                VanishAPI.broadcastHiddenMessage(actor, component);
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });


        ServerPlayerExtraEvents.DEATH_MESSAGE.register((playerList, player, component, actor) -> {
            if (VanishAPI.isVanished(actor)) {
                VanishAPI.broadcastHiddenMessage(actor, component);
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        ServerPlayerExtraEvents.GAME_EVENT.register((packetListener, packet, player) -> {
            Entity entity;

            if (player instanceof ServerPlayer serverPlayer) {
                entity = serverPlayer;
            } else {
                entity = VanishAPI.ACTIVE_ENTITY.get();
            }

            if (entity instanceof TraceableEntity traceableEntity && traceableEntity.getOwner() instanceof ServerPlayer owner) {
                entity = owner;
            }

            if (entity instanceof ServerPlayer actor) {
                return VanishAPI.canSeePlayer(actor, packetListener.player) ? EventResult.SUCCEEDED : EventResult.CANCELED;
            } else {
                return EventResult.SUCCEEDED;
            }
        });

        ServerPlayerExtraEvents.SWEEPING_EDGE_ATTACK.register((livingEntity, aabb) -> {
            if (VanishAPI.isVanished(livingEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.SUCCEEDED;
        });

        ServerPlayerExtraEvents.INVULNERABILITY_TO.register((player, original) -> {
            if (VanishAPI.isVanished(player)) {
                return EventResult.SUCCEEDED;
            }

            return EventResult.CONTINUE;
        });

        OtherEvents.SERVER_SENT_PACKET.register((unknownListener, packet, packetSendListener, server) -> {
            if (unknownListener instanceof ServerGamePacketListenerImpl listener) {
                if (packet instanceof ClientboundTakeItemEntityPacket takeItemEntityPacket) {
                    Entity entity = listener.player.level().getEntity(takeItemEntityPacket.getPlayerId());

                    if (entity instanceof ServerPlayer actor && !VanishAPI.canSeePlayer(actor, listener.player)) {
                        listener.send(new ClientboundRemoveEntitiesPacket(takeItemEntityPacket.getItemId()));
                        return EventResult.CANCELED;
                    }
                } else if (packet instanceof ClientboundPlayerInfoUpdatePacket playerInfoPacket) {
                    ObjectArrayList<ServerPlayer> modifiedEntries = new ObjectArrayList<>();
                    int visible = 0;

                    for (ClientboundPlayerInfoUpdatePacket.Entry playerUpdate : playerInfoPacket.entries()) {
                        ServerPlayer player = server.getPlayerList().getPlayer(playerUpdate.profileId());
                        if (VanishAPI.canSeePlayer(player, listener.player)) {
                            visible++;
                            if (player != null) modifiedEntries.add(player);
                        }
                    }
                    if (visible != playerInfoPacket.entries().size()) {
                        if (!modifiedEntries.isEmpty()) {
                            listener.send(new ClientboundPlayerInfoUpdatePacket(playerInfoPacket.actions(), modifiedEntries));
                        }
                        return EventResult.CANCELED;
                    }
                }
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.DESTROY_PROGRESS_BROADCAST.register((packetListener, packet, entity) -> {
            if (!(entity instanceof ServerPlayer player) || VanishAPI.canSeePlayer(player, packetListener.player)) {
                return EventResult.SUCCEEDED;
            }

            return EventResult.CANCELED;
        });

        ServerPlayerExtraEvents.SLEEPING_COUNT.register((serverPlayer) -> {
            return VanishAPI.isVanished(serverPlayer) ? EventResult.CANCELED : EventResult.SUCCEEDED;
        });

        LivingEntityExtraEvents.CAUSED_GAME_EVENT.register((sourceEntity, serverLevel, gameEvent, context, vec3) -> {
            if (VanishAPI.isVanished(sourceEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.WARDEN_PICK_TARGET.register((warden, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.MINECART_PUSHED.register((abstractMinecart, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.ARMOR_DISPENSED_ON_ENTITY.register((livingEntity) -> {
            if (VanishAPI.isVanished(livingEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.PRESSURE_PLATE_PRESSED.register((level, blockState, blockPos, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.BEE_ANGERED.register((livingEntity) -> {
            if (VanishAPI.isVanished(livingEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.DRIPLEAF_PRESSED.register((level, blockState, blockPos, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.FARM_TRAMPLE.register((level, blockState, blockPos, entity, fallHeight) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.PUSHING.register((livingEntity) -> {
            if (VanishAPI.isVanished(livingEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.SPAWN_FALL_PARTICLES.register((livingEntity, level, particleOptions, x, y, z, count, dx, dy, dz, speed) -> {
            if (VanishAPI.isVanished(livingEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        LivingEntityExtraEvents.CAN_BE_SEEN.register((livingEntity) -> {
            if (VanishAPI.isVanished(livingEntity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        ServerPlayerExtraEvents.ADDED_TO_MAP.register((mapItemSavedData, holdingPlayer) -> {
            if (VanishAPI.isVanished(holdingPlayer.player)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        ServerPlayerExtraEvents.VILLAGE_RAID_STARTED.register((serverPlayer, level) -> {
            if (VanishAPI.isVanished(serverPlayer)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        ServerPlayerExtraEvents.TOUCH_ENTITY.register((player, entity) -> {
            if (VanishAPI.isVanished(player)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        ServerPlayerExtraEvents.CAN_BE_HIT_BY_PROJECTILES.register((player) -> {
            if (VanishAPI.isVanished(player)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.RED_STONE_ORE_PRESSED.register((level, blockState, blockPos, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.SCULK_LIKE_STEPPED.register((level, blockState, blockPos, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.TRIP_WIRE_PRESSED.register((level, blockState, blockPos, entity) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        BlockEvents.TURTLE_EGG_TRAMPLE.register((level, blockState, blockPos, entity, fallHeight) -> {
            if (VanishAPI.isVanished(entity)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });

        ServerPlayerExtraEvents.TELLRAW_RECEIVED.register((sender, serverPlayer, commandContext, command) -> {
            if (!VanishAPI.canSeePlayer(sender, serverPlayer)) {
                return EventResult.CANCELED;
            }

            return EventResult.CONTINUE;
        });
    }
}
