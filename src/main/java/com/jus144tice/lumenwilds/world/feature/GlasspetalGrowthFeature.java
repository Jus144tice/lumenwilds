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
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Scatters <b>glasspetal crystal growths</b> of varied size across the dry Glasspetal Crags floor (Phase 9
 * "feels like it's growing", reworked). Each growth is a fountain-like burst of tapering {@code glasspetal_block}
 * <b>crystal spikes</b> (thinning to a point and leaning outward) bristling with {@code glasspetal_cluster}
 * crystals — so it reads as a living crystal growth, not a blocky cube. Rolls a size: lone cluster / small burst /
 * large burst / rare MEGA (a tall many-spiked crystal). Kept sparse so the ground breathes and mobs spawn; the
 * town-sized version is the Glasspetal Spires <i>structure</i>. Chunk-safe (within ~±10, dry land only). Bound to
 * {@code ModFeatures#GLASSPETAL_GROWTH}.
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
            int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
            BlockPos spot = new BlockPos(x, y, z);
            if (!level.getBlockState(spot).isAir()) {
                continue; // dry land only
            }
            BlockPos floor = spot.below();
            if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) {
                continue;
            }
            double s = rand.nextDouble();
            if (s < 0.55) {
                cluster(level, spot); // a lone crystal
            } else if (s < 0.85) {
                burst(level, rand, spot, 2 + rand.nextInt(2), 2 + rand.nextInt(3)); // small burst
            } else if (s < 0.97) {
                burst(level, rand, spot, 3 + rand.nextInt(2), 4 + rand.nextInt(4)); // large burst
            } else {
                burst(level, rand, spot, 5 + rand.nextInt(3), 8 + rand.nextInt(7)); // rare MEGA crystal
            }
            placedAny = true;
        }
        return placedAny;
    }

    private static void cluster(WorldGenLevel level, BlockPos spot) {
        level.setBlock(spot, ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState(), 2);
    }

    /** A fountain of {@code spikes} tapering crystal spires (max height {@code maxH}) bursting from a small base. */
    private static void burst(WorldGenLevel level, RandomSource rand, BlockPos base, int spikes, int maxH) {
        BlockState core = ModBlocks.GLASSPETAL_BLOCK.get().defaultBlockState();
        // A small crystalline base pad.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (Math.abs(dx) + Math.abs(dz) <= 1) {
                    setIfReplaceable(level, base.offset(dx, 0, dz), core);
                }
            }
        }
        // A central tall spike + leaning satellites.
        spike(level, rand, base, 0.0, 0.0, maxH);
        for (int i = 0; i < spikes; i++) {
            double ang = rand.nextDouble() * Math.PI * 2.0;
            double lean = 0.18 + rand.nextDouble() * 0.22;
            int h = Math.max(2, (int) (maxH * (0.4 + rand.nextDouble() * 0.5)));
            spike(level, rand, base, Math.cos(ang) * lean, Math.sin(ang) * lean, h);
        }
    }

    /** One tapering crystal spike: a near-1-wide column that drifts (leans) and points up, cluster-capped. */
    private static void spike(
            WorldGenLevel level, RandomSource rand, BlockPos base, double dxStep, double dzStep, int h) {
        BlockState core = ModBlocks.GLASSPETAL_BLOCK.get().defaultBlockState();
        BlockState cluster = ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState();
        double fx = base.getX() + 0.5;
        double fz = base.getZ() + 0.5;
        int lastX = base.getX();
        int lastZ = base.getZ();
        int lastY = base.getY();
        for (int k = 1; k <= h; k++) {
            fx += dxStep;
            fz += dzStep;
            lastX = (int) Math.floor(fx);
            lastZ = (int) Math.floor(fz);
            lastY = base.getY() + k;
            BlockPos p = new BlockPos(lastX, lastY, lastZ);
            setIfReplaceable(level, p, core);
            // a crystal bristling off a side now and then
            if (k > 1 && rand.nextInt(3) == 0) {
                Direction d = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
                BlockPos side = p.relative(d);
                if (level.getBlockState(side).canBeReplaced()) {
                    level.setBlock(side, cluster.setValue(AmethystClusterBlock.FACING, d), 2);
                }
            }
        }
        // The pointed crystal tip.
        BlockPos tip = new BlockPos(lastX, lastY + 1, lastZ);
        if (level.getBlockState(tip).canBeReplaced()) {
            level.setBlock(tip, cluster, 2);
        }
    }

    private static void setIfReplaceable(WorldGenLevel level, BlockPos p, BlockState state) {
        if (level.getBlockState(p).canBeReplaced()) {
            level.setBlock(p, state, 2);
        }
    }
}
