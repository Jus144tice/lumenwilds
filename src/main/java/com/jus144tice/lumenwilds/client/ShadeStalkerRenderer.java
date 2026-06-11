/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.ShadeStalkerModel;
import com.jus144tice.lumenwilds.entity.ShadeStalker;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Shade Stalker with the bespoke {@link ShadeStalkerModel} (Phase 9b — a sleek low four-legged
 * predator, replacing the spider placeholder).
 */
public class ShadeStalkerRenderer extends MobRenderer<ShadeStalker, ShadeStalkerModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/shade_stalker.png");

    public ShadeStalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadeStalkerModel(context.bakeLayer(LumenModelLayers.SHADE_STALKER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadeStalker entity) {
        return TEXTURE;
    }
}
