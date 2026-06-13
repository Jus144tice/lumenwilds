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

    /** BFS the connected {@link LumenConduitBlock} positions reachable from {@code core} (6-connected). */
    public static Set<BlockPos> flood(Level level, BlockPos core) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        for (Direction d : Direction.values()) {
            BlockPos n = core.relative(d);
            if (isConduit(level, n)) {
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
                if (!visited.contains(n) && isConduit(level, n)) {
                    queue.add(n);
                }
            }
        }
        return visited;
    }

    public static boolean isConduit(Level level, BlockPos p) {
        return level.getBlockState(p).getBlock() instanceof LumenConduitBlock;
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
        updateDoors(level, core, reach, previous);
    }

    /** Tear the whole network down (core removed): set every reached conduit DIM and re-close its doors. */
    public static void deenergize(Level level, BlockPos core, Set<BlockPos> reach) {
        for (BlockPos p : reach) {
            setConduit(level, p, LumenConduitBlock.State.DIM);
        }
        updateDoors(level, core, Set.of(), reach);
    }

    /** Re-evaluate doors next to any conduit in {@code reach}/{@code previous} or next to the core. */
    private static void updateDoors(Level level, BlockPos core, Set<BlockPos> reach, Set<BlockPos> previous) {
        Set<BlockPos> doorLowers = new HashSet<>();
        collectDoors(level, core, doorLowers);
        for (BlockPos p : reach) {
            collectDoors(level, p, doorLowers);
        }
        for (BlockPos p : previous) {
            collectDoors(level, p, doorLowers);
        }
        for (BlockPos lower : doorLowers) {
            boolean powered = adjacentToPower(level, lower) || adjacentToPower(level, lower.above());
            setDoorOpen(level, lower, powered);
        }
    }

    private static void collectDoors(Level level, BlockPos around, Set<BlockPos> out) {
        for (Direction d : Direction.values()) {
            BlockPos n = around.relative(d);
            BlockState bs = level.getBlockState(n);
            if (bs.getBlock() instanceof DoorBlock) {
                out.add(bs.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? n.below() : n);
            }
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
