/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * Cavecap (v1.4 F4) — the alien <b>stone-grown</b> crop (4-stage, AGE_3): a farmable cave fungus that grows
 * directly on Moonstone / Deep Moonstone / the strata rocks (and Moonloam), <b>no tilling needed</b> — farm it
 * in the Undercrown. Grows at any light (inherited from {@link LumenCropBlock}).
 */
public class CavecapCropBlock extends LumenCropBlock {

    public static final MapCodec<CavecapCropBlock> CODEC = simpleCodec(CavecapCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public CavecapCropBlock(BlockBehaviour.Properties props) {
        super(props, () -> ModItems.CAVECAP.get());
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

    /** Grows on cave rock (Moonstone family + strata) and Moonloam — not farmland. */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModBlocks.MOONSTONE.get())
                || state.is(ModBlocks.DEEP_MOONSTONE.get())
                || state.is(ModBlocks.VEINSTONE.get())
                || state.is(ModBlocks.PALE_TUFF.get())
                || state.is(ModBlocks.MOONLOAM.get());
    }
}
