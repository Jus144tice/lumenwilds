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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Decorates the actual cave surfaces of the Undercrown (Phase 9 "make the caves interesting") — instead of
 * ore-veins buried in rock, this finds open cave air around its origin and grows things on the floors, walls and
 * ceilings: blue-violet Glasspetal Clusters bristling from any rock face, and glowing plants (Glowvine / Glow
 * Fern) carpeting the floors. Makes the cavern system read as a living, glowing crystal grotto. Chunk-safe
 * (scans within ±7 / ±8 of the origin). Bound to {@code ModFeatures#UNDERCROWN_DECOR}.
 */
public class UndercrownDecorFeature extends Feature<NoneFeatureConfiguration> {

    private static final int TRIES = 56;

    public UndercrownDecorFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = context.random();
        BlockState cluster = ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState();
        boolean placedAny = false;

        for (int i = 0; i < TRIES; i++) {
            BlockPos p = origin.offset(rand.nextInt(15) - 7, rand.nextInt(17) - 8, rand.nextInt(15) - 7);
            if (!level.getBlockState(p).isAir()) {
                continue; // must be open cave air
            }
            // Ceiling: hang a TALL Glowvine strand down into the air (the Undercrown's defining drape of living
            // light), or sometimes a down-facing crystal.
            if (isCaveRock(level, p.above(), Direction.DOWN)) {
                if (rand.nextInt(3) != 0) {
                    int len = 3 + rand.nextInt(6); // 3..8 blocks long
                    BlockPos.MutableBlockPos m = p.mutable();
                    for (int d = 0; d < len && level.getBlockState(m).isAir(); d++) {
                        level.setBlock(m, ModBlocks.GLOWVINE.get().defaultBlockState(), 2);
                        m.move(Direction.DOWN);
                    }
                } else {
                    level.setBlock(p, cluster.setValue(AmethystClusterBlock.FACING, Direction.DOWN), 2);
                }
                placedAny = true;
                continue;
            }
            // Floor: a sturdy rock face below — grow a glowing plant or an up-facing crystal.
            BlockPos below = p.below();
            if (isCaveRock(level, below, Direction.UP)) {
                double r = rand.nextDouble();
                if (r < 0.55) {
                    level.setBlock(p, ModBlocks.GLOW_FERN.get().defaultBlockState(), 2);
                } else if (r < 0.85) {
                    level.setBlock(p, cluster.setValue(AmethystClusterBlock.FACING, Direction.UP), 2);
                }
                placedAny = true;
                continue;
            }
            // Otherwise attach a crystal to whichever rock face is adjacent (wall), pointing into the air.
            for (Direction dir : Direction.values()) {
                if (isCaveRock(level, p.relative(dir), dir.getOpposite())) {
                    if (rand.nextInt(3) == 0) {
                        level.setBlock(p, cluster.setValue(AmethystClusterBlock.FACING, dir.getOpposite()), 2);
                        placedAny = true;
                    }
                    break;
                }
            }
        }
        return placedAny;
    }

    /** True if {@code pos} is solid Undercrown rock presenting a sturdy face in {@code towardAir}. */
    private static boolean isCaveRock(WorldGenLevel level, BlockPos pos, Direction towardAir) {
        BlockState st = level.getBlockState(pos);
        return (st.is(ModBlocks.MOONSTONE.get()) || st.is(ModBlocks.DEEP_MOONSTONE.get()))
                && st.isFaceSturdy(level, pos, towardAir);
    }
}
