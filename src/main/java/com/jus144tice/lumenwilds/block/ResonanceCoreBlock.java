/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Resonance Core (Phase 10e) — an ancient power source. A placed (player-crafted, or restored) core is
 * "on": its {@link ResonanceCoreBlockEntity} floods power through connected Lumen Conduits and opens the
 * ancient doors they reach (see {@link ResonanceNetwork}). Glows steadily. Removing it tears the network
 * down ({@link ResonanceCoreBlockEntity#shutdown}).
 */
public class ResonanceCoreBlock extends BaseEntityBlock {

    public static final MapCodec<ResonanceCoreBlock> CODEC = simpleCodec(ResonanceCoreBlock::new);

    public ResonanceCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResonanceCoreBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? null
                : createTickerHelper(type, ModBlockEntities.RESONANCE_CORE.get(), ResonanceCoreBlockEntity::serverTick);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof ResonanceCoreBlockEntity be) {
                be.shutdown(level);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
}
