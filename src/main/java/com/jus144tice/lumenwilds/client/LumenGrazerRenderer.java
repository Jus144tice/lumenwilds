/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.LumenGrazerModel;
import com.jus144tice.lumenwilds.entity.LumenGrazer;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Lumen Grazer with the bespoke {@link LumenGrazerModel} (Phase 9b — the six-legged herd
 * herbivore, replacing the cow placeholder).
 */
public class LumenGrazerRenderer extends MobRenderer<LumenGrazer, LumenGrazerModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lumen_grazer.png");

    public LumenGrazerRenderer(EntityRendererProvider.Context context) {
        super(context, new LumenGrazerModel(context.bakeLayer(LumenModelLayers.LUMEN_GRAZER)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(LumenGrazer entity) {
        return TEXTURE;
    }
}
