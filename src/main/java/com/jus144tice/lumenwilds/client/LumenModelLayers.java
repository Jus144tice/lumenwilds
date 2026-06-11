/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;

/**
 * {@link ModelLayerLocation}s for the Lumenwilds' bespoke entity models (Phase 9b). Each is registered (with
 * its {@code createBodyLayer}) in {@code LumenwildsClient#onRegisterLayerDefinitions} and baked in the mob's
 * renderer. Replaces the vanilla-model placeholders one mob at a time.
 */
public final class LumenModelLayers {

    public static final ModelLayerLocation SKY_JELLY =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("sky_jelly"), "main");

    private LumenModelLayers() {}
}
