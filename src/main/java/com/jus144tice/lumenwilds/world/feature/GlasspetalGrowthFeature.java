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
 * "make it feel like it's growing"): mostly small single clusters, often small crystal nubs, and uncommonly a
 * taller crystal mound — so the biome reads as living, evolving mineral growth rather than a uniform field of
 * one-block clusters. (The town-sized version is the rare Glasspetal Spires <i>structure</i>.) Chunk-safe:
 * scatters within ±7 of the origin and only on dry land (skips submerged spots). Bound to
 * {@code ModFeatures#GLASSPETAL_GROWTH}.
 */
public class GlasspetalGrowthFeature extends Feature<NoneFeatureConfiguration> {

    private static final int TRIES = 16;
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
                continue; // dry land only (water/occupied spots skipped)
            }
            BlockPos floor = spot.below();
            if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) {
                continue;
            }
            double s = rand.nextDouble();
            if (s < 0.58) {
                cluster(level, spot); // small: a single cluster
            } else if (s < 0.88) {
                mound(level, rand, spot, 1 + rand.nextInt(2), 1); // medium nub
            } else {
                mound(level, rand, spot, 3 + rand.nextInt(3), 2); // large crystal mound
            }
            placedAny = true;
        }
        return placedAny;
    }

    private static void cluster(WorldGenLevel level, BlockPos spot) {
        level.setBlock(spot, ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState(), 2);
    }

    /** A tapering crystal mound of Lumen Crystal Block (+ a little Shimmerstone), crowned with a cluster. */
    private static void mound(WorldGenLevel level, RandomSource rand, BlockPos base, int height, int baseR) {
        BlockState crystal = ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState();
        BlockState shimmer = ModBlocks.SHIMMERSTONE.get().defaultBlockState();
        for (int h = 0; h < height; h++) {
            int r = Math.round(baseR * (1.0F - (float) h / height));
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx * dx + dz * dz <= r * r + 1) {
                        BlockPos p = base.offset(dx, h, dz);
                        if (level.getBlockState(p).canBeReplaced()) {
                            level.setBlock(p, rand.nextInt(4) == 0 ? shimmer : crystal, 2);
                        }
                    }
                }
            }
        }
        BlockPos crown = base.above(height);
        if (level.getBlockState(crown).canBeReplaced()) {
            level.setBlock(crown, ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState(), 2);
        }
    }
}
