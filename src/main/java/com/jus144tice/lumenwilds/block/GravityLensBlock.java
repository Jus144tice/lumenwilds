/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Gravity Lens (Phase 10e.2) — a Lumenwright gravity device: a Shimmerstone frame around a suspended
 * blue-violet lens. While {@link #POWERED} by the Resonance network (an adjacent active conduit or core, set
 * by {@link ResonanceNetwork}), it gently lifts entities in the column above it (see
 * {@code event.LumenGravityEvents}) and brightens (light 6; dim 2 when dead).
 *
 * <p>This is the standalone, single-block gravity effect. The town-scale paired descent/ascension liftshafts
 * + the craftable Lumen Field Projector ({@code docs/lumenwright_liftshafts.txt}) are a later feature that
 * reuses this block's {@code gravity_lens_fragment} and the same velocity-field philosophy.</p>
 */
public class GravityLensBlock extends Block {

    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public GravityLensBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public static int lightFor(BlockState state) {
        return state.getValue(POWERED) ? 6 : 2;
    }
}
