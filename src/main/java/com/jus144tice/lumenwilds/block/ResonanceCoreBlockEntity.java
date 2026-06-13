/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Drives a Resonance Core's network (Phase 10e). Every {@link #TICK_INTERVAL} server ticks it floods power
 * through the connected Lumen Conduits and opens any ancient doors they touch (see {@link ResonanceNetwork}).
 * {@link #powered} is transient — the network is recomputed from the world each tick, so it always reflects
 * the current conduit layout (cuts drop downstream conduits back to dim) and nothing is persisted.
 */
public class ResonanceCoreBlockEntity extends BlockEntity {

    private static final int TICK_INTERVAL = 20;

    private Set<BlockPos> powered = new HashSet<>();

    public ResonanceCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESONANCE_CORE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResonanceCoreBlockEntity be) {
        if ((level.getGameTime() + (pos.getX() & 15)) % TICK_INTERVAL != 0) {
            return; // staggered by position so many cores don't all flood on the same tick
        }
        Set<BlockPos> reach = ResonanceNetwork.flood(level, pos);
        ResonanceNetwork.energize(level, pos, reach, be.powered);
        be.powered = reach;
    }

    /** Called from the block on removal so the network goes dark and its doors re-close. */
    public void shutdown(Level level) {
        Set<BlockPos> reach = ResonanceNetwork.flood(level, this.getBlockPos());
        ResonanceNetwork.deenergize(level, this.getBlockPos(), reach);
        this.powered.clear();
    }
}
