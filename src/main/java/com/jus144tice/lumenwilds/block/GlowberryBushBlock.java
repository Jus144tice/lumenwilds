/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Glowberry Bush (v1.1c) — a renewable, plantable alien berry bush modelled on vanilla
 * {@code SweetBerryBushBlock}: an {@code AGE 0..3} bush that ripens over time (and via bone meal), glows a
 * little brighter as it matures, and is harvested by right-clicking a mature bush (empty hand) — popping
 * 1–2 {@link ModItems#GLOWBERRY} and reverting to age 1, so it regrows without replanting. Planted by
 * right-clicking valid soil with a Glowberry (the item is an {@code ItemNameBlockItem} over this block).
 *
 * <p>The previous Phase-9 incarnation was a plain {@code TallGrassBlock} with a right-click-harvest event
 * (now removed): you couldn't plant it and it had no growth. This block fixes both.</p>
 */
public class GlowberryBushBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<GlowberryBushBlock> CODEC = simpleCodec(GlowberryBushBlock::new);
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final VoxelShape SAPLING_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    private static final VoxelShape MID_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public GlowberryBushBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    /** Light level by age (3 → 6) — wired in {@code ModBlocks} so the bush glows brighter as it ripens. */
    public static int lightFor(BlockState state) {
        return 3 + state.getValue(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        int age = state.getValue(AGE);
        if (age == 0) {
            return SAPLING_SHAPE;
        }
        return age < MAX_AGE ? MID_SHAPE : super.getShape(state, level, pos, ctx);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE && random.nextInt(5) == 0 && level.getRawBrightness(pos.above(), 0) >= 9) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE) {
            return super.useWithoutItem(state, level, pos, player, hit);
        }
        int count = 1 + level.getRandom().nextInt(2);
        Block.popResource(level, pos, new ItemStack(ModItems.GLOWBERRY.get(), count));
        level.playSound(
                null,
                pos,
                SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                SoundSource.BLOCKS,
                1.0F,
                0.8F + level.getRandom().nextFloat() * 0.4F);
        level.setBlock(pos, state.setValue(AGE, 1), Block.UPDATE_CLIENTS);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // --- BonemealableBlock: bone meal advances the age by one ------------------------------------

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Math.min(MAX_AGE, state.getValue(AGE) + 1);
        level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_CLIENTS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
