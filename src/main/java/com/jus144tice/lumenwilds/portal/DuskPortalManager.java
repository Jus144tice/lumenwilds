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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;

/**
 * Orchestrator for Duskglass portals (Lumenwilds ↔ Nether) — the sibling of {@link LumenPortalManager}. Igniting
 * validates a Duskglass frame and fills it with {@link ModBlocks#DUSK_PORTAL}; on the destination side it
 * finds-or-builds the matching return portal. Placement is dimension-aware: open dims (the Lumenwilds) anchor to
 * the surface heightmap, while the enclosed Nether scans for a floored air pocket (avoiding lava) and carves the
 * frame in.
 */
public final class DuskPortalManager {

    private static final int BUILT_INTERIOR_WIDTH = 2;
    private static final int BUILT_INTERIOR_HEIGHT = 3;
    private static final int SEARCH_RADIUS_HORIZONTAL = 8;
    private static final int SEARCH_RADIUS_VERTICAL = 48;

    private DuskPortalManager() {}

    public record ExitPortal(BlockPos bottomLeftInterior, Direction.Axis axis, int width, int height) {}

    /** Validate a Duskglass frame around the (air) {@code seed} and fill it with Dusk Portal blocks. */
    public static boolean tryActivatePortal(Level level, BlockPos seed) {
        if (level.isClientSide) {
            return false;
        }
        Optional<DuskPortalShape> shape = DuskPortalShape.findEmptyPortalShape(level, seed, Direction.Axis.X);
        if (shape.isEmpty()) {
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
        Lumenwilds.LOGGER.info("[{}] Dusk portal ignited around {}.", Lumenwilds.MOD_ID, seed);
        return true;
    }

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
                    if (dest.getBlockState(cursor).is(ModBlocks.DUSK_PORTAL.get())) {
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
        BlockPos.MutableBlockPos bottom = best.mutable();
        while (dest.getBlockState(bottom.below()).is(ModBlocks.DUSK_PORTAL.get())) {
            bottom.move(Direction.DOWN);
        }
        Direction.Axis axis = dest.getBlockState(bottom)
                .getOptionalValue(DuskPortalBlock.AXIS)
                .orElse(Direction.Axis.X);
        return new ExitPortal(bottom.immutable(), axis, 1, 1);
    }

    private static ExitPortal createExitPortal(ServerLevel dest, BlockPos around, Direction.Axis axis) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        int y = anchorY(dest, around);
        BlockPos corner = new BlockPos(around.getX(), y, around.getZ()); // bottom-left frame corner

        BlockState frame = ModBlocks.DUSKGLASS.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState portal = ModBlocks.DUSK_PORTAL.get().defaultBlockState().setValue(DuskPortalBlock.AXIS, axis);

        int footprintW = BUILT_INTERIOR_WIDTH + 2;
        int footprintH = BUILT_INTERIOR_HEIGHT + 2;

        // Clear a small air pocket around the whole footprint (needed in the enclosed Nether, harmless in the
        // open Lumenwilds) so the portal isn't buried and lava can't sit against the opening.
        for (int col = -1; col <= footprintW; col++) {
            for (int row = 0; row <= footprintH; row++) {
                for (int depth = -1; depth <= 1; depth++) {
                    BlockPos p = corner.relative(right, col).above(row).relative(right.getClockWise(), depth);
                    if (dest.getBlockState(p).is(ModBlocks.DUSK_PORTAL.get())) {
                        continue;
                    }
                    dest.setBlock(p, air, 18);
                }
            }
        }

        // Frame outline + cleared interior.
        for (int col = 0; col < footprintW; col++) {
            for (int row = 0; row < footprintH; row++) {
                BlockPos p = corner.relative(right, col).above(row);
                boolean isFrame = col == 0 || col == footprintW - 1 || row == 0 || row == footprintH - 1;
                dest.setBlock(p, isFrame ? frame : air, 3);
            }
        }
        // Solid footing under the whole footprint (and its ±1 depth margin) so the frame stands on ground.
        for (int col = -1; col <= footprintW; col++) {
            for (int depth = -1; depth <= 1; depth++) {
                BlockPos under = corner.relative(right, col)
                        .relative(right.getClockWise(), depth)
                        .below();
                if (!dest.getBlockState(under).isSolid()) {
                    dest.setBlock(under, frame, 3);
                }
            }
        }
        // Fill the interior with portal blocks (flag 18 = clients + known-shape, no neighbour pops).
        BlockPos bottomLeftInterior = corner.relative(right, 1).above(1);
        for (int col = 0; col < BUILT_INTERIOR_WIDTH; col++) {
            for (int row = 0; row < BUILT_INTERIOR_HEIGHT; row++) {
                dest.setBlock(bottomLeftInterior.relative(right, col).above(row), portal, 18);
            }
        }
        Lumenwilds.LOGGER.info("[{}] Built return Dusk portal at {} (axis {}).", Lumenwilds.MOD_ID, corner, axis);
        return new ExitPortal(bottomLeftInterior, axis, BUILT_INTERIOR_WIDTH, BUILT_INTERIOR_HEIGHT);
    }

    /** Where to seat the built frame: the surface in open dims, or a floored, lava-free air pocket in the Nether. */
    private static int anchorY(ServerLevel dest, BlockPos around) {
        if (!dest.dimensionType().hasCeiling()) {
            BlockPos surface = dest.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, around);
            return Mth.clamp(
                    surface.getY(),
                    dest.getMinBuildHeight() + 1,
                    dest.getMaxBuildHeight() - (BUILT_INTERIOR_HEIGHT + 3));
        }
        // Enclosed (Nether): scan for a solid, lava-free floor with headroom near the scaled Y. Cap at the
        // LOGICAL height (128 in the Nether — below the bedrock ceiling), NOT getMaxBuildHeight() (256), or the
        // portal builds on the roof.
        int ceiling = Math.min(dest.getMaxBuildHeight(), dest.dimensionType().logicalHeight());
        int lo = dest.getMinBuildHeight() + 4;
        int hi = ceiling - (BUILT_INTERIOR_HEIGHT + 4);
        int start = Mth.clamp(around.getY(), lo, hi);
        for (int yy = start; yy >= lo; yy--) {
            if (floored(dest, around.getX(), yy, around.getZ())) {
                return yy;
            }
        }
        for (int yy = start + 1; yy <= hi; yy++) {
            if (floored(dest, around.getX(), yy, around.getZ())) {
                return yy;
            }
        }
        return start; // force it — createExitPortal carves the pocket + lays a Duskglass floor anyway
    }

    /** A solid, non-lava block at (x,y-1,z) with clear, lava-free headroom at (x,y..y+3,z). */
    private static boolean floored(ServerLevel dest, int x, int y, int z) {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(x, y - 1, z);
        BlockState below = dest.getBlockState(p);
        if (!below.isSolid() || !below.getFluidState().isEmpty()) {
            return false;
        }
        for (int dy = 0; dy < BUILT_INTERIOR_HEIGHT + 1; dy++) {
            p.set(x, y + dy, z);
            BlockState here = dest.getBlockState(p);
            FluidState fluid = here.getFluidState();
            if ((here.isSolid() && !here.canBeReplaced()) || !fluid.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
