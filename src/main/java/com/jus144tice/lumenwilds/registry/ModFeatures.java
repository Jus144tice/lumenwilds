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
 * {@code world.LumenPlacedFeatures}).
 *
 * <p>Empty again as of Phase 5c-2: the Glowroot mega tree moved to a <em>structure</em>
 * ({@code registry.ModStructures}) so it can span chunks. Intentionally NOT wired to the mod bus while
 * empty; add {@code ModFeatures.FEATURES.register(modBus)} in {@link Lumenwilds} when the first custom
 * feature is added.</p>
 */
public final class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Lumenwilds.MOD_ID);

    private ModFeatures() {}
}
