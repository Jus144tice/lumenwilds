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
 * <p>Phase 1: keys only — define the placements under {@code data/lumenwilds/worldgen/placed_feature/}
 * (or a {@code RegistrySetBuilder} bootstrap) in Phase 3, then reference them from the biome JSON.</p>
 */
public final class LumenPlacedFeatures {

    private LumenPlacedFeatures() {}

    public static final ResourceKey<PlacedFeature> GLOWWOOD_TREE_PLACED = key("glowwood_tree_placed");
    public static final ResourceKey<PlacedFeature> MOONBLOSSOM_PATCH_PLACED = key("moonblossom_patch_placed");
    public static final ResourceKey<PlacedFeature> LUMEN_CRYSTAL_ORE_PLACED = key("lumen_crystal_ore_placed");

    private static ResourceKey<PlacedFeature> key(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
