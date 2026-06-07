/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world;

/**
 * Central place to programmatically bootstrap the Lumenwilds worldgen registries (dimension type,
 * level stem, biomes, configured/placed features, noise settings) when/if the mod moves off pure
 * datapack JSON.
 *
 * <p>Phase 1: intentionally empty. The dimension is currently defined entirely by JSON under
 * {@code src/main/resources/data/lumenwilds/} (see {@link LumenDimensionConstants}).</p>
 *
 * <p>TODO (Phase 2/3): if we switch to code-generated worldgen, add static {@code bootstrap(...)}
 * methods taking a {@code BootstrapContext<T>} for each registry and wire them into a
 * {@code RegistrySetBuilder}, then expose them through {@code DataGenerators} via
 * {@code DatapackBuiltinEntriesProvider}. Keeping the seam here avoids scattering worldgen code.</p>
 */
public final class LumenWorldgenBootstrap {

    private LumenWorldgenBootstrap() {}

    // TODO: public static void bootstrapDimensionType(BootstrapContext<DimensionType> ctx) { ... }
    // TODO: public static void bootstrapLevelStem(BootstrapContext<LevelStem> ctx) { ... }
    // TODO: public static void bootstrapBiomes(BootstrapContext<Biome> ctx) { ... }
}
