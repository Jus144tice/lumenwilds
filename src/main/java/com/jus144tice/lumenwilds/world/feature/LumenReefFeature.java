/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * A <b>Lumen Reef</b> (Phase 9 drawing-board "vibe up the underwater"): a small cluster of glowing
 * {@code lumen_coral_block} mounds and waterlogged {@code lumen_coral} fronds grown on the submerged seabed of
 * the Lumenwater seas, so the floor reads as living and bright instead of dead dirt. Self-contained and
 * chunk-safe (writes within ±2); no-ops on dry land (the placement reaches it via the ocean-floor heightmap).
 * Bound to {@code ModFeatures#LUMEN_REEF}.
 */
public class LumenReefFeature extends Feature<NoneFeatureConfiguration> {

    public LumenReefFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = context.random();
        if (!isWater(level, origin)) {
            return false; // must be submerged
        }

        BlockState coral = ModBlocks.LUMEN_CORAL_BLOCK.get().defaultBlockState();
        BlockState sand = ModBlocks.LUMENSAND.get().defaultBlockState();

        int cx = origin.getX();
        int cz = origin.getZ();
        int floorY = origin.getY() - 1; // the seabed block under the first water block
        boolean placedAny = false;
        BlockPos.MutableBlockPos cur = new BlockPos.MutableBlockPos();

        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (dx * dx + dz * dz > 5) {
                    continue;
                }
                int gx = cx + dx;
                int gz = cz + dz;
                cur.set(gx, floorY, gz);
                if (!isSeabed(level.getBlockState(cur))) {
                    continue;
                }
                double roll = rand.nextDouble();
                if (roll < 0.32) {
                    // A coral-block mound 1–2 high, capped by a frond.
                    int h = 1 + rand.nextInt(2);
                    int top = floorY;
                    for (int k = 1; k <= h; k++) {
                        BlockPos p = new BlockPos(gx, floorY + k, gz);
                        if (!isWater(level, p)) {
                            break;
                        }
                        level.setBlock(p, coral, 2);
                        top = floorY + k;
                        placedAny = true;
                    }
                    placeFrond(level, new BlockPos(gx, top + 1, gz), frond(rand));
                } else if (roll < 0.72) {
                    // A frond (coral or kelp) straight on the seabed.
                    if (placeFrond(level, new BlockPos(gx, floorY + 1, gz), frond(rand))) {
                        placedAny = true;
                    }
                } else if (roll < 0.85) {
                    // A glowing sand accent.
                    level.setBlock(cur.immutable(), sand, 2);
                    placedAny = true;
                }
            }
        }
        return placedAny;
    }

    /** A reef frond — a glowing Lumen Coral or, more often, a teal Lumen Kelp, for varied sea vegetation. */
    private static BlockState frond(RandomSource rand) {
        return (rand.nextInt(3) == 0 ? ModBlocks.LUMEN_CORAL : ModBlocks.LUMEN_KELP)
                .get()
                .defaultBlockState();
    }

    private static boolean placeFrond(WorldGenLevel level, BlockPos pos, BlockState frond) {
        if (isWater(level, pos)) {
            level.setBlock(pos, frond, 2);
            return true;
        }
        return false;
    }

    private static boolean isWater(WorldGenLevel level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER)
                && level.getBlockState(pos).canBeReplaced();
    }

    private static boolean isSeabed(BlockState state) {
        return state.is(ModBlocks.LUMENSAND.get())
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.MOONSTONE.get())
                || state.is(ModBlocks.LUMEN_GRASS_BLOCK.get())
                || state.is(ModBlocks.LUMEN_CORAL_BLOCK.get());
    }
}
