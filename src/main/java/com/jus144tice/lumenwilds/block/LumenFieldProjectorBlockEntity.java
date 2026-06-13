/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Drives a {@link LumenFieldProjectorBlock}'s gravity column (Phase 11b). Every {@link #TICK_INTERVAL} server
 * ticks it (re)projects the field via {@link LiftShaftNetwork} and clears any cell it owned last tick but no
 * longer does (so the column shrinks correctly when the shaft is blocked or a repeater is removed). The owned
 * set is transient — it's re-derived from the world each tick — so nothing is persisted; on removal/mode-flip
 * the column is cleared by walking it from the projector.
 */
public class LumenFieldProjectorBlockEntity extends BlockEntity {

    private static final int TICK_INTERVAL = 10;

    private List<BlockPos> owned = new ArrayList<>();

    public LumenFieldProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LUMEN_FIELD_PROJECTOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LumenFieldProjectorBlockEntity be) {
        if ((level.getGameTime() + (pos.getX() & 15)) % TICK_INTERVAL != 0) {
            return; // staggered by position so many projectors don't all flood on the same tick
        }
        if (!(state.getBlock() instanceof LumenFieldProjectorBlock)) {
            return;
        }
        LumenFieldProjectorBlock.Mode mode = state.getValue(LumenFieldProjectorBlock.MODE);
        List<BlockPos> now = LiftShaftNetwork.project(level, pos, mode);
        List<BlockPos> stale = new ArrayList<>();
        for (BlockPos p : be.owned) {
            if (!now.contains(p)) {
                stale.add(p);
            }
        }
        LiftShaftNetwork.clearStale(level, stale, mode);
        be.owned = now;
    }

    /** Clear this projector's whole column (on removal or before a mode flip) and forget what it owned. */
    public void clearField(Level level) {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof LumenFieldProjectorBlock) {
            LumenFieldProjectorBlock.Mode mode = state.getValue(LumenFieldProjectorBlock.MODE);
            LiftShaftNetwork.clearColumn(level, getBlockPos(), mode);
            LiftShaftNetwork.clearStale(level, owned, mode);
        }
        owned.clear();
    }
}
