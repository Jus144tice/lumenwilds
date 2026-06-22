/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Wooden Post (v1.4.2) — a thin 6×6 waterloggable pillar (Quark-style decorative post), built from a
 * stripped log. Extends {@link RotatedPillarBlock} (3-axis from the clicked face) + waterlogging.
 */
public class WoodPostBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    // Typed to the parent (RotatedPillarBlock narrows codec()); target-typing still builds our block.
    public static final MapCodec<RotatedPillarBlock> CODEC = simpleCodec(WoodPostBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape Y_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape X_SHAPE = Block.box(0.0, 5.0, 5.0, 16.0, 11.0, 11.0);
    private static final VoxelShape Z_SHAPE = Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 16.0);

    public WoodPostBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public MapCodec<RotatedPillarBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(AXIS)) {
            case X -> X_SHAPE;
            case Z -> Z_SHAPE;
            default -> Y_SHAPE;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean waterlogged = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(AXIS, ctx.getClickedFace().getAxis())
                .setValue(WATERLOGGED, waterlogged);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction dir,
            BlockState neighbor,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
