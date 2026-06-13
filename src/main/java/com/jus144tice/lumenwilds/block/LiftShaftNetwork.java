/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The liftshaft field-projection logic (Phase 11b) — the static helper a {@link LumenFieldProjectorBlock}'s
 * block entity runs each tick to fill (and clear) its gravity column. Mirrors {@code block.ResonanceNetwork} in
 * spirit: pure static, server-side, transient.
 *
 * <p>{@link #project} walks the column in the projector's mode direction, dropping a field cell into each open
 * space until it hits a solid block. The travel budget starts at {@link #RANGE}; whenever a placed cell is
 * orthogonally adjacent to a {@link GravityRepeaterBlock}, the budget resets to {@link #RANGE} — so wall-mounted
 * repeaters chain the column arbitrarily tall (the user's design). The total is hard-capped at
 * {@link #MAX_LENGTH} for safety. The block entity diffs the returned cells against last tick's to clear any it
 * no longer owns.</p>
 */
public final class LiftShaftNetwork {

    private LiftShaftNetwork() {}

    /** Cells a single source/repeater segment reaches. */
    public static final int RANGE = 16;
    /** Absolute safety cap on a column's length, regardless of repeaters. */
    public static final int MAX_LENGTH = 256;

    /** Walk and (re)place the field column from {@code projectorPos}; returns the cells now owned by it. */
    public static List<BlockPos> project(Level level, BlockPos projectorPos, LumenFieldProjectorBlock.Mode mode) {
        Direction dir = mode == LumenFieldProjectorBlock.Mode.ASCEND ? Direction.UP : Direction.DOWN;
        Block fieldBlock = fieldBlock(mode);
        BlockState fieldState = fieldBlock.defaultBlockState();
        List<BlockPos> placed = new ArrayList<>();
        int budget = RANGE;
        BlockPos.MutableBlockPos p = projectorPos.mutable().move(dir);
        for (int i = 0; i < MAX_LENGTH && budget > 0; i++) {
            BlockState here = level.getBlockState(p);
            boolean ours = here.is(fieldBlock);
            if (!ours && !canFill(here)) {
                break; // hit a solid/obstruction — the shaft ends here
            }
            BlockPos cell = p.immutable();
            if (!ours) {
                level.setBlock(cell, fieldState, 2);
            }
            placed.add(cell);
            budget--;
            if (adjacentRepeater(level, cell)) {
                budget = RANGE; // a repeater touches this cell → extend the column another segment
            }
            p.move(dir);
        }
        return placed;
    }

    /** Remove the given cells if they are still our field block (used to clear cells a projector dropped). */
    public static void clearStale(Level level, Collection<BlockPos> stale, LumenFieldProjectorBlock.Mode mode) {
        Block fieldBlock = fieldBlock(mode);
        for (BlockPos p : stale) {
            if (level.getBlockState(p).is(fieldBlock)) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

    /** On projector removal/mode-flip: walk the contiguous field column from the projector and clear it. */
    public static void clearColumn(Level level, BlockPos projectorPos, LumenFieldProjectorBlock.Mode mode) {
        Direction dir = mode == LumenFieldProjectorBlock.Mode.ASCEND ? Direction.UP : Direction.DOWN;
        Block fieldBlock = fieldBlock(mode);
        BlockPos.MutableBlockPos p = projectorPos.mutable().move(dir);
        for (int i = 0; i < MAX_LENGTH; i++) {
            if (!level.getBlockState(p).is(fieldBlock)) {
                break;
            }
            level.setBlock(p.immutable(), Blocks.AIR.defaultBlockState(), 2);
            p.move(dir);
        }
    }

    private static Block fieldBlock(LumenFieldProjectorBlock.Mode mode) {
        return mode == LumenFieldProjectorBlock.Mode.ASCEND
                ? ModBlocks.ASCENSION_FIELD.get()
                : ModBlocks.DESCENT_FIELD.get();
    }

    private static boolean canFill(BlockState s) {
        return s.isAir() || s.canBeReplaced();
    }

    private static boolean adjacentRepeater(Level level, BlockPos cell) {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(cell.relative(d)).getBlock() instanceof GravityRepeaterBlock) {
                return true;
            }
        }
        return false;
    }
}
