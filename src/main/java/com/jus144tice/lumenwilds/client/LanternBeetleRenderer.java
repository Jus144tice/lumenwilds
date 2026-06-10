/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.LanternBeetle;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Lantern Beetle. <b>Placeholder:</b> reuses the vanilla {@link SilverfishModel} (the
 * already-registered {@code ModelLayers.SILVERFISH}) with a glowing Lumenwilds texture — a small bug
 * silhouette stands in for the final winged glowing beetle (Phase 9 art, incl. the emissive lantern glow).
 */
public class LanternBeetleRenderer extends MobRenderer<LanternBeetle, SilverfishModel<LanternBeetle>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lantern_beetle.png");

    public LanternBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverfishModel<>(context.bakeLayer(ModelLayers.SILVERFISH)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(LanternBeetle entity) {
        return TEXTURE;
    }
}
