/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Fluids added by The Lumenwilds (Phase 5e). <b>Lumenwater</b> — the dimension's native glowing water —
 * is a vanilla-style still/flowing pair built on NeoForge's {@link BaseFlowingFluid}. The non-state
 * properties (light, motion) live in {@link ModFluidTypes#LUMENWATER_TYPE}; the placeable block is
 * {@code fluid.LumenwaterBlock} (registered as {@link ModBlocks#LUMENWATER_BLOCK}) and the bucket is
 * {@link ModItems#LUMENWATER_BUCKET}.
 *
 * <p>{@link #props()} ties type + still + flowing + block + bucket together; it is built lazily (at
 * fluid-construction time) so the still/flowing holders it references are already declared — avoiding an
 * illegal static forward reference.</p>
 */
public final class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Lumenwilds.MOD_ID);

    public static final DeferredHolder<Fluid, FlowingFluid> LUMENWATER =
            FLUIDS.register("lumenwater", () -> new BaseFlowingFluid.Source(props()));

    public static final DeferredHolder<Fluid, FlowingFluid> LUMENWATER_FLOWING =
            FLUIDS.register("flowing_lumenwater", () -> new BaseFlowingFluid.Flowing(props()));

    private static BaseFlowingFluid.Properties properties;

    /** Lazily-built shared properties wiring type ↔ still ↔ flowing ↔ block ↔ bucket; water-like flow. */
    private static BaseFlowingFluid.Properties props() {
        if (properties == null) {
            properties = new BaseFlowingFluid.Properties(ModFluidTypes.LUMENWATER_TYPE, LUMENWATER, LUMENWATER_FLOWING)
                    .block(ModBlocks.LUMENWATER_BLOCK)
                    .bucket(ModItems.LUMENWATER_BUCKET)
                    .slopeFindDistance(4)
                    .levelDecreasePerBlock(1)
                    .tickRate(5);
        }
        return properties;
    }

    private ModFluids() {}
}
