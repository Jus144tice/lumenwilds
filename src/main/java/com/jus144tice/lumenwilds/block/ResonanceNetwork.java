/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

/**
 * The Lumenwright Resonance network (Phase 10e) — the static logic that makes the cities' dead technology
 * flicker back to life. A {@link ResonanceCoreBlockEntity} floods power outward through connected
 * {@link LumenConduitBlock}s (lighting them {@code ACTIVE}); any {@code ancient_door} (an iron-set
 * {@link DoorBlock}) touching a powered conduit or the core opens, and closes when the power is cut.
 *
 * <p>Pure static helpers, server-side, all sets transient — the network is recomputed from the world each
 * core tick (and torn down on core removal), so nothing persists or desyncs. Conduit/door states are set
 * with flag 2 (no neighbour cascade) so the flood can't recurse.</p>
 */
public final class ResonanceNetwork {

    private ResonanceNetwork() {}

    /** Safety cap on a single core's network (bounded flood, avoids runaway worldgen-sized graphs). */
    public static final int MAX_NODES = 160;

    /** How far a Lumen Relay bridges to the next conductor (lets a network jump a wall or open plaza). */
    private static final int RELAY_BRIDGE = 2;

    /**
     * BFS the connected conductor positions reachable from {@code core} — Lumen Conduits (6-connected) plus
     * Lumen Relays, which additionally bridge to other conductors within {@link #RELAY_BRIDGE} blocks.
     */
    public static Set<BlockPos> flood(Level level, BlockPos core) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        for (Direction d : Direction.values()) {
            BlockPos n = core.relative(d);
            if (isConductor(level, n)) {
                queue.add(n);
            }
        }
        while (!queue.isEmpty() && visited.size() < MAX_NODES) {
            BlockPos p = queue.poll();
            if (!visited.add(p)) {
                continue;
            }
            for (Direction d : Direction.values()) {
                BlockPos n = p.relative(d);
                if (!visited.contains(n) && isConductor(level, n)) {
                    queue.add(n);
                }
            }
            // Relays bridge across small gaps to other conductors.
            if (isRelay(level, p)) {
                for (int dx = -RELAY_BRIDGE; dx <= RELAY_BRIDGE; dx++) {
                    for (int dy = -RELAY_BRIDGE; dy <= RELAY_BRIDGE; dy++) {
                        for (int dz = -RELAY_BRIDGE; dz <= RELAY_BRIDGE; dz++) {
                            BlockPos n = p.offset(dx, dy, dz);
                            if (!visited.contains(n) && isConductor(level, n)) {
                                queue.add(n);
                            }
                        }
                    }
                }
            }
        }
        return visited;
    }

    public static boolean isConduit(Level level, BlockPos p) {
        return level.getBlockState(p).getBlock() instanceof LumenConduitBlock;
    }

    private static boolean isRelay(Level level, BlockPos p) {
        return level.getBlockState(p).getBlock() instanceof LumenRelayBlock;
    }

    private static boolean isConductor(Level level, BlockPos p) {
        return isConduit(level, p) || isRelay(level, p);
    }

    /** Set a conduit's state (only if it changed), without a neighbour cascade. */
    public static void setConduit(Level level, BlockPos p, LumenConduitBlock.State state) {
        BlockState bs = level.getBlockState(p);
        if (bs.getBlock() instanceof LumenConduitBlock && bs.getValue(LumenConduitBlock.CONDUIT_STATE) != state) {
            level.setBlock(p, bs.setValue(LumenConduitBlock.CONDUIT_STATE, state), 2);
        }
    }

    private static boolean isCore(Level level, BlockPos p) {
        return level.getBlockState(p).getBlock() instanceof ResonanceCoreBlock;
    }

    private static boolean isActiveConduit(Level level, BlockPos p) {
        BlockState bs = level.getBlockState(p);
        return bs.getBlock() instanceof LumenConduitBlock
                && bs.getValue(LumenConduitBlock.CONDUIT_STATE) == LumenConduitBlock.State.ACTIVE;
    }

    /** A device (door) is powered iff a neighbour is an active conduit or a core. */
    private static boolean adjacentToPower(Level level, BlockPos p) {
        for (Direction d : Direction.values()) {
            BlockPos n = p.relative(d);
            if (isCore(level, n) || isActiveConduit(level, n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Energize the network: set {@code reach} conduits ACTIVE, set the dropped ({@code previous \ reach})
     * conduits DIM, then re-evaluate every door adjacent to any touched conduit or the core.
     */
    public static void energize(Level level, BlockPos core, Set<BlockPos> reach, Set<BlockPos> previous) {
        for (BlockPos p : previous) {
            if (!reach.contains(p)) {
                setConduit(level, p, LumenConduitBlock.State.DIM);
            }
        }
        for (BlockPos p : reach) {
            setConduit(level, p, LumenConduitBlock.State.ACTIVE);
        }
        updateDevices(level, core, reach, previous);
    }

    /** Tear the whole network down (core removed): set every reached conduit DIM and re-evaluate its devices. */
    public static void deenergize(Level level, BlockPos core, Set<BlockPos> reach) {
        for (BlockPos p : reach) {
            setConduit(level, p, LumenConduitBlock.State.DIM);
        }
        updateDevices(level, core, Set.of(), reach);
    }

    /** Re-evaluate every device (ancient door, gravity lens) next to a touched conduit or the core. */
    private static void updateDevices(Level level, BlockPos core, Set<BlockPos> reach, Set<BlockPos> previous) {
        Set<BlockPos> doorLowers = new HashSet<>();
        Set<BlockPos> lenses = new HashSet<>();
        collectDevices(level, core, doorLowers, lenses);
        for (BlockPos p : reach) {
            collectDevices(level, p, doorLowers, lenses);
        }
        for (BlockPos p : previous) {
            collectDevices(level, p, doorLowers, lenses);
        }
        for (BlockPos lower : doorLowers) {
            setDoorOpen(level, lower, adjacentToPower(level, lower) || adjacentToPower(level, lower.above()));
        }
        for (BlockPos lens : lenses) {
            setLensPowered(level, lens, adjacentToPower(level, lens));
        }
    }

    private static void collectDevices(Level level, BlockPos around, Set<BlockPos> doors, Set<BlockPos> lenses) {
        for (Direction d : Direction.values()) {
            BlockPos n = around.relative(d);
            BlockState bs = level.getBlockState(n);
            if (bs.getBlock() instanceof DoorBlock) {
                doors.add(bs.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? n.below() : n);
            } else if (bs.getBlock() instanceof GravityLensBlock) {
                lenses.add(n);
            }
        }
    }

    private static void setLensPowered(Level level, BlockPos p, boolean powered) {
        BlockState bs = level.getBlockState(p);
        if (bs.getBlock() instanceof GravityLensBlock && bs.getValue(GravityLensBlock.POWERED) != powered) {
            level.setBlock(p, bs.setValue(GravityLensBlock.POWERED, powered), 2);
        }
    }

    /** Open/close both halves of the ancient door whose lower half is at {@code lower}. */
    private static void setDoorOpen(Level level, BlockPos lower, boolean open) {
        BlockState lowerState = level.getBlockState(lower);
        if (!(lowerState.getBlock() instanceof DoorBlock)) {
            return;
        }
        if (lowerState.getValue(DoorBlock.OPEN) != open) {
            level.setBlock(lower, lowerState.setValue(DoorBlock.OPEN, open), 2);
        }
        BlockPos upper = lower.above();
        BlockState upperState = level.getBlockState(upper);
        if (upperState.getBlock() instanceof DoorBlock && upperState.getValue(DoorBlock.OPEN) != open) {
            level.setBlock(upper, upperState.setValue(DoorBlock.OPEN, open), 2);
        }
    }
}
