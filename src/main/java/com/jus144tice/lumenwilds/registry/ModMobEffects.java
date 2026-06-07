/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Mob effects (potion-style status effects) added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO (Phase 4+): candidate effects — "Lumen Sickness" (overexposure to native light), a
 * glow/marking effect, a low-gravity buff source, sporefall affliction, etc.</p>
 */
public final class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Lumenwilds.MOD_ID);

    private ModMobEffects() {}
}
