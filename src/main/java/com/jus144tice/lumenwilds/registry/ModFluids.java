/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Fluids added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO (Phase 3): add "Lumenwater" — a still/flowing fluid pair plus its fluid type, bucket item,
 * and liquid block. Lumenwater is intended to glow faintly and possibly buoy entities.</p>
 */
public final class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Lumenwilds.MOD_ID);

    private ModFluids() {}
}
