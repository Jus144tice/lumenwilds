/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Scatters <b>glasspetal crystal growths</b> of varied size across the dry Glasspetal Crags floor (Phase 9
 * "make it feel like it's growing"). All one blue-violet crystal family — a {@code glasspetal_block} core with
 * {@code glasspetal_cluster} crystals bristling off it — at sizes from a lone cluster to a small mound, so the
 * biome reads as living, evolving mineral growth. Kept <b>sparse</b> (open ground between growths, so mobs still
 * spawn). The town-sized version is the rare Glasspetal Spires <i>structure</i>. Chunk-safe: scatters within ±7,
 * dry land only. Bound to {@code ModFeatures#GLASSPETAL_GROWTH}.
 */
public class GlasspetalGrowthFeature extends Feature<NoneFeatureConfiguration> {

    private static final int TRIES = 6;
    private static final int SPREAD = 7;

    public GlasspetalGrowthFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = context.random();
        boolean placedAny = false;

        for (int t = 0; t < TRIES; t++) {
            int x = origin.getX() + rand.nextInt(SPREAD * 2 + 1) - SPREAD;
            int z = origin.getZ() + rand.nextInt(SPREAD * 2 + 1) - SPREAD;
            int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z); // first non-solid above the floor
            BlockPos spot = new BlockPos(x, y, z);
            if (!level.getBlockState(spot).isAir()) {
                continue; // dry land only
            }
            BlockPos floor = spot.below();
            if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) {
                continue;
            }
            double s = rand.nextDouble();
            if (s < 0.62) {
                cluster(level, spot); // small: a lone crystal
            } else if (s < 0.9) {
                mound(level, rand, spot, 1); // crystal bud
            } else {
                mound(level, rand, spot, 2 + rand.nextInt(2)); // a small crystal mound
            }
            placedAny = true;
        }
        return placedAny;
    }

    private static void cluster(WorldGenLevel level, BlockPos spot) {
        level.setBlock(spot, ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState(), 2);
    }

    /** A blue-violet {@code glasspetal_block} core (tapering) bristling with clusters on top + a couple of sides. */
    private static void mound(WorldGenLevel level, RandomSource rand, BlockPos base, int height) {
        BlockState core = ModBlocks.GLASSPETAL_BLOCK.get().defaultBlockState();
        BlockState cluster = ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState();
        for (int h = 0; h < height; h++) {
            int r = h == 0 && height >= 3 ? 1 : 0; // a 1-wide base on the larger mounds, a column otherwise
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos p = base.offset(dx, h, dz);
                    if (level.getBlockState(p).canBeReplaced()) {
                        level.setBlock(p, core, 2);
                    }
                }
            }
        }
        // Crystal crown.
        BlockPos crown = base.above(height);
        if (level.getBlockState(crown).canBeReplaced()) {
            level.setBlock(crown, cluster, 2);
        }
        // A few clusters bristling off the sides of the core.
        int bristles = height; // taller mounds bristle more
        for (int i = 0; i < bristles; i++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
            int hy = base.getY() + rand.nextInt(height);
            BlockPos side = new BlockPos(base.getX() + dir.getStepX(), hy, base.getZ() + dir.getStepZ());
            if (level.getBlockState(side).canBeReplaced()) {
                level.setBlock(
                        side, cluster.setValue(net.minecraft.world.level.block.AmethystClusterBlock.FACING, dir), 2);
            }
        }
    }
}
