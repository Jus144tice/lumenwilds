/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Lumengrain (v1.4 F2) — the wheat-analog staple grain (8 stages). Drops Lumengrain + seeds. */
public class LumengrainCropBlock extends LumenCropBlock {

    public static final MapCodec<LumengrainCropBlock> CODEC = simpleCodec(LumengrainCropBlock::new);

    public LumengrainCropBlock(BlockBehaviour.Properties props) {
        super(props, () -> ModItems.LUMENGRAIN_SEEDS.get());
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }
}
