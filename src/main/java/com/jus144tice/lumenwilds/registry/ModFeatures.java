/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.feature.GlowrootTreeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom {@link Feature} types added by The Lumenwilds (the worldgen <em>logic</em>, as opposed to the
 * data-driven configured/placed feature keys in {@code world.LumenConfiguredFeatures} /
 * {@code world.LumenPlacedFeatures}). Registered to the mod bus in {@link Lumenwilds}.
 */
public final class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Lumenwilds.MOD_ID);

    /** The ordinary 2×2 Glowroot tree (a scaled-down spreading version of the mega-tree shape). */
    public static final DeferredHolder<Feature<?>, GlowrootTreeFeature> GLOWROOT_TREE_2X2 =
            FEATURES.register("glowroot_tree_2x2", () -> new GlowrootTreeFeature(NoneFeatureConfiguration.CODEC));

    private ModFeatures() {}
}
