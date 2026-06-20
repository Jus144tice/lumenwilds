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
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * The Lumen Geode's growing heart (v1.3 Phase E1) — a {@link BuddingAmethystBlock}-style block that slowly
 * grows Lumen Crystal buds on its exposed faces, ramping small → medium → large → cluster. A faithful port of
 * vanilla {@code BuddingAmethystBlock#randomTick} keyed to the Lumenwilds bud blocks (vanilla hard-codes its
 * own bud blocks, so a subclass can't be reused). Like vanilla budding amethyst it can't be harvested
 * ({@code noLootTable()} in {@link ModBlocks}), so geode crystal is renewable but the budding block isn't farmable.
 */
public class BuddingLumenCrystalBlock extends AmethystBlock {

    public static final MapCodec<BuddingLumenCrystalBlock> CODEC = simpleCodec(BuddingLumenCrystalBlock::new);
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingLumenCrystalBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public MapCodec<BuddingLumenCrystalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rng) {
        if (rng.nextInt(GROWTH_CHANCE) == 0) {
            Direction dir = DIRECTIONS[rng.nextInt(DIRECTIONS.length)];
            BlockPos target = pos.relative(dir);
            BlockState at = level.getBlockState(target);
            Block grow = null;
            if (canClusterGrowAtState(at)) {
                grow = ModBlocks.SMALL_LUMEN_CRYSTAL_BUD.get();
            } else if (at.is(ModBlocks.SMALL_LUMEN_CRYSTAL_BUD.get())
                    && at.getValue(AmethystClusterBlock.FACING) == dir) {
                grow = ModBlocks.MEDIUM_LUMEN_CRYSTAL_BUD.get();
            } else if (at.is(ModBlocks.MEDIUM_LUMEN_CRYSTAL_BUD.get())
                    && at.getValue(AmethystClusterBlock.FACING) == dir) {
                grow = ModBlocks.LARGE_LUMEN_CRYSTAL_BUD.get();
            } else if (at.is(ModBlocks.LARGE_LUMEN_CRYSTAL_BUD.get())
                    && at.getValue(AmethystClusterBlock.FACING) == dir) {
                grow = ModBlocks.LUMEN_CRYSTAL_CLUSTER.get();
            }
            if (grow != null) {
                BlockState grown = grow.defaultBlockState()
                        .setValue(AmethystClusterBlock.FACING, dir)
                        .setValue(
                                AmethystClusterBlock.WATERLOGGED,
                                at.getFluidState().getType() == Fluids.WATER);
                level.setBlockAndUpdate(target, grown);
            }
        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || (state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8);
    }
}
