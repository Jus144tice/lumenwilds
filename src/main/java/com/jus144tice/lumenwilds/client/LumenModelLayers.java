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
    public static final ModelLayerLocation GLOWMOTH =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("glowmoth"), "main");
    public static final ModelLayerLocation CRAG_WRAITH =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("crag_wraith"), "main");
    public static final ModelLayerLocation LANTERN_BEETLE =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("lantern_beetle"), "main");
    public static final ModelLayerLocation SPORELING =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("sporeling"), "main");
    public static final ModelLayerLocation SHADE_STALKER =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("shade_stalker"), "main");
    public static final ModelLayerLocation LUMEN_GRAZER =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("lumen_grazer"), "main");
    public static final ModelLayerLocation ROOTBACK =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("rootback"), "main");
    public static final ModelLayerLocation MIRELURKER =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("mirelurker"), "main");
    public static final ModelLayerLocation LUMEN_FISH =
            new ModelLayerLocation(ResourceLocationHelper.modLoc("lumen_fish"), "main");

    private LumenModelLayers() {}
}
