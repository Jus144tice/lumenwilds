/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom {@link Feature} types added by The Lumenwilds (the worldgen <em>logic</em>, as opposed to the
 * data-driven configured/placed feature keys in {@code world.LumenConfiguredFeatures} /
 * {@code world.LumenPlacedFeatures}). Empty for Phase 1 — vanilla feature types cover the early
 * placeholders.
 *
 * <p>TODO (Phase 3): register custom features only if vanilla ones can't express a structure (e.g. a
 * bespoke glowwood canopy or a living-light cluster). This DeferredRegister is intentionally NOT
 * wired to the mod bus yet because it is empty; add {@code ModFeatures.FEATURES.register(modBus)} in
 * {@link Lumenwilds} when the first feature is added.</p>
 */
public final class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Lumenwilds.MOD_ID);

    private ModFeatures() {}
}
