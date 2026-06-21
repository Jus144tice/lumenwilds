/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Lumen Dirt Path (v1.4 Phase F1) — the Moonloam analog of vanilla's grass/dirt path. Shovel-flatten Lumen
 * Grass / Moonloam to make it (see {@code event.LumenFarmingEvents}); when a solid block is placed above it,
 * it reverts to <b>Moonloam</b> (not dirt). Mirrors {@link DirtPathBlock}, overriding the methods that hardcode
 * vanilla dirt.
 */
public class LumenDirtPathBlock extends DirtPathBlock {

    // Typed to the parent (DirtPathBlock narrowed codec()'s return type); target-typing still builds our block.
    public static final MapCodec<DirtPathBlock> CODEC = simpleCodec(LumenDirtPathBlock::new);

    public LumenDirtPathBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public MapCodec<DirtPathBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return !this.defaultBlockState().canSurvive(ctx.getLevel(), ctx.getClickedPos())
                ? Block.pushEntitiesUp(
                        this.defaultBlockState(),
                        ModBlocks.MOONLOAM.get().defaultBlockState(),
                        ctx.getLevel(),
                        ctx.getClickedPos())
                : super.getStateForPlacement(ctx);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState moonloam =
                Block.pushEntitiesUp(state, ModBlocks.MOONLOAM.get().defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, moonloam);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, moonloam));
    }
}
