/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.feature.GlasspetalGrowthFeature;
import com.jus144tice.lumenwilds.world.feature.GlowcapFeature;
import com.jus144tice.lumenwilds.world.feature.GlowrootTreeFeature;
import com.jus144tice.lumenwilds.world.feature.LumenReefFeature;
import com.jus144tice.lumenwilds.world.feature.LumenwaterPoolFeature;
import com.jus144tice.lumenwilds.world.feature.StillbloomFeature;
import com.jus144tice.lumenwilds.world.feature.UndercrownDecorFeature;
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

    /** The giant Stillbloom flower (Phase 5d.6) — stem column + petal dome + glowing core. */
    public static final DeferredHolder<Feature<?>, StillbloomFeature> STILLBLOOM =
            FEATURES.register("stillbloom", () -> new StillbloomFeature(NoneFeatureConfiguration.CODEC));

    /**
     * Small chunk-safe Lumenwater pool (replaces the vanilla {@code lake}, which crashed chunk-gen near
     * chunk borders). Used by the Moonmire + Undercrown pool configured features.
     */
    public static final DeferredHolder<Feature<?>, LumenwaterPoolFeature> LUMENWATER_POOL =
            FEATURES.register("lumenwater_pool", () -> new LumenwaterPoolFeature(NoneFeatureConfiguration.CODEC));

    /** A Lumen Reef — glowing coral mounds + fronds on the submerged seabed (Phase 9 drawing-board). */
    public static final DeferredHolder<Feature<?>, LumenReefFeature> LUMEN_REEF =
            FEATURES.register("lumen_reef", () -> new LumenReefFeature(NoneFeatureConfiguration.CODEC));

    /** Glasspetal crystal growths of varied size scattered across the Crags floor (Phase 9). */
    public static final DeferredHolder<Feature<?>, GlasspetalGrowthFeature> GLASSPETAL_GROWTH =
            FEATURES.register("glasspetal_growth", () -> new GlasspetalGrowthFeature(NoneFeatureConfiguration.CODEC));

    /** Decorates Undercrown cave surfaces — crystals on rock faces + glowing plants on floors (Phase 9). */
    public static final DeferredHolder<Feature<?>, UndercrownDecorFeature> UNDERCROWN_DECOR =
            FEATURES.register("undercrown_decor", () -> new UndercrownDecorFeature(NoneFeatureConfiguration.CODEC));

    /** The ordinary Glowcap mushroom — bell-shaped, size- and colour-varied (Phase 9). */
    public static final DeferredHolder<Feature<?>, GlowcapFeature> GLOWCAP =
            FEATURES.register("glowcap", () -> new GlowcapFeature(NoneFeatureConfiguration.CODEC));

    private ModFeatures() {}
}
