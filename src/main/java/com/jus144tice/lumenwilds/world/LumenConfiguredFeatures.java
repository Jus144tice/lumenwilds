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

    // Phase 10a: Luminite ore (the Lumenwright structural metal) in moonstone + deep moonstone.
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMINITE_ORE = key("luminite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOONBLOSSOM = key("patch_moonblossom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GLOW_FERN = key("patch_glow_fern");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWWOOD_TREE = key("glowwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWROOT_TREE = key("glowroot_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWROOT_TREE_2X2 = key("glowroot_tree_2x2");
    // The Glowroot MEGA tree is a structure (registry.ModStructures); the two above are the ordinary
    // (1×1 / 2×2) Glowroot trees that spawn naturally, like spruce/jungle/dark-oak.

    // Phase 5d.2: a surface patch of Glasspetal Cluster crystals, used by the Glasspetal Crags biome.
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GLASSPETAL = key("patch_glasspetal");

    // v1.2.1: Shimmerstone ore-blobs in the Glasspetal Crags (moonstone/deep_moonstone → shimmerstone) so the
    // Shimmerstone set + the liftshaft craftables are obtainable without first looting a Glasspetal Spire.
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHIMMERSTONE_ORE = key("shimmerstone_ore");

    // Phase 5d.3: the Giant Glowcap mushroom (vanilla huge_brown_mushroom feature), used by Sporefall Jungle.
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_GLOWCAP = key("giant_glowcap");

    // Phase 5d.4: Moonmire — glowing Lumenwater pools (vanilla lake feature) + reed/algae ground cover.
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMENWATER_POOL = key("lumenwater_pool");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GLOW_ALGAE = key("patch_glow_algae");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LUMEN_REEDS = key("patch_lumen_reeds");

    // Phase 5d.5: Undercrown Caverns — glowing Glowvine veins threaded through the deep rock.
    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERCROWN_GLOWVINE = key("undercrown_glowvine");

    // Phase 5d.6: the giant Stillbloom flower (custom StillbloomFeature), used by the Stillbloom Basin.
    public static final ResourceKey<ConfiguredFeature<?, ?>> STILLBLOOM = key("stillbloom");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocationHelper.modLoc(path));
    }
}
