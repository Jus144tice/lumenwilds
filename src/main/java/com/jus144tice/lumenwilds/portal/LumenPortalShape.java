/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Frame detection for a Lumenbound Stone portal.
 *
 * <p>Phase 1 placeholder: defines the frame block and the intended size bounds, and exposes a single
 * {@link #findEmptyPortalShape} seam that currently returns {@link Optional#empty()}. The real
 * implementation will scan from the ignited block for a rectangular {@link ModBlocks#LUMENBOUND_STONE}
 * frame (interior {@link #MIN_WIDTH}×{@link #MIN_HEIGHT} up to {@link #MAX_WIDTH}×{@link #MAX_HEIGHT}),
 * much like {@code net.minecraft.world.level.portal.PortalShape} does for obsidian/Nether portals.</p>
 */
public final class LumenPortalShape {

    /** Interior width bounds (in air blocks), matching Nether-portal conventions. */
    public static final int MIN_WIDTH = 2;

    public static final int MAX_WIDTH = 21;

    /** Interior height bounds (in air blocks). */
    public static final int MIN_HEIGHT = 3;

    public static final int MAX_HEIGHT = 21;

    private LumenPortalShape() {}

    /** True if {@code state} is the required frame material ({@link ModBlocks#LUMENBOUND_STONE}). */
    public static boolean isFrameBlock(BlockState state) {
        return state.is(ModBlocks.LUMENBOUND_STONE.get());
    }

    /**
     * Attempt to locate a valid, empty (air-filled) Lumenbound Stone frame anchored at/near
     * {@code pos}. Returns the discovered shape, or empty if no valid frame exists.
     *
     * <p>TODO (Phase 2): implement the rectangular scan + validation and return a small record
     * describing the interior bounds/axis so {@link LumenPortalManager} can fill it with
     * {@link ModBlocks#LUMEN_PORTAL}. Until then this always returns {@link Optional#empty()} so the
     * striker logs an "attempt" without doing anything unsafe.</p>
     */
    public static Optional<LumenPortalShape> findEmptyPortalShape(LevelAccessor level, BlockPos pos) {
        return Optional.empty();
    }
}
