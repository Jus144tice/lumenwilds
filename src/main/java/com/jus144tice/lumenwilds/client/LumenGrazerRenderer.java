/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.LumenGrazer;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Lumen Grazer. <b>Placeholder:</b> reuses the vanilla {@link CowModel} (the already-registered
 * {@code ModelLayers.COW} layer) with a Lumenwilds texture, so no bespoke {@code LayerDefinition} is needed
 * yet. The final six-legged model + emissive night-glow layer are a Phase 9 art task.
 */
public class LumenGrazerRenderer extends MobRenderer<LumenGrazer, CowModel<LumenGrazer>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lumen_grazer.png");

    public LumenGrazerRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(LumenGrazer entity) {
        return TEXTURE;
    }
}
