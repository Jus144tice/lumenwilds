/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * {@link PlacedFeature} resource keys for the Lumenwilds (the "where/how often to place" half of
 * worldgen). Each typically wraps a {@link LumenConfiguredFeatures} entry with placement modifiers
 * (count, rarity, height range, biome filter).
 *
 * <p>Defined under {@code data/lumenwilds/worldgen/placed_feature/} and referenced from biome feature
 * lists. Placed features share the same path as their configured feature (different registries). Live
 * (Phase 5b): {@link #LUMEN_CRYSTAL_ORE}, {@link #PATCH_MOONBLOSSOM}, {@link #PATCH_GLOW_FERN}.</p>
 */
public final class LumenPlacedFeatures {

    private LumenPlacedFeatures() {}

    public static final ResourceKey<PlacedFeature> LUMEN_CRYSTAL_ORE = key("lumen_crystal_ore");
    public static final ResourceKey<PlacedFeature> PATCH_MOONBLOSSOM = key("patch_moonblossom");
    public static final ResourceKey<PlacedFeature> PATCH_GLOW_FERN = key("patch_glow_fern");
    public static final ResourceKey<PlacedFeature> GLOWWOOD_TREE = key("glowwood_tree");
    public static final ResourceKey<PlacedFeature> GLOWROOT_TREE = key("glowroot_tree");
    public static final ResourceKey<PlacedFeature> GLOWROOT_TREE_2X2 = key("glowroot_tree_2x2");
    // The Glowroot MEGA tree is a structure (registry.ModStructures); the two above are the ordinary
    // (1×1 / 2×2) Glowroot trees that spawn naturally.

    // Phase 5d.1: forest-density placement of the 2×2 Glowroot tree, used by the Glowroot Forest biome.
    public static final ResourceKey<PlacedFeature> GLOWROOT_FOREST_TREES = key("glowroot_forest_trees");

    private static ResourceKey<PlacedFeature> key(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
