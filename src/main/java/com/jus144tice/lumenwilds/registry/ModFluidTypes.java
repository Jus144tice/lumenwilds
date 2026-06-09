/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * {@link FluidType}s added by The Lumenwilds (Phase 5e). A {@code FluidType} carries the non-state
 * physical/visual properties of a fluid (light, density, motion, sounds); the actual still/flowing
 * {@code Fluid}s live in {@link ModFluids} and reference this.
 *
 * <p><b>Lumenwater</b> is the dimension's native glowing water (light 4). It is anti-OP: placed outside
 * the Lumenwilds it slowly decays to ordinary water (see {@code fluid.LumenwaterBlock}).</p>
 */
public final class ModFluidTypes {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Lumenwilds.MOD_ID);

    /** Lumenwater — glows faintly (light 4); otherwise water-like motion. Cannot form new sources. */
    public static final DeferredHolder<FluidType, FluidType> LUMENWATER_TYPE = FLUID_TYPES.register(
            "lumenwater",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid_type.lumenwilds.lumenwater")
                    .lightLevel(4)
                    .canConvertToSource(false)
                    .supportsBoating(true)));

    private ModFluidTypes() {}
}
