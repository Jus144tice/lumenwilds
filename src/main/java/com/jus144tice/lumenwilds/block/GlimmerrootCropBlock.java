/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Glimmerroot (v1.4 F2) — the carrot-analog root (8 stages). The harvested Glimmerroot IS the seed (edible). */
public class GlimmerrootCropBlock extends LumenCropBlock {

    public static final MapCodec<GlimmerrootCropBlock> CODEC = simpleCodec(GlimmerrootCropBlock::new);

    public GlimmerrootCropBlock(BlockBehaviour.Properties props) {
        super(props, () -> ModItems.GLIMMERROOT.get());
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }
}
