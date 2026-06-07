/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * High-level orchestrator for Lumenbound Stone portals: igniting a frame (validate + fill interior) and,
 * on the destination side, locating or building the matching return portal.
 *
 * <p>We can't reuse vanilla {@code PortalForcer} — it searches the {@code nether_portal} POI and builds
 * obsidian frames. So this class does its own find-or-build for {@link ModBlocks#LUMEN_PORTAL} interiors
 * inside {@link ModBlocks#LUMENBOUND_STONE} frames.</p>
 */
public final class LumenPortalManager {

    /** Interior dimensions of an auto-built return portal (frame footprint is +2 in each direction). */
    private static final int BUILT_INTERIOR_WIDTH = 2;

    private static final int BUILT_INTERIOR_HEIGHT = 3;

    /** Search box (half-extents) for an existing return portal near the scaled destination. */
    private static final int SEARCH_RADIUS_HORIZONTAL = 8;

    private static final int SEARCH_RADIUS_VERTICAL = 48;

    private LumenPortalManager() {}

    /**
     * A located/constructed exit portal: the bottom-left interior block of the opening, plus its axis and
     * interior size, which {@link LumenPortalTeleporter} uses to place the arriving entity.
     */
    public record ExitPortal(BlockPos bottomLeftInterior, Direction.Axis axis, int width, int height) {}

    /**
     * Attempt to ignite a portal whose interior contains the (air) {@code seed} block. Validates a
     * Lumenbound Stone frame around it, fills the interior with {@link ModBlocks#LUMEN_PORTAL}, and plays
     * an ignition sound.
     *
     * @return {@code true} if a valid empty frame was found and filled.
     */
    public static boolean tryActivatePortal(Level level, BlockPos seed) {
        if (level.isClientSide) {
            return false;
        }
        Optional<LumenPortalShape> shape = LumenPortalShape.findEmptyPortalShape(level, seed, Direction.Axis.X);
        if (shape.isEmpty()) {
            Lumenwilds.LOGGER.debug(
                    "[{}] No valid empty Lumenbound Stone frame around seed {}.", Lumenwilds.MOD_ID, seed);
            return false;
        }
        shape.get().createPortalBlocks();
        level.playSound(
                null,
                seed,
                SoundEvents.PORTAL_TRIGGER,
                SoundSource.BLOCKS,
                1.0F,
                level.getRandom().nextFloat() * 0.4F + 0.8F);
        Lumenwilds.LOGGER.info("[{}] Lumen portal ignited around {}.", Lumenwilds.MOD_ID, seed);
        return true;
    }

    /**
     * Find an existing Lumen portal near {@code around}, or build a fresh one at the surface there.
     *
     * @return the exit portal, or {@code null} if construction failed.
     */
    @Nullable
    public static ExitPortal getOrCreateExitPortal(ServerLevel dest, BlockPos around, Direction.Axis axis) {
        ExitPortal existing = findExistingPortal(dest, around);
        return existing != null ? existing : createExitPortal(dest, around, axis);
    }

    @Nullable
    private static ExitPortal findExistingPortal(ServerLevel dest, BlockPos around) {
        int yMin = Math.max(dest.getMinBuildHeight() + 1, around.getY() - SEARCH_RADIUS_VERTICAL);
        int yMax = Math.min(dest.getMaxBuildHeight() - 1, around.getY() + SEARCH_RADIUS_VERTICAL);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos best = null;
        double bestDistSqr = Double.MAX_VALUE;
        for (int dx = -SEARCH_RADIUS_HORIZONTAL; dx <= SEARCH_RADIUS_HORIZONTAL; dx++) {
            for (int dz = -SEARCH_RADIUS_HORIZONTAL; dz <= SEARCH_RADIUS_HORIZONTAL; dz++) {
                for (int y = yMin; y <= yMax; y++) {
                    cursor.set(around.getX() + dx, y, around.getZ() + dz);
                    if (dest.getBlockState(cursor).is(ModBlocks.LUMEN_PORTAL.get())) {
                        double distSqr = cursor.distSqr(around);
                        if (distSqr < bestDistSqr) {
                            bestDistSqr = distSqr;
                            best = cursor.immutable();
                        }
                    }
                }
            }
        }
        if (best == null) {
            return null;
        }
        // Descend to the bottom portal block of the column so the entity lands at the opening's base.
        BlockPos.MutableBlockPos bottom = best.mutable();
        while (dest.getBlockState(bottom.below()).is(ModBlocks.LUMEN_PORTAL.get())) {
            bottom.move(Direction.DOWN);
        }
        Direction.Axis axis = dest.getBlockState(bottom)
                .getOptionalValue(LumenPortalBlock.AXIS)
                .orElse(Direction.Axis.X);
        Lumenwilds.LOGGER.info(
                "[{}] Reusing existing Lumen portal near {} (at {}).", Lumenwilds.MOD_ID, around, bottom);
        return new ExitPortal(bottom.immutable(), axis, 1, 1);
    }

    private static ExitPortal createExitPortal(ServerLevel dest, BlockPos around, Direction.Axis axis) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockPos surface = dest.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, around);
        int y = Mth.clamp(
                surface.getY(), dest.getMinBuildHeight() + 1, dest.getMaxBuildHeight() - (BUILT_INTERIOR_HEIGHT + 3));
        BlockPos corner = new BlockPos(around.getX(), y, around.getZ()); // bottom-left frame corner

        BlockState frame = ModBlocks.LUMENBOUND_STONE.get().defaultBlockState();
        BlockState air = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        BlockState portal = ModBlocks.LUMEN_PORTAL.get().defaultBlockState().setValue(LumenPortalBlock.AXIS, axis);

        int footprintW = BUILT_INTERIOR_WIDTH + 2;
        int footprintH = BUILT_INTERIOR_HEIGHT + 2;

        // Frame outline + cleared interior.
        for (int col = 0; col < footprintW; col++) {
            for (int row = 0; row < footprintH; row++) {
                BlockPos p = corner.relative(right, col).above(row);
                boolean isFrame = col == 0 || col == footprintW - 1 || row == 0 || row == footprintH - 1;
                dest.setBlock(p, isFrame ? frame : air, 3);
            }
        }
        // Solid footing under the whole footprint so the frame doesn't float over a hole.
        for (int col = 0; col < footprintW; col++) {
            BlockPos under = corner.relative(right, col).below();
            if (dest.getBlockState(under).isAir()) {
                dest.setBlock(under, frame, 3);
            }
        }
        // Fill the interior with portal blocks (flag 18 = clients + known-shape, no neighbour pops).
        BlockPos bottomLeftInterior = corner.relative(right, 1).above(1);
        for (int col = 0; col < BUILT_INTERIOR_WIDTH; col++) {
            for (int row = 0; row < BUILT_INTERIOR_HEIGHT; row++) {
                dest.setBlock(bottomLeftInterior.relative(right, col).above(row), portal, 18);
            }
        }
        Lumenwilds.LOGGER.info("[{}] Built return Lumen portal at {} (axis {}).", Lumenwilds.MOD_ID, corner, axis);
        return new ExitPortal(bottomLeftInterior, axis, BUILT_INTERIOR_WIDTH, BUILT_INTERIOR_HEIGHT);
    }
}
