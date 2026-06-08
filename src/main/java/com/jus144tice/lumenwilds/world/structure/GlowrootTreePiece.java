/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * Draws the Glowroot mega tree (massive tapering trunk, arching buttress roots, deep taproots, a broad
 * estate-style canopy, and a Lumen-Crystal-Ore root cluster). {@link #postProcess} runs once per chunk
 * the tree overlaps; it regenerates the whole tree from a position-seeded RNG (so every chunk's slice is
 * identical) but only writes blocks inside the per-chunk {@code writeBox} — so a town-sized tree spans
 * chunks with no "far chunk" errors.
 */
public class GlowrootTreePiece extends StructurePiece {

    private static final double TRUNK_RADIUS = 10.0; // ~20 wide
    private static final int MIN_HEIGHT = 72;
    private static final int EXTRA_HEIGHT = 18; // total 72..89
    private static final int ROOT_DEPTH = 32;
    private static final int HORIZONTAL_REACH = 34; // covers the ~50-wide crown + branch/root spread

    private final BlockPos origin;

    public GlowrootTreePiece(BlockPos origin) {
        super(ModStructures.GLOWROOT_TREE_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public GlowrootTreePiece(CompoundTag tag) {
        super(ModStructures.GLOWROOT_TREE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - HORIZONTAL_REACH,
                o.getY() - ROOT_DEPTH - 2,
                o.getZ() - HORIZONTAL_REACH,
                o.getX() + HORIZONTAL_REACH,
                o.getY() + MIN_HEIGHT + EXTRA_HEIGHT + 12,
                o.getZ() + HORIZONTAL_REACH);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("ox", origin.getX());
        tag.putInt("oy", origin.getY());
        tag.putInt("oz", origin.getZ());
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox writeBox,
            ChunkPos chunkPos,
            BlockPos pos) {
        // Deterministic per-tree RNG so every overlapping chunk regenerates the identical tree.
        RandomSource rand = RandomSource.create(
                origin.getX() * 341873128712L ^ origin.getZ() * 132897987541L ^ (long) origin.getY());

        int cx = origin.getX();
        int cz = origin.getZ();
        int baseY = origin.getY();
        int height = MIN_HEIGHT + rand.nextInt(EXTRA_HEIGHT);
        BlockState log = ModBlocks.GLOWROOT_LOG.get().defaultBlockState();

        buildTrunk(level, writeBox, cx, baseY, cz, height, log);
        buildButtressRoots(level, writeBox, rand, cx, baseY, cz, log);
        buildTaproots(level, writeBox, rand, cx, baseY, cz, log);
        seedOreColumn(level, writeBox, rand, cx, baseY, cz);
        buildBranchesAndCrown(level, writeBox, rand, cx, baseY, cz, height, log);
    }

    private void buildTrunk(
            WorldGenLevel level, BoundingBox box, int cx, int baseY, int cz, int height, BlockState log) {
        for (int dy = 0; dy < height; dy++) {
            double t = (double) dy / height;
            double r = TRUNK_RADIUS - (t > 0.6 ? (t - 0.6) / 0.4 * 4.0 : 0.0);
            fillDisc(level, box, cx, baseY + dy, cz, Math.max(3.0, r), log, false);
        }
    }

    private void buildButtressRoots(
            WorldGenLevel level, BoundingBox box, RandomSource random, int cx, int baseY, int cz, BlockState log) {
        int arms = 12 + random.nextInt(6);
        for (int k = 0; k < arms; k++) {
            double ang = (k / (double) arms) * Math.PI * 2.0 + random.nextDouble() * 0.5;
            double dirX = Math.cos(ang);
            double dirZ = Math.sin(ang);
            int len = 14 + random.nextInt(8);
            double x = cx + dirX * (TRUNK_RADIUS - 1.0);
            double z = cz + dirZ * (TRUNK_RADIUS - 1.0);
            double y = baseY + 4 + random.nextInt(5);
            for (int i = 0; i < len; i++) {
                x += dirX;
                z += dirZ;
                double frac = i / (double) len;
                if (frac < 0.4) {
                    y += (random.nextDouble() - 0.45) * 0.6;
                } else {
                    y -= 1.0 + (random.nextDouble() - 0.5) * 0.4;
                }
                if (y < baseY - 10) {
                    break;
                }
                double r = Math.max(1.6, 3.2 - frac * 1.8);
                fillDisc(level, box, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), r, log, false);
            }
        }
    }

    private void buildTaproots(
            WorldGenLevel level, BoundingBox box, RandomSource random, int cx, int baseY, int cz, BlockState log) {
        int taproots = 4 + random.nextInt(4);
        int minY = level.getMinBuildHeight() + 1;
        for (int k = 0; k < taproots; k++) {
            double ang = random.nextDouble() * Math.PI * 2.0;
            double x = cx + Math.cos(ang) * (TRUNK_RADIUS * 0.5);
            double z = cz + Math.sin(ang) * (TRUNK_RADIUS * 0.5);
            int depth = 18 + random.nextInt(ROOT_DEPTH - 18 + 1);
            for (int i = 0; i < depth; i++) {
                int y = baseY - i;
                if (y <= minY) {
                    break;
                }
                x += (random.nextDouble() - 0.5) * 0.7;
                z += (random.nextDouble() - 0.5) * 0.7;
                fillDisc(level, box, (int) Math.round(x), y, (int) Math.round(z), 1.7, log, false);
            }
        }
    }

    private void seedOreColumn(WorldGenLevel level, BoundingBox box, RandomSource random, int cx, int baseY, int cz) {
        BlockState ore = ModBlocks.LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        BlockState deepOre = ModBlocks.DEEP_LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        double radius = TRUNK_RADIUS + 2.0;
        int ir = (int) Math.ceil(radius);
        int minY = level.getMinBuildHeight() + 1;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dy = -1; dy >= -ROOT_DEPTH; dy--) {
            int y = baseY + dy;
            if (y <= minY) {
                break;
            }
            for (int dx = -ir; dx <= ir; dx++) {
                for (int dz = -ir; dz <= ir; dz++) {
                    double distSqr = dx * dx + dz * dz;
                    if (distSqr > radius * radius) {
                        continue;
                    }
                    double chance = 0.5 * (1.0 - Math.sqrt(distSqr) / (radius + 1.0));
                    if (random.nextDouble() >= chance) {
                        continue;
                    }
                    cursor.set(cx + dx, y, cz + dz);
                    if (!box.isInside(cursor)) {
                        continue;
                    }
                    BlockState here = level.getBlockState(cursor);
                    if (here.is(ModBlocks.MOONSTONE.get())) {
                        level.setBlock(cursor, ore, 2);
                    } else if (here.is(ModBlocks.DEEP_MOONSTONE.get())) {
                        level.setBlock(cursor, deepOre, 2);
                    }
                }
            }
        }
    }

    private void buildBranchesAndCrown(
            WorldGenLevel level,
            BoundingBox box,
            RandomSource random,
            int cx,
            int baseY,
            int cz,
            int height,
            BlockState log) {
        int branches = 16 + random.nextInt(8);
        int firstBranchY = baseY + (int) (height * 0.40);
        int top = baseY + height;
        for (int k = 0; k < branches; k++) {
            double ang = (k / (double) branches) * Math.PI * 2.0 + random.nextDouble() * 0.6;
            double dirX = Math.cos(ang);
            double dirZ = Math.sin(ang);
            double x = cx;
            double z = cz;
            double y = firstBranchY + random.nextInt(Math.max(1, top - firstBranchY));
            int len = 16 + random.nextInt(8);
            for (int i = 0; i < len; i++) {
                x += dirX;
                z += dirZ;
                double frac = i / (double) len;
                y += frac < 0.4 ? 0.8 : (frac < 0.75 ? 0.15 : -0.2);
                double r = Math.max(1.0, 2.6 - frac * 1.8);
                fillDisc(level, box, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), r, log, false);
                if (frac > 0.4 && i % 2 == 0) {
                    placeLeafBlob(level, box, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), 4);
                }
            }
            placeLeafBlob(
                    level, box, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), 5 + random.nextInt(2));
        }
        // Broad ~50-wide crown, in two stacked domes for fullness.
        placeOblateCrown(level, box, cx, baseY + (int) (height * 0.86), cz, 24, 10);
        placeOblateCrown(level, box, cx, baseY + (int) (height * 0.72), cz, 18, 7);
    }

    private void placeOblateCrown(WorldGenLevel level, BoundingBox box, int cx, int cy, int cz, int horiz, int vert) {
        BlockState leaves = leaves();
        for (int dy = -vert; dy <= vert; dy++) {
            double layerR = horiz * Math.sqrt(Math.max(0.0, 1.0 - (double) (dy * dy) / (double) (vert * vert)));
            fillDisc(level, box, cx, cy + dy, cz, layerR, leaves, true);
        }
    }

    private void placeLeafBlob(WorldGenLevel level, BoundingBox box, int cx, int cy, int cz, int radius) {
        BlockState leaves = leaves();
        for (int dy = -radius; dy <= radius; dy++) {
            double layerR = radius * Math.sqrt(Math.max(0.0, 1.0 - (double) (dy * dy) / (double) (radius * radius)));
            fillDisc(level, box, cx, cy + dy, cz, layerR, leaves, true);
        }
    }

    private void fillDisc(
            WorldGenLevel level,
            BoundingBox box,
            int cx,
            int y,
            int cz,
            double radius,
            BlockState state,
            boolean onlyReplaceable) {
        int ir = (int) Math.ceil(radius);
        double r2 = radius * radius;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -ir; dx <= ir; dx++) {
            for (int dz = -ir; dz <= ir; dz++) {
                if (dx * dx + dz * dz > r2) {
                    continue;
                }
                cursor.set(cx + dx, y, cz + dz);
                if (!box.isInside(cursor)) {
                    continue;
                }
                if (onlyReplaceable && !level.getBlockState(cursor).canBeReplaced()) {
                    continue;
                }
                level.setBlock(cursor, state, 2);
            }
        }
    }

    private static BlockState leaves() {
        return ModBlocks.GLOWWOOD_LEAVES.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);
    }
}
