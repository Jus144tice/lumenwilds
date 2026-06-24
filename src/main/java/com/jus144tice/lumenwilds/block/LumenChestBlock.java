/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The glowing wood chest block (v1.4.6 fix). Vanilla {@link ChestBlock#newBlockEntity} hardcodes a vanilla
 * {@link net.minecraft.world.level.block.entity.ChestBlockEntity} (type {@code minecraft:chest}) — it ignores
 * the block-entity-type supplier passed to its constructor. Registering Glowwood/Glowroot chests as a plain
 * {@code ChestBlock} therefore placed a {@code minecraft:chest} BE at a {@code lumenwilds:*_chest} block, and
 * the type↔block validation threw <b>{@code IllegalStateException: Invalid block entity ... got Block{...}}</b>
 * on placement — a hard crash.
 *
 * <p>This subclass overrides {@link #newBlockEntity} to create our {@link LumenChestBlockEntity} (carrying the
 * {@link ModBlockEntities#LUMEN_CHEST} type that <em>does</em> list these blocks), so placement validates, the
 * bespoke {@code client.LumenChestRenderer} is actually used (per-species glowing texture), and the chest's
 * lid animation / double-chest combine / menu all resolve against the matching type.</p>
 */
public class LumenChestBlock extends ChestBlock {

    public LumenChestBlock(Properties props) {
        super(props, ModBlockEntities.LUMEN_CHEST::get);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LumenChestBlockEntity(pos, state);
    }
}
