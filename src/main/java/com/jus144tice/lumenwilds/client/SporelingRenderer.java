/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.Sporeling;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Sporeling. <b>Placeholder:</b> reuses the vanilla {@link SlimeModel} (the already-registered
 * {@code ModelLayers.SLIME}) with a fungal Lumenwilds texture — a small blob stands in for the final
 * fungal swarm creature (Phase 9 art).
 */
public class SporelingRenderer extends MobRenderer<Sporeling, SlimeModel<Sporeling>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/sporeling.png");

    public SporelingRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(Sporeling entity) {
        return TEXTURE;
    }
}
