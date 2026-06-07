/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Biome resource keys for the Lumenwilds, plus the future home of programmatic biome bootstrap.
 *
 * <p>Phase 1: just the keys. The placeholder dimension JSON currently uses a vanilla fixed biome, so
 * none of these are wired into worldgen yet.</p>
 *
 * <p>TODO (Phase 3): define each {@link Biome} either as datapack JSON under
 * {@code data/lumenwilds/worldgen/biome/} or via a {@code RegistrySetBuilder} bootstrap registered in
 * {@code DataGenerators} (DatapackBuiltinEntriesProvider). Tune fog/sky/water colours, ambient light,
 * particles, mob spawns and features per biome.</p>
 */
public final class LumenBiomeBootstrap {

    private LumenBiomeBootstrap() {}

    // The bible's seven biomes. Lumen Glade is the baseline arrival biome (live as of Phase 5a); the
    // rest get their definitions/terrain in Phase 5d.
    public static final ResourceKey<Biome> LUMEN_GLADE = key("lumen_glade");
    public static final ResourceKey<Biome> GLOWROOT_FOREST = key("glowroot_forest");
    public static final ResourceKey<Biome> MOONMIRE = key("moonmire");
    public static final ResourceKey<Biome> SPOREFALL_JUNGLE = key("sporefall_jungle");
    public static final ResourceKey<Biome> GLASSPETAL_CRAGS = key("glasspetal_crags");
    public static final ResourceKey<Biome> UNDERCROWN_CAVERNS = key("undercrown_caverns");
    public static final ResourceKey<Biome> STILLBLOOM_BASIN = key("stillbloom_basin");

    private static ResourceKey<Biome> key(String path) {
        return ResourceKey.create(Registries.BIOME, ResourceLocationHelper.modLoc(path));
    }
}
