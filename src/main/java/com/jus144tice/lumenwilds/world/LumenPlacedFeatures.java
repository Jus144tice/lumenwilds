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

    private static ResourceKey<PlacedFeature> key(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
