/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.CragWraithModel;
import com.jus144tice.lumenwilds.entity.CragWraith;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Crag Wraith with the bespoke {@link CragWraithModel} (Phase 9b — a manta-like flier with
 * sweeping wings + tail, replacing the scaled-ghast placeholder).
 */
public class CragWraithRenderer extends MobRenderer<CragWraith, CragWraithModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/crag_wraith.png");

    public CragWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new CragWraithModel(context.bakeLayer(LumenModelLayers.CRAG_WRAITH)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CragWraith entity) {
        return TEXTURE;
    }
}
