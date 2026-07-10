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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A gourd stem that grows on the Lumenwilds' native soil, not just vanilla farmland. Vanilla
 * {@code StemBlock#mayPlaceOn} accepts ONLY {@code minecraft:farmland}, so a Glowgourd/Moonmelon stem planted
 * on <b>Lumen Farmland</b> (a different block) or Moonloam failed {@code canSurvive} and broke instantly — the
 * "glowgourd seeds vanish / show blank on hoed ground" bug (v1.5.0). This accepts any {@link FarmBlock} (Lumen
 * Farmland) plus Moonloam / Lumen Grass, matching {@link LumenCropBlock}'s soil leniency.
 */
public class LumenStemBlock extends StemBlock {

    public LumenStemBlock(
            ResourceKey<Block> fruit,
            ResourceKey<Block> attachedStem,
            ResourceKey<Item> seed,
            BlockBehaviour.Properties props) {
        super(fruit, attachedStem, seed, props);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmBlock
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.LUMEN_GRASS_BLOCK.get());
    }
}
