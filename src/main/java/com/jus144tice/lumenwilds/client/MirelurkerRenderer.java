/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.Mirelurker;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Mirelurker. <b>Placeholder:</b> reuses the vanilla {@link SalmonModel} (the already-registered
 * {@code ModelLayers.SALMON}) with a murky Lumenwilds texture — a fish silhouette stands in for the final
 * amphibious lurker with its glowing plant-mimic lure (Phase 9 art).
 */
public class MirelurkerRenderer extends MobRenderer<Mirelurker, SalmonModel<Mirelurker>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/mirelurker.png");

    public MirelurkerRenderer(EntityRendererProvider.Context context) {
        super(context, new SalmonModel<>(context.bakeLayer(ModelLayers.SALMON)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Mirelurker entity) {
        return TEXTURE;
    }
}
