/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.LumenSilkwormModel;
import com.jus144tice.lumenwilds.entity.LumenSilkworm;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders the Lumen Silkworm (v1.4.4) with the bespoke {@link LumenSilkwormModel} + emissive glow. */
public class LumenSilkwormRenderer extends MobRenderer<LumenSilkworm, LumenSilkwormModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lumen_silkworm.png");

    public LumenSilkwormRenderer(EntityRendererProvider.Context context) {
        super(context, new LumenSilkwormModel(context.bakeLayer(LumenModelLayers.LUMEN_SILKWORM)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(LumenSilkworm entity) {
        return TEXTURE;
    }
}
