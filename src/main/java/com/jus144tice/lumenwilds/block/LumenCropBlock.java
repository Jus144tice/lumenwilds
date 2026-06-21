/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared base for the Lumenwilds' native crops (v1.4 F2+). Extends vanilla {@link CropBlock} — so it plants on
 * {@link LumenFarmlandBlock} for free ({@code mayPlaceOn} = {@code instanceof FarmBlock}), keeps the vanilla
 * moisture growth bonus ({@code getGrowthSpeed}, so Lumenwater-hydrated farmland grows crops faster), and is
 * recognised by auto-replant / right-click-harvest mods (which gate on {@code instanceof CropBlock}).
 *
 * <p>Two alien-world overrides: crops grow at <b>any light level</b> (the dimension is constant dim twilight —
 * vanilla gates growth on light ≥ 9) and survive without the vanilla light ≥ 8 requirement. Leaf subclasses
 * supply the seed item + {@code CODEC} (and, for short-cycle crops, the age property).</p>
 */
public abstract class LumenCropBlock extends CropBlock {

    private final Supplier<? extends ItemLike> seed;

    protected LumenCropBlock(BlockBehaviour.Properties props, Supplier<? extends ItemLike> seed) {
        super(props);
        this.seed = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seed.get();
    }

    /**
     * Crops grow on Lumen Farmland AND on native soil (Moonloam / Lumen Grass) — an alien-world leniency that
     * lets crops generate wild and gives a forgiving early game. Tilling still matters: only farmland gets the
     * moisture/fertility growth bonus ({@code getGrowthSpeed}), so a hydrated Lumen Farmland grows crops far
     * faster than bare Moonloam.
     */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.LUMEN_GRASS_BLOCK.get());
    }

    /** Grow at ANY light level (vanilla requires raw brightness ≥ 9). Keeps the moisture/fertility speed bonus. */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        int age = getAge(state);
        if (age < getMaxAge()) {
            float speed = getGrowthSpeed(state, level, pos);
            if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(
                    level, pos, state, random.nextInt((int) (25.0F / speed) + 1) == 0)) {
                level.setBlock(pos, getStateForAge(age + 1), 2);
                net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, state);
            }
        }
    }

    /** Survive on farmland WITHOUT the vanilla light ≥ 8 gate (dim world). Soil rules otherwise unchanged. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        net.neoforged.neoforge.common.util.TriState soil =
                belowState.canSustainPlant(level, below, Direction.UP, state);
        if (!soil.isDefault()) {
            return soil.isTrue();
        }
        return mayPlaceOn(belowState, level, below);
    }
}
