/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * {@link ConfiguredFeature} resource keys for the Lumenwilds (the "what to place" half of worldgen).
 *
 * <p>Phase 1: keys only. Configured features are data-driven — define them under
 * {@code data/lumenwilds/worldgen/configured_feature/} or via a {@code RegistrySetBuilder} bootstrap.
 * Pair each with a {@link LumenPlacedFeatures} entry (the "where to place" half).</p>
 *
 * <p>TODO (Phase 3): glowwood trees, glowroot clusters, moonblossom patches, lumenbulb scatters,
 * lumen-crystal ore veins.</p>
 */
public final class LumenConfiguredFeatures {

    private LumenConfiguredFeatures() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWWOOD_TREE = key("glowwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOONBLOSSOM_PATCH = key("moonblossom_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMEN_CRYSTAL_ORE = key("lumen_crystal_ore");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
