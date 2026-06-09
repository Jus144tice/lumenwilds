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
 * <p>Defined as datapack JSON under {@code data/lumenwilds/worldgen/configured_feature/}. Pair each with
 * a {@link LumenPlacedFeatures} entry (the "where to place" half) and reference the placed key from a
 * biome's feature list.</p>
 *
 * <p>Live (Phase 5b): {@link #LUMEN_CRYSTAL_ORE}, {@link #PATCH_MOONBLOSSOM}, {@link #PATCH_GLOW_FERN}.
 * {@link #GLOWWOOD_TREE} lands in Phase 5c.</p>
 */
public final class LumenConfiguredFeatures {

    private LumenConfiguredFeatures() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMEN_CRYSTAL_ORE = key("lumen_crystal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOONBLOSSOM = key("patch_moonblossom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GLOW_FERN = key("patch_glow_fern");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWWOOD_TREE = key("glowwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWROOT_TREE = key("glowroot_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWROOT_TREE_2X2 = key("glowroot_tree_2x2");
    // The Glowroot MEGA tree is a structure (registry.ModStructures); the two above are the ordinary
    // (1×1 / 2×2) Glowroot trees that spawn naturally, like spruce/jungle/dark-oak.

    // Phase 5d.2: a surface patch of Glasspetal Cluster crystals, used by the Glasspetal Crags biome.
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GLASSPETAL = key("patch_glasspetal");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
