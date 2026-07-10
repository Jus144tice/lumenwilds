/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The attached (fruit-bearing) counterpart of {@link LumenStemBlock}. Vanilla {@code AttachedStemBlock#mayPlaceOn}
 * likewise accepts only {@code minecraft:farmland}, so once a gourd formed the attached stem would break on
 * Lumen Farmland. Same soil leniency as {@link LumenStemBlock}.
 */
public class LumenAttachedStemBlock extends AttachedStemBlock {

    public LumenAttachedStemBlock(
            ResourceKey<Block> stem,
            ResourceKey<Block> fruit,
            ResourceKey<Item> seed,
            BlockBehaviour.Properties props) {
        super(stem, fruit, seed, props);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.LUMEN_GRASS_BLOCK.get());
    }
}
