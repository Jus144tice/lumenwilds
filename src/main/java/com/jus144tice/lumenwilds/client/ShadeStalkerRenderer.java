/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.ShadeStalker;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Shade Stalker. <b>Placeholder:</b> reuses the vanilla {@link SpiderModel} (the
 * already-registered {@code ModelLayers.SPIDER}) with a dark Lumenwilds texture, so no bespoke
 * {@code LayerDefinition} is needed — a creepy dark silhouette stands in for the final thin quadruped with
 * glowing eyes (Phase 9 art).
 */
public class ShadeStalkerRenderer extends MobRenderer<ShadeStalker, SpiderModel<ShadeStalker>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/shade_stalker.png");

    public ShadeStalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadeStalker entity) {
        return TEXTURE;
    }
}
