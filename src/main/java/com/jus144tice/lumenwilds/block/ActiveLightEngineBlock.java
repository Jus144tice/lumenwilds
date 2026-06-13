/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

/**
 * Active (restored) Dormant Light Engine (Phase 10e.2) — the city-scale centrepiece, brought back online by
 * feeding a {@code resonance_core_fragment} into a {@link DormantLightEngineBlock}. It <b>is</b> a Resonance
 * Core (extends {@link ResonanceCoreBlock}, reusing the network-flooding block entity + ticker), so restoring
 * it lights the plaza's conduits and opens its sealed doors — the city's fading heartbeat returns. Brighter
 * than a hand-built core. Shares the {@code RESONANCE_CORE} block-entity type (it's added to that type's
 * valid blocks in {@code ModBlockEntities}).
 */
public class ActiveLightEngineBlock extends ResonanceCoreBlock {

    public static final MapCodec<ActiveLightEngineBlock> CODEC = simpleCodec(ActiveLightEngineBlock::new);

    public ActiveLightEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
