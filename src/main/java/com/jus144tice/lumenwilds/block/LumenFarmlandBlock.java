/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Lumen Farmland (v1.4 Phase F1) — the dimension's tilled soil, a faithful {@link FarmBlock} clone keyed to
 * {@link ModBlocks#MOONLOAM} instead of vanilla dirt. Hoe Moonloam / Lumen Grass to make it (see
 * {@code event.LumenFarmingEvents}); it dries/tramples back to <b>Moonloam</b> (not dirt).
 *
 * <p>Because it extends {@link FarmBlock}, every vanilla farming check that does {@code instanceof FarmBlock}
 * (crop placement, the moisture growth bonus, stem→gourd) works on it for free. <b>Lumenwater hydrates it with
 * no extra code</b>: {@code isNearWater} reads {@code state.canBeHydrated(...)} and Lumenwater's FluidType has
 * {@code canHydrate(true)} + is in {@code #minecraft:water}. Vanilla's revert helpers hardcode {@code DIRT}, so
 * every method that reverts/places is overridden to use Moonloam.</p>
 */
public class LumenFarmlandBlock extends FarmBlock {

    // Typed to the parent (FarmBlock narrowed codec()'s return type); target-typing still builds our block.
    public static final MapCodec<FarmBlock> CODEC = simpleCodec(LumenFarmlandBlock::new);

    public LumenFarmlandBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public MapCodec<FarmBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return !this.defaultBlockState().canSurvive(ctx.getLevel(), ctx.getClickedPos())
                ? ModBlocks.MOONLOAM.get().defaultBlockState()
                : super.getStateForPlacement(ctx);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            turnToMoonloam(null, state, level, pos);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);
        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            } else if (!shouldMaintainFarmland(level, pos)) {
                turnToMoonloam(null, state, level, pos);
            }
        } else if (moisture < MAX_MOISTURE) {
            level.setBlock(pos, state.setValue(MOISTURE, MAX_MOISTURE), 2);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!level.isClientSide
                && net.neoforged.neoforge.common.CommonHooks.onFarmlandTrample(
                        level, pos, ModBlocks.MOONLOAM.get().defaultBlockState(), fallDistance, entity)) {
            turnToMoonloam(entity, state, level, pos);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    /** Revert to Moonloam (the Moonloam analog of vanilla {@code FarmBlock.turnToDirt}). */
    private static void turnToMoonloam(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        BlockState moonloam = pushEntitiesUp(state, ModBlocks.MOONLOAM.get().defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, moonloam);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, moonloam));
    }

    private static boolean shouldMaintainFarmland(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(BlockTags.MAINTAINS_FARMLAND);
    }

    private static boolean isNearWater(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        for (BlockPos around : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (state.canBeHydrated(level, pos, level.getFluidState(around), around)) {
                return true;
            }
        }
        return net.neoforged.neoforge.common.FarmlandWaterManager.hasBlockWaterTicket(level, pos);
    }
}
