/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.RootbackModel;
import com.jus144tice.lumenwilds.entity.Rootback;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Rootback with the bespoke {@link RootbackModel} (Phase 9b — a real domed-shell turtle built to
 * fill the ~3×2 hitbox at 1:1, replacing the scaled cow placeholder).
 */
public class RootbackRenderer extends MobRenderer<Rootback, RootbackModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/rootback.png");

    public RootbackRenderer(EntityRendererProvider.Context context) {
        super(context, new RootbackModel(context.bakeLayer(LumenModelLayers.ROOTBACK)), 1.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Rootback entity) {
        return TEXTURE;
    }
}
