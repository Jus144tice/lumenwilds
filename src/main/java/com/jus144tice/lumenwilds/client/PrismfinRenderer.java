/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.LumenFishModel;
import com.jus144tice.lumenwilds.entity.Prismfin;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Prismfin (v1.4.2) — reuses the shared {@link LumenFishModel} geometry (the {@code LUMEN_FISH}
 * layer) with its own vivid texture.
 */
public class PrismfinRenderer extends MobRenderer<Prismfin, LumenFishModel<Prismfin>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/prismfin.png");

    public PrismfinRenderer(EntityRendererProvider.Context context) {
        super(context, new LumenFishModel<>(context.bakeLayer(LumenModelLayers.LUMEN_FISH)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(Prismfin entity) {
        return TEXTURE;
    }
}
