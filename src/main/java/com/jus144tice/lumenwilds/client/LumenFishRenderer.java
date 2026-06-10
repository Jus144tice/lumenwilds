/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.LumenFish;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Lumen Fish. <b>Placeholder:</b> reuses the vanilla {@link CodModel} (the already-registered
 * {@code ModelLayers.COD}) with a glowing Lumenwilds texture — a small fish stands in for the final
 * bioluminescent swimmer (Phase 9 art).
 */
public class LumenFishRenderer extends MobRenderer<LumenFish, CodModel<LumenFish>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lumen_fish.png");

    public LumenFishRenderer(EntityRendererProvider.Context context) {
        super(context, new CodModel<>(context.bakeLayer(ModelLayers.COD)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(LumenFish entity) {
        return TEXTURE;
    }
}
