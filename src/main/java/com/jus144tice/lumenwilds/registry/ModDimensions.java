/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * Convenience aggregator for the Lumenwilds dimension resource keys.
 *
 * <p>The dimension is data-driven (datapack registries), so the canonical keys live in
 * {@link LumenDimensionConstants}. This class re-exports them under the {@code registry} package for
 * consistency with the other {@code Mod*} entry points.</p>
 */
public final class ModDimensions {

    private ModDimensions() {}

    public static final ResourceKey<Level> LUMENWILDS_LEVEL = LumenDimensionConstants.LUMENWILDS_LEVEL;
    public static final ResourceKey<LevelStem> LUMENWILDS_STEM = LumenDimensionConstants.LUMENWILDS_STEM;
    public static final ResourceKey<DimensionType> LUMENWILDS_DIM_TYPE = LumenDimensionConstants.LUMENWILDS_DIM_TYPE;
}
