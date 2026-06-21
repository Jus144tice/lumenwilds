/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/** Moonbeet (v1.4 F2) — the beetroot-analog (short 4-stage cycle, AGE_3). Drops Moonbeet + seeds. */
public class MoonbeetCropBlock extends LumenCropBlock {

    public static final MapCodec<MoonbeetCropBlock> CODEC = simpleCodec(MoonbeetCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public MoonbeetCropBlock(BlockBehaviour.Properties props) {
        super(props, () -> ModItems.MOONBEET_SEEDS.get());
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
    protected int getBonemealAgeIncrease(Level level) {
        return super.getBonemealAgeIncrease(level) / 3;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
