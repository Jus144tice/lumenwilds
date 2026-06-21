/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * Duskbean (v1.4 F4) — the alien <b>darkness-loving</b> crop (4-stage, AGE_3). Inverting normal farming, it
 * only advances when the spot is dim/dark ({@code getMaxLocalRawBrightness <= MAX_LIGHT_TO_GROW}); bright light
 * stalls it. Otherwise a normal {@link LumenCropBlock} (grows on native soil or farmland; the harvested bean is
 * the seed).
 */
public class DuskbeanCropBlock extends LumenCropBlock {

    public static final MapCodec<DuskbeanCropBlock> CODEC = simpleCodec(DuskbeanCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final int MAX_LIGHT_TO_GROW = 7;

    public DuskbeanCropBlock(BlockBehaviour.Properties props) {
        super(props, () -> ModItems.DUSKBEAN.get());
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos) <= MAX_LIGHT_TO_GROW) {
            super.randomTick(state, level, pos, random); // the alien twist: grows in the dark
        }
    }
}
