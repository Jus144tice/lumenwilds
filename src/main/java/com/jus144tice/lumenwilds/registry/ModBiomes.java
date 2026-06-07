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
 * single {@code registry}-package entry point ({@code ModBiomes.LUMEN_GLADE}) consistent with the
 * other {@code Mod*} registries.</p>
 */
public final class ModBiomes {

    private ModBiomes() {}

    public static final ResourceKey<Biome> LUMEN_GLADE = LumenBiomeBootstrap.LUMEN_GLADE;
    public static final ResourceKey<Biome> GLOWROOT_FOREST = LumenBiomeBootstrap.GLOWROOT_FOREST;
    public static final ResourceKey<Biome> MOONMIRE = LumenBiomeBootstrap.MOONMIRE;
    public static final ResourceKey<Biome> SPOREFALL_JUNGLE = LumenBiomeBootstrap.SPOREFALL_JUNGLE;
    public static final ResourceKey<Biome> GLASSPETAL_CRAGS = LumenBiomeBootstrap.GLASSPETAL_CRAGS;
    public static final ResourceKey<Biome> UNDERCROWN_CAVERNS = LumenBiomeBootstrap.UNDERCROWN_CAVERNS;
    public static final ResourceKey<Biome> STILLBLOOM_BASIN = LumenBiomeBootstrap.STILLBLOOM_BASIN;
}
