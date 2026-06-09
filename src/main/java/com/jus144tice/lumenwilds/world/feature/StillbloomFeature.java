/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * The giant <b>Stillbloom</b> of the Stillbloom Basin (Phase 5d.6): a 3–8-tall soft glowing flower built
 * from a {@code stillbloom_stem} column carrying a petal dome around a brilliant {@code stillbloom_core}.
 * A small, self-contained worldgen feature (well within a feature's chunk-write reach). Bound to
 * {@code ModFeatures#STILLBLOOM}.
 */
public class StillbloomFeature extends Feature<NoneFeatureConfiguration> {

    public StillbloomFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (!isGround(level.getBlockState(origin.below()))) {
            return false;
        }

        BlockState stem = ModBlocks.STILLBLOOM_STEM.get().defaultBlockState();
        BlockState petal = ModBlocks.STILLBLOOM_PETAL.get().defaultBlockState();
        BlockState core = ModBlocks.STILLBLOOM_CORE.get().defaultBlockState();

        int cx = origin.getX();
        int cz = origin.getZ();
        int baseY = origin.getY();
        int height = 3 + context.random().nextInt(6); // 3–8 tall

        // Stem column. Stop early if it runs into solid ground (e.g. a hill).
        int placed = 0;
        for (int dy = 0; dy < height; dy++) {
            BlockPos p = new BlockPos(cx, baseY + dy, cz);
            if (!isReplaceable(level, p)) {
                break;
            }
            setBlock(level, p, stem);
            placed = dy + 1;
        }
        if (placed < 2) {
            return false; // not enough room for a bloom
        }

        int headY = baseY + placed; // first block above the stem top
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        // Petal skirt (radius 2) just above the stem.
        placePetalDisc(level, cursor, cx, headY, cz, 2, petal);
        // Upper ring (radius 1) cupping the core.
        placePetalDisc(level, cursor, cx, headY + 1, cz, 1, petal);
        // The brilliant core in the centre of the upper ring, with a single petal cap above it.
        setIfReplaceable(level, cursor.set(cx, headY + 1, cz), core);
        setIfReplaceable(level, cursor.set(cx, headY + 2, cz), petal);
        return true;
    }

    private static void placePetalDisc(
            WorldGenLevel level, BlockPos.MutableBlockPos cursor, int cx, int y, int cz, int radius, BlockState petal) {
        double r2 = radius * radius + 0.5;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= r2) {
                    setIfReplaceable(level, cursor.set(cx + dx, y, cz + dz), petal);
                }
            }
        }
    }

    private static void setIfReplaceable(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (isReplaceable(level, pos)) {
            level.setBlock(pos, state, 2);
        }
    }

    private static boolean isReplaceable(WorldGenLevel level, BlockPos pos) {
        BlockState here = level.getBlockState(pos);
        return here.isAir() || here.canBeReplaced();
    }

    private static boolean isGround(BlockState state) {
        return state.is(ModBlocks.LUMEN_GRASS_BLOCK.get())
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.MOONSTONE.get())
                || state.is(ModBlocks.SMOOTH_MOONSTONE.get());
    }
}
