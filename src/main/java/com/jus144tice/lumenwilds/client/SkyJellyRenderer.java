/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.SkyJellyModel;
import com.jus144tice.lumenwilds.entity.SkyJelly;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Sky Jelly with the bespoke {@link SkyJellyModel} (Phase 9b — the first real model, replacing the
 * scaled-ghast placeholder). Texture {@code textures/entity/sky_jelly.png}.
 */
public class SkyJellyRenderer extends MobRenderer<SkyJelly, SkyJellyModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/sky_jelly.png");

    public SkyJellyRenderer(EntityRendererProvider.Context context) {
        super(context, new SkyJellyModel(context.bakeLayer(LumenModelLayers.SKY_JELLY)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(SkyJelly entity) {
        return TEXTURE;
    }
}
