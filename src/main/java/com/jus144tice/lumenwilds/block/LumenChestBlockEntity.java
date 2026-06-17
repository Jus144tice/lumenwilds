/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block entity for the glowing wood chests (Glowwood + Glowroot, v1.1.3). A thin {@code ChestBlockEntity}
 * subclass whose only job is to carry our own {@link ModBlockEntities#LUMEN_CHEST} type (so the bespoke
 * {@code client.LumenChestRenderer} can pick the right texture per species and render it fullbright). All
 * chest behaviour — inventory, double-chest pairing, lid animation, sounds — is inherited from vanilla.
 */
public class LumenChestBlockEntity extends ChestBlockEntity {

    public LumenChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LUMEN_CHEST.get(), pos, state);
    }
}
