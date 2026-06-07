/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.world.LumenBiomeBootstrap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Convenience aggregator for the Lumenwilds biome resource keys.
 *
 * <p>Biomes are a datapack (worldgen) registry, not a {@code DeferredRegister}-backed one, so the
 * canonical keys live in {@link LumenBiomeBootstrap}. This class re-exports them so callers can use a
 * single {@code registry}-package entry point ({@code ModBiomes.LUMEN_MEADOW}) consistent with the
 * other {@code Mod*} registries.</p>
 */
public final class ModBiomes {

    private ModBiomes() {}

    public static final ResourceKey<Biome> LUMEN_MEADOW = LumenBiomeBootstrap.LUMEN_MEADOW;
    public static final ResourceKey<Biome> GLOWING_GROVE = LumenBiomeBootstrap.GLOWING_GROVE;
    public static final ResourceKey<Biome> MOONLIT_BARRENS = LumenBiomeBootstrap.MOONLIT_BARRENS;
}
