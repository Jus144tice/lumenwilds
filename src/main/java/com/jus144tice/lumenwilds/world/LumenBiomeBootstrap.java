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

    public static final ResourceKey<Biome> LUMEN_MEADOW = key("lumen_meadow");
    public static final ResourceKey<Biome> GLOWING_GROVE = key("glowing_grove");
    public static final ResourceKey<Biome> MOONLIT_BARRENS = key("moonlit_barrens");

    private static ResourceKey<Biome> key(String path) {
        return ResourceKey.create(Registries.BIOME, ResourceLocationHelper.modLoc(path));
    }
}
