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
 * The ordinary 2×2 Glowroot tree — a normal worldgen feature that draws a scaled-down version of the
 * {@link GlowrootShape} mega tree (tall trunk, spreading leafy branches, broad dual-dome canopy, arching
 * roots). This makes it read as the same species as the giant rather than a vanilla dark oak. Kept small
 * enough ({@link GlowrootShape#MEDIUM}) to stay within a feature's chunk-write limit.
 */
public class GlowrootTreeFeature extends Feature<NoneFeatureConfiguration> {

    public GlowrootTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (!isGround(level.getBlockState(origin.below()))) {
            return false;
        }
        GlowrootShape.Placer placer = new GlowrootShape.Placer() {
            @Override
            public void set(BlockPos pos, BlockState state) {
                setBlock(level, pos, state);
            }

            @Override
            public BlockState getState(BlockPos pos) {
                return level.getBlockState(pos);
            }

            @Override
            public int minY() {
                return level.getMinBuildHeight() + 1;
            }
        };
        GlowrootShape.generate(placer, context.random(), origin, GlowrootShape.MEDIUM);
        return true;
    }

    private static boolean isGround(BlockState state) {
        return state.is(ModBlocks.LUMEN_GRASS_BLOCK.get())
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.MOONSTONE.get());
    }
}
