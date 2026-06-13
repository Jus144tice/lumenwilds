/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Lumen Conduit (Phase 10c) — the thin glowing energy lines embedded in the floors and walls of the
 * Lumenwright cities. A {@code conduit_state} of {@link State#DEAD}/{@link State#DIM}/{@link State#ACTIVE}
 * drives its light level (0 / 2 / 8): in the ruins most conduits are dead or dim, hinting at an energy
 * network that has gone out.
 *
 * <p>Phase 10c is purely cosmetic — generated ruins place dead/dim conduits and the state never changes.
 * The functional <b>Resonance</b> network (a powered core lighting connected conduits, opening doors, etc.)
 * lands in Phase 10e, which will drive {@code conduit_state} dynamically.</p>
 */
public class LumenConduitBlock extends Block {

    public static final EnumProperty<State> CONDUIT_STATE = EnumProperty.create("conduit_state", State.class);

    public LumenConduitBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONDUIT_STATE, State.DEAD));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONDUIT_STATE);
    }

    /** Light level for a conduit's state — used by the block's {@code lightLevel} in {@code ModBlocks}. */
    public static int lightFor(BlockState state) {
        return switch (state.getValue(CONDUIT_STATE)) {
            case DEAD -> 0;
            case DIM -> 2;
            case ACTIVE -> 8;
        };
    }

    /** The three energy states of a Lumen Conduit. */
    public enum State implements StringRepresentable {
        DEAD("dead"),
        DIM("dim"),
        ACTIVE("active");

        private final String name;

        State(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
