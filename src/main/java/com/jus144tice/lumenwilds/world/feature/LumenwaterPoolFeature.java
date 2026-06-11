/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * A small, <b>chunk-safe</b> Lumenwater pool (replaces the vanilla {@code minecraft:lake}, Phase 9 fix). The
 * vanilla {@code LakeFeature} writes up to ~16 blocks from its origin, which — combined with {@code in_square}
 * placement — can reach a not-yet-generated neighbour chunk and crash chunk-gen with <i>"Requested chunk
 * unavailable during world generation"</i> (it did, in the Moonmire / Undercrown pools). This carves a
 * shallow ellipsoid basin of at most radius 4 (well within the feature-write margin), so it can never reach an
 * unavailable chunk. Walls the basin with Moonloam and fills the lower bowl with Lumenwater.
 */
public class LumenwaterPoolFeature extends Feature<NoneFeatureConfiguration> {

    public LumenwaterPoolFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int rx = 2 + random.nextInt(2); // 2–3
        int rz = 2 + random.nextInt(2);
        int depth = 2 + random.nextInt(2); // 2–3

        BlockState water = ModBlocks.LUMENWATER_BLOCK.get().defaultBlockState(); // LEVEL 0 = source
        BlockState wall = ModBlocks.MOONLOAM.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        boolean placed = false;

        for (int dx = -rx - 1; dx <= rx + 1; dx++) {
            for (int dz = -rz - 1; dz <= rz + 1; dz++) {
                for (int dy = -depth; dy <= 1; dy++) {
                    double e = (double) (dx * dx) / ((rx + 0.5) * (rx + 0.5))
                            + (double) (dz * dz) / ((rz + 0.5) * (rz + 0.5))
                            + (double) (dy * dy) / ((depth + 0.5) * (depth + 0.5));
                    cursor.setWithOffset(origin, dx, dy, dz);
                    if (e <= 1.0) {
                        // Interior bowl: Lumenwater up to the rim, air above it.
                        setBlock(level, cursor, dy <= 0 ? water : air);
                        placed = true;
                    } else if (e <= 1.7 && dy <= 0) {
                        // Shell: wall the basin with Moonloam where there's solid ground (don't seal air gaps).
                        BlockState current = level.getBlockState(cursor);
                        if (!current.isAir() && !current.is(water.getBlock())) {
                            setBlock(level, cursor, wall);
                        }
                    }
                }
            }
        }
        return placed;
    }
}
