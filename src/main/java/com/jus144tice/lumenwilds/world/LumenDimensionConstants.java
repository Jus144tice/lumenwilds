/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * Canonical resource keys for the Lumenwilds dimension.
 *
 * <p>The dimension itself is data-driven for now (see
 * {@code src/main/resources/data/lumenwilds/dimension/lumenwilds.json} and
 * {@code .../dimension_type/lumenwilds.json}); these keys are the Java-side handles used by portal
 * teleport code, low-gravity gating, and any future bootstrap. The path is {@code "lumenwilds"} so
 * every key resolves to {@code lumenwilds:lumenwilds}.</p>
 */
public final class LumenDimensionConstants {

    private LumenDimensionConstants() {}

    /** The dimension's path within the mod namespace → {@code lumenwilds:lumenwilds}. */
    public static final String DIMENSION_PATH = "lumenwilds";

    /** {@link Level} key — identifies the runtime {@code ServerLevel} (the destination of the portal). */
    public static final ResourceKey<Level> LUMENWILDS_LEVEL =
            ResourceKey.create(Registries.DIMENSION, ResourceLocationHelper.modLoc(DIMENSION_PATH));

    /** {@link LevelStem} key — the worldgen "stem" (dimension type + chunk generator) loaded from JSON. */
    public static final ResourceKey<LevelStem> LUMENWILDS_STEM =
            ResourceKey.create(Registries.LEVEL_STEM, ResourceLocationHelper.modLoc(DIMENSION_PATH));

    /** {@link DimensionType} key — lighting, height, coordinate scale, etc. (loaded from JSON). */
    public static final ResourceKey<DimensionType> LUMENWILDS_DIM_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocationHelper.modLoc(DIMENSION_PATH));

    // TODO (Phase 2): if/when terrain moves to a custom NoiseGeneratorSettings, add:
    //   ResourceKey<NoiseGeneratorSettings> LUMENWILDS_NOISE =
    //       ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocationHelper.modLoc("lumenwilds"));
    // For now the dimension JSON reuses minecraft:overworld noise settings as a safe placeholder.
}
