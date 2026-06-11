/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.LanternBeetleModel;
import com.jus144tice.lumenwilds.entity.LanternBeetle;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Lantern Beetle with the bespoke {@link LanternBeetleModel} (Phase 9b — a real shelled beetle
 * with legs + a glowing lantern abdomen, replacing the silverfish placeholder).
 */
public class LanternBeetleRenderer extends MobRenderer<LanternBeetle, LanternBeetleModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lantern_beetle.png");

    public LanternBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new LanternBeetleModel(context.bakeLayer(LumenModelLayers.LANTERN_BEETLE)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(LanternBeetle entity) {
        return TEXTURE;
    }
}
