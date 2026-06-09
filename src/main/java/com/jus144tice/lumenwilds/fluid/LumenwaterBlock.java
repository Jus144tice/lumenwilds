/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.fluid;

import com.jus144tice.lumenwilds.registry.ModFluids;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

/**
 * The placeable Lumenwater block. Identical to vanilla {@link LiquidBlock} except for the bible's
 * <b>anti-OP rule</b>: Lumenwater carried <em>out</em> of the Lumenwilds is unstable, so any Lumenwater
 * block outside the dimension slowly reverts to ordinary water (on a random tick). In-dimension the random
 * tick is a no-op, so there is no ongoing cost there.
 */
public class LumenwaterBlock extends LiquidBlock {

    // Typed as MapCodec<LiquidBlock> to match LiquidBlock#codec()'s invariant return; the factory still
    // builds a LumenwaterBlock, so round-tripping keeps the subtype.
    public static final MapCodec<LiquidBlock> CODEC =
            simpleCodec(props -> (LiquidBlock) new LumenwaterBlock(ModFluids.LUMENWATER.get(), props));

    public LumenwaterBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public MapCodec<LiquidBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.dimension().equals(LumenDimensionConstants.LUMENWILDS_LEVEL)) {
            // Outside the Lumenwilds: decay to ordinary water, preserving the flow level so it settles.
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState().setValue(LEVEL, state.getValue(LEVEL)));
        }
    }
}
