/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.GlowmothModel;
import com.jus144tice.lumenwilds.entity.Glowmoth;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Glowmoth with the bespoke {@link GlowmothModel} (Phase 9b — a real moth with fluttering wings,
 * replacing the scaled-endermite placeholder).
 */
public class GlowmothRenderer extends MobRenderer<Glowmoth, GlowmothModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/glowmoth.png");

    public GlowmothRenderer(EntityRendererProvider.Context context) {
        super(context, new GlowmothModel(context.bakeLayer(LumenModelLayers.GLOWMOTH)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(Glowmoth entity) {
        return TEXTURE;
    }
}
