/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.LumenFishModel;
import com.jus144tice.lumenwilds.entity.LumenFish;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Lumen Fish with the bespoke {@link LumenFishModel} (Phase 9b — a small glowing schooling fish,
 * replacing the cod placeholder).
 */
public class LumenFishRenderer extends MobRenderer<LumenFish, LumenFishModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lumen_fish.png");

    public LumenFishRenderer(EntityRendererProvider.Context context) {
        super(context, new LumenFishModel(context.bakeLayer(LumenModelLayers.LUMEN_FISH)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(LumenFish entity) {
        return TEXTURE;
    }
}
