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

    // Phase 5d.2: surface Glasspetal Cluster patches, used by the Glasspetal Crags biome.
    public static final ResourceKey<PlacedFeature> PATCH_GLASSPETAL = key("patch_glasspetal");

    // Phase 5d.3: Giant Glowcap mushrooms, used by the Sporefall Jungle biome.
    public static final ResourceKey<PlacedFeature> GIANT_GLOWCAP = key("giant_glowcap");

    // Phase 5d.4: Moonmire — Lumenwater pools + reed/algae ground cover.
    public static final ResourceKey<PlacedFeature> LUMENWATER_POOL = key("lumenwater_pool");
    public static final ResourceKey<PlacedFeature> PATCH_GLOW_ALGAE = key("patch_glow_algae");
    public static final ResourceKey<PlacedFeature> PATCH_LUMEN_REEDS = key("patch_lumen_reeds");

    // Phase 5d.5: Undercrown Caverns — deep glow (crystal + glowvine veins) and underground Lumenwater pools.
    public static final ResourceKey<PlacedFeature> UNDERCROWN_GLOWVINE = key("undercrown_glowvine");
    public static final ResourceKey<PlacedFeature> UNDERCROWN_CRYSTAL = key("undercrown_crystal");
    public static final ResourceKey<PlacedFeature> UNDERCROWN_POOL = key("undercrown_pool");

    // Phase 5d.6: the giant Stillbloom flower, used by the Stillbloom Basin biome.
    public static final ResourceKey<PlacedFeature> STILLBLOOM = key("stillbloom");

    private static ResourceKey<PlacedFeature> key(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
