/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

/**
 * Lumen Grass — the dimension's living surface block, behaving like the Overworld's grass: it spreads onto
 * adjacent {@link ModBlocks#MOONLOAM} (the "dirt"), reverts to Moonloam when capped by a light-blocking block,
 * drops Moonloam when mined (Silk Touch drops the grass — see {@code ModLootTableProvider}), and responds to
 * bone meal by scattering Lumenwilds flora.
 *
 * <p>A faithful port of vanilla {@code GrassBlock}/{@code SpreadingSnowyDirtBlock}, keyed to Moonloam + Lumen
 * Grass, but extending {@link Block} directly (no {@code SNOWY} property — the Lumenwilds has no snow, so the
 * extra block state + models aren't worth it; vanilla's {@code canBeGrass}/{@code canPropagate} are private
 * anyway, so subclassing wouldn't reuse them).</p>
 *
 * <p><b>Dim-dimension tuning:</b> the spread light threshold ({@link #MIN_SPREAD_LIGHT}) is far lower than
 * vanilla's 9. The Lumenwilds is a dim world — its "daylight" is low and its nights run on moonlight at a
 * similar level — so a low bar lets Lumen Grass grow by sun, moon, <em>or</em> nearby bioluminescence
 * ({@code getMaxLocalRawBrightness} folds in block light), matching the bible's living-light theme. True
 * darkness (deep unlit caves) still stops it.</p>
 */
public class LumenGrassBlock extends Block implements BonemealableBlock {

    public static final MapCodec<LumenGrassBlock> CODEC = simpleCodec(LumenGrassBlock::new);

    /** Minimum light above for spread (vanilla grass = 9). Low, so the dim dimension still grows grass. */
    public static final int MIN_SPREAD_LIGHT = 4;

    public LumenGrassBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public MapCodec<LumenGrassBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) {
                return; // don't load neighbouring chunks just to revert
            }
            level.setBlockAndUpdate(pos, ModBlocks.MOONLOAM.get().defaultBlockState());
        } else {
            if (!level.isAreaLoaded(pos, 3)) {
                return; // don't load neighbouring chunks just to spread
            }
            if (level.getMaxLocalRawBrightness(pos.above()) >= MIN_SPREAD_LIGHT) {
                BlockState grass = this.defaultBlockState();
                for (int i = 0; i < 4; i++) {
                    BlockPos target = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (level.getBlockState(target).is(ModBlocks.MOONLOAM.get())
                            && canPropagate(grass, level, target)) {
                        level.setBlockAndUpdate(target, grass);
                    }
                }
            }
        }
    }

    /**
     * Revert PROMPTLY when a block is placed directly on top (capping the grass), rather than waiting for the
     * next random tick — so burying Lumen Grass with Moonloam (or anything opaque) turns it back to Moonloam
     * within a couple of ticks, matching the player expectation. Mirrors vanilla {@code FarmBlock}'s
     * updateShape → scheduleTick → tick pattern. (The random-tick revert in {@link #randomTick} still covers the
     * slow/ambient cases.)
     */
    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        if (direction == Direction.UP && !canBeGrass(state, level, pos)) {
            level.scheduleTick(pos, this, 2);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.MOONLOAM.get().defaultBlockState());
        }
    }

    /** Grass survives unless it's capped by a (nearly) light-blocking block or fully submerged. */
    private static boolean canBeGrass(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (aboveState.getFluidState().getAmount() == 8) {
            return false;
        }
        int light = LightEngine.getLightBlockInto(
                level, state, pos, aboveState, above, Direction.UP, aboveState.getLightBlock(level, above));
        return light < level.getMaxLightLevel();
    }

    /** Moonloam can become grass if it could host grass and isn't under flowing/standing water. */
    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        return canBeGrass(state, level, pos)
                && !level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    // --- BonemealableBlock: scatter Lumenwilds flora (Glow Fern + occasional Moonblossom) ------------

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos start = pos.above();
        BlockState fern = ModBlocks.GLOW_FERN.get().defaultBlockState();
        BlockState flower = ModBlocks.MOONBLOSSOM.get().defaultBlockState();

        attempts:
        for (int i = 0; i < 128; i++) {
            BlockPos target = start;
            for (int j = 0; j < i / 16; j++) {
                target = target.offset(
                        random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!level.getBlockState(target.below()).is(this)
                        || level.getBlockState(target).isCollisionShapeFullBlock(level, target)) {
                    continue attempts;
                }
            }
            if (level.getBlockState(target).isAir()) {
                BlockState place = random.nextInt(10) == 0 ? flower : fern; // ~10% Moonblossom, else Glow Fern
                if (place.canSurvive(level, target)) {
                    level.setBlock(target, place, Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    @Override
    public BonemealableBlock.Type getType() {
        return BonemealableBlock.Type.NEIGHBOR_SPREADER;
    }
}
