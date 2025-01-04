package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.chaotic_loom.under_control.events.EventResult;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class LivingEntityExtraEvents {
    public static final Event<MinecartPushed> MINECART_PUSHED = EventFactory.createArray(MinecartPushed.class, callbacks -> (minecart, pusher) -> {
        for (MinecartPushed callback : callbacks) {
            EventResult result = callback.onMinecartPushed(minecart, pusher);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface MinecartPushed {
        EventResult onMinecartPushed(AbstractMinecart minecart, Entity pusher);
    }

    public static final Event<BeeAngered> BEE_ANGERED = EventFactory.createArray(BeeAngered.class, callbacks -> (livingEntity) -> {
        for (BeeAngered callback : callbacks) {
            EventResult result = callback.onBeeAngered(livingEntity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface BeeAngered {
        EventResult onBeeAngered(LivingEntity livingEntity);
    }

    public static final Event<EntityObstruction> ENTITY_OBSTRUCTION = EventFactory.createArray(EntityObstruction.class, callbacks -> (entity) -> {
        for (EntityObstruction callback : callbacks) {
            EventResult result = callback.onEntityObstruction(entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface EntityObstruction {
        EventResult onEntityObstruction(Entity entity);
    }

    public static final Event<BroadcastToPlayer> BROADCAST_TO_PLAYER = EventFactory.createArray(BroadcastToPlayer.class, callbacks -> (entity, serverPlayer) -> {
        for (BroadcastToPlayer callback : callbacks) {
            EventResult result = callback.onBroadcastToPlayer(entity, serverPlayer);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface BroadcastToPlayer {
        EventResult onBroadcastToPlayer(Entity entity, ServerPlayer serverPlayer);
    }

    public static final Event<InvisibleTo> INVISIBLE_TO = EventFactory.createArray(InvisibleTo.class, callbacks -> (entity, player) -> {
        for (InvisibleTo callback : callbacks) {
            EventResult result = callback.onInvisibleTo(entity, player);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface InvisibleTo {
        EventResult onInvisibleTo(Entity entity, Player player);
    }

    public static final Event<PlaySound> PLAY_SOUND = EventFactory.createArray(PlaySound.class, callbacks -> (entity, soundEvent, f, g) -> {
        for (PlaySound callback : callbacks) {
            EventResult result = callback.onPlaySound(entity, soundEvent, f, g);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface PlaySound {
        EventResult onPlaySound(Entity entity, SoundEvent soundEvent, float f, float g);
    }

    public static final Event<Pushable> PUSHABLE = EventFactory.createArray(Pushable.class, callbacks -> (entity, actor) -> {
        for (Pushable callback : callbacks) {
            EventResult result = callback.onPushable(entity, actor);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface Pushable {
        EventResult onPushable(Entity entity, Entity actor);
    }

    public static final Event<SpawnFallParticles> SPAWN_FALL_PARTICLES = EventFactory.createArray(SpawnFallParticles.class, callbacks -> (livingEntity, level, particleOptions, x, y, z, count, dx, dy, dz, speed) -> {
        for (SpawnFallParticles callback : callbacks) {
            EventResult result = callback.onSpawnFallParticles(livingEntity, level, particleOptions, x, y, z, count, dx, dy, dz, speed);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface SpawnFallParticles {
        EventResult onSpawnFallParticles(LivingEntity livingEntity, ServerLevel level, ParticleOptions particleOptions, double x, double y, double z, int count, double dx, double dy, double dz, double speed);
    }

    public static final Event<CanBeSeen> CAN_BE_SEEN = EventFactory.createArray(CanBeSeen.class, callbacks -> (livingEntity) -> {
        for (CanBeSeen callback : callbacks) {
            EventResult result = callback.onCanBeSeen(livingEntity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface CanBeSeen {
        EventResult onCanBeSeen(LivingEntity livingEntity);
    }

    public static final Event<CausedGameEvent> CAUSED_GAME_EVENT = EventFactory.createArray(CausedGameEvent.class, callbacks -> (sourceEntity, serverLevel, gameEvent, context, vec3) -> {
        for (CausedGameEvent callback : callbacks) {
            EventResult result = callback.onCausedGameEvent(sourceEntity, serverLevel, gameEvent, context, vec3);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface CausedGameEvent {
        EventResult onCausedGameEvent(Entity sourceEntity, ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3);
    }

    public static final Event<WardenPickTarget> WARDEN_PICK_TARGET = EventFactory.createArray(WardenPickTarget.class, callbacks -> (warden, entity) -> {
        for (WardenPickTarget callback : callbacks) {
            EventResult result = callback.onWardenPickTarget(warden, entity);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            } else if (result == EventResult.SUCCEEDED) {
                return EventResult.SUCCEEDED;
            }
        }

        return EventResult.CONTINUE;
    });

    @FunctionalInterface
    public interface WardenPickTarget {
        EventResult onWardenPickTarget(Warden warden, Entity entity);
    }
}
