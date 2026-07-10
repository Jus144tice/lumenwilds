/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Frame detection + interior fill for a Duskglass portal — the same focused port of vanilla {@code PortalShape}
 * as {@link LumenPortalShape}, but keyed to {@link ModBlocks#DUSKGLASS} (frame) and {@link ModBlocks#DUSK_PORTAL}
 * (interior). Interior bounds match Nether-portal conventions ({@link #MIN_WIDTH}×{@link #MIN_HEIGHT} up to
 * {@link #MAX_WIDTH}×{@link #MAX_HEIGHT}).
 */
public final class DuskPortalShape {

    public static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    public static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;

    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    private BlockPos bottomLeft;
    private int height;
    private int width;

    /** True if {@code state} is the required frame material ({@link ModBlocks#DUSKGLASS}). */
    public static boolean isFrameBlock(BlockState state) {
        return state.is(ModBlocks.DUSKGLASS.get());
    }

    /** "Empty" interior: air, or an existing Dusk Portal block (so re-detection is idempotent). */
    private static boolean isEmpty(BlockState state) {
        return state.isAir() || state.is(ModBlocks.DUSK_PORTAL.get());
    }

    public static Optional<DuskPortalShape> findEmptyPortalShape(
            LevelAccessor level, BlockPos seed, Direction.Axis axis) {
        return findPortalShape(level, seed, s -> s.isValid() && s.numPortalBlocks == 0, axis);
    }

    private static Optional<DuskPortalShape> findPortalShape(
            LevelAccessor level, BlockPos seed, Predicate<DuskPortalShape> predicate, Direction.Axis axis) {
        Optional<DuskPortalShape> first =
                Optional.of(new DuskPortalShape(level, seed, axis)).filter(predicate);
        if (first.isPresent()) {
            return first;
        }
        Direction.Axis other = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        return Optional.of(new DuskPortalShape(level, seed, other)).filter(predicate);
    }

    private DuskPortalShape(LevelAccessor level, BlockPos seed, Direction.Axis axis) {
        this.level = level;
        this.axis = axis;
        this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(seed);
        if (this.bottomLeft == null) {
            this.bottomLeft = seed;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }

    private BlockPos calculateBottomLeft(BlockPos pos) {
        int floor = Math.max(this.level.getMinBuildHeight(), pos.getY() - MAX_HEIGHT);
        while (pos.getY() > floor && isEmpty(this.level.getBlockState(pos.below()))) {
            pos = pos.below();
        }
        Direction left = this.rightDir.getOpposite();
        int dist = this.getDistanceUntilEdgeAboveFrame(pos, left) - 1;
        return dist < 0 ? null : pos.relative(left, dist);
    }

    private int calculateWidth() {
        int w = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return w >= MIN_WIDTH && w <= MAX_WIDTH ? w : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos pos, Direction direction) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= MAX_WIDTH; i++) {
            cursor.set(pos).move(direction, i);
            BlockState state = this.level.getBlockState(cursor);
            if (!isEmpty(state)) {
                if (isFrameBlock(state)) {
                    return i;
                }
                break;
            }
            BlockState below = this.level.getBlockState(cursor.move(Direction.DOWN));
            if (!isFrameBlock(below)) {
                break;
            }
        }
        return 0;
    }

    private int calculateHeight() {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int h = this.getDistanceUntilTop(cursor);
        return h >= MIN_HEIGHT && h <= MAX_HEIGHT && this.hasTopFrame(cursor, h) ? h : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos cursor, int height) {
        for (int i = 0; i < this.width; i++) {
            BlockPos.MutableBlockPos p =
                    cursor.set(this.bottomLeft).move(Direction.UP, height).move(this.rightDir, i);
            if (!isFrameBlock(this.level.getBlockState(p))) {
                return false;
            }
        }
        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos cursor) {
        for (int i = 0; i < MAX_HEIGHT; i++) {
            cursor.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!isFrameBlock(this.level.getBlockState(cursor))) {
                return i;
            }
            cursor.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!isFrameBlock(this.level.getBlockState(cursor))) {
                return i;
            }
            for (int j = 0; j < this.width; j++) {
                cursor.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState state = this.level.getBlockState(cursor);
                if (!isEmpty(state)) {
                    return i;
                }
                if (state.is(ModBlocks.DUSK_PORTAL.get())) {
                    this.numPortalBlocks++;
                }
            }
        }
        return MAX_HEIGHT;
    }

    public boolean isValid() {
        return this.bottomLeft != null
                && this.width >= MIN_WIDTH
                && this.width <= MAX_WIDTH
                && this.height >= MIN_HEIGHT
                && this.height <= MAX_HEIGHT;
    }

    public Direction.Axis axis() {
        return this.axis;
    }

    /** Fill the validated interior with {@link ModBlocks#DUSK_PORTAL}, oriented along {@link #axis}. */
    public void createPortalBlocks() {
        BlockState portal = ModBlocks.DUSK_PORTAL.get().defaultBlockState().setValue(DuskPortalBlock.AXIS, this.axis);
        BlockPos farCorner =
                this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1);
        BlockPos.betweenClosed(this.bottomLeft, farCorner).forEach(p -> this.level.setBlock(p, portal, 18));
    }
}
