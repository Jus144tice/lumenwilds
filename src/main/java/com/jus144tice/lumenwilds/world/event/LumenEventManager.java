/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.network.LumenEventPayload;
import com.jus144tice.lumenwilds.registry.ModEntities;
import com.jus144tice.lumenwilds.world.LumenBiomeBootstrap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Drives the Lumenwilds' ambient {@link LumenEvent}s (Phase 7d.2): one event active at a time (or
 * {@link LumenEvent#NONE}), chosen on a timer, with per-event server-side effects (boosted spawns) and a
 * client sync (so the sky / particles can react). Ticked once per Lumenwilds tick by {@code event.LumenEventDriver}.
 *
 * <p>State is transient (per server session — ambient events needn't persist a restart); {@link #reset()} is
 * called on server stop. The event timing uses the Lumenwilds' own (half-rate, Phase 7d.1) clock for the
 * night check, so Moonwake aligns with the dimension's night.</p>
 */
public final class LumenEventManager {

    private static final int INITIAL_DELAY = 400; // first roll ~20s after load
    private static final int COOLDOWN_MIN = 1800; // ~1.5 min between events
    private static final int COOLDOWN_MAX = 4800; // ~4 min
    private static final int EVENT_MIN = 800; //   ~40s
    private static final int EVENT_MAX = 2400; //  ~2 min

    private static LumenEvent active = LumenEvent.NONE;
    private static int ticksRemaining = INITIAL_DELAY;

    private LumenEventManager() {}

    public static LumenEvent active() {
        return active;
    }

    public static void reset() {
        active = LumenEvent.NONE;
        ticksRemaining = INITIAL_DELAY;
    }

    /** Advances the schedule and applies the active event's ongoing effects. */
    public static void tick(ServerLevel level) {
        if (active != LumenEvent.NONE) {
            applyOngoing(level);
        }
        if (--ticksRemaining > 0) {
            return;
        }
        if (active != LumenEvent.NONE) {
            // event ended → quiet cooldown
            setActive(level, LumenEvent.NONE, randomBetween(level, COOLDOWN_MIN, COOLDOWN_MAX));
        } else {
            LumenEvent next = roll(level);
            if (next == LumenEvent.NONE) {
                ticksRemaining = randomBetween(level, COOLDOWN_MIN / 2, COOLDOWN_MAX / 2);
            } else {
                setActive(level, next, randomBetween(level, EVENT_MIN, EVENT_MAX));
            }
        }
    }

    private static LumenEvent roll(ServerLevel level) {
        int r = level.getRandom().nextInt(100);
        if (level.isNight() && r < 25) {
            return LumenEvent.MOONWAKE; // calm bright nights, night-only
        }
        if (r < 55) {
            return LumenEvent.SPOREFALL;
        }
        if (r < 80) {
            return LumenEvent.DEEP_HUSH;
        }
        return LumenEvent.NONE; // sometimes nothing happens
    }

    private static void setActive(ServerLevel level, LumenEvent event, int ticks) {
        active = event;
        ticksRemaining = ticks;
        Lumenwilds.LOGGER.info("[lumenwilds] Lumen event → {} for {} ticks", event, ticks);
        PacketDistributor.sendToPlayersInDimension(level, new LumenEventPayload(event.id(), ticks));
    }

    private static int randomBetween(ServerLevel level, int min, int max) {
        return min + level.getRandom().nextInt(Math.max(1, max - min));
    }

    // --- per-event server-side effects (boosted spawns near players) ----------------------------

    private static void applyOngoing(ServerLevel level) {
        long t = level.getGameTime();
        switch (active) {
            case SPOREFALL:
                if (t % 80L == 0L) {
                    for (ServerPlayer player : level.players()) {
                        if (level.getBiome(player.blockPosition()).is(LumenBiomeBootstrap.SPOREFALL_JUNGLE)
                                && countNearby(level, player, ModEntities.SPORELING.get()) < 8) {
                            spawnAtSurface(level, player, ModEntities.SPORELING.get(), 0);
                        }
                    }
                }
                break;
            case MOONWAKE:
                if (t % 120L == 0L) {
                    for (ServerPlayer player : level.players()) {
                        if (countNearby(level, player, ModEntities.LANTERN_BEETLE.get()) < 6) {
                            spawnAtSurface(level, player, ModEntities.LANTERN_BEETLE.get(), 2); // fly a touch above
                        }
                    }
                }
                break;
            case DEEP_HUSH:
                if (t % 160L == 0L) {
                    for (ServerPlayer player : level.players()) {
                        if (player.getBlockY() < 40
                                && !level.canSeeSky(player.blockPosition())
                                && countNearby(level, player, ModEntities.SHADE_STALKER.get()) < 4) {
                            spawnInCave(level, player, ModEntities.SHADE_STALKER.get());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private static int countNearby(ServerLevel level, ServerPlayer player, EntityType<?> type) {
        return level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(24.0), m -> m.getType() == type)
                .size();
    }

    private static void spawnAtSurface(
            ServerLevel level, ServerPlayer player, EntityType<? extends Mob> type, int yOffset) {
        RandomSource rand = level.getRandom();
        int x = player.getBlockX() + (rand.nextBoolean() ? 1 : -1) * (10 + rand.nextInt(10));
        int z = player.getBlockZ() + (rand.nextBoolean() ? 1 : -1) * (10 + rand.nextInt(10));
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos pos = new BlockPos(x, y + yOffset, z);
        if (level.isLoaded(pos)
                && level.getBlockState(pos).isAir()
                && level.getBlockState(pos.above()).isAir()) {
            spawn(level, type, pos);
        }
    }

    private static void spawnInCave(ServerLevel level, ServerPlayer player, EntityType<? extends Mob> type) {
        RandomSource rand = level.getRandom();
        int x = player.getBlockX() + (rand.nextBoolean() ? 1 : -1) * (4 + rand.nextInt(8));
        int z = player.getBlockZ() + (rand.nextBoolean() ? 1 : -1) * (4 + rand.nextInt(8));
        BlockPos pos = new BlockPos(x, player.getBlockY(), z);
        if (level.isLoaded(pos)
                && level.getBlockState(pos).isAir()
                && level.getBlockState(pos.above()).isAir()
                && level.getBlockState(pos.below()).isSolid()) {
            spawn(level, type, pos);
        }
    }

    private static void spawn(ServerLevel level, EntityType<? extends Mob> type, BlockPos pos) {
        Mob mob = type.create(level);
        if (mob == null) {
            return;
        }
        mob.moveTo(
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                level.getRandom().nextFloat() * 360.0F,
                0.0F);
        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
        level.addFreshEntity(mob);
    }
}
