/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.SporelingModel;
import com.jus144tice.lumenwilds.entity.Sporeling;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Sporeling with the bespoke {@link SporelingModel} (Phase 9b — a fungal creeper with a glowing
 * mushroom cap, replacing the slime placeholder).
 */
public class SporelingRenderer extends MobRenderer<Sporeling, SporelingModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/sporeling.png");

    public SporelingRenderer(EntityRendererProvider.Context context) {
        super(context, new SporelingModel(context.bakeLayer(LumenModelLayers.SPORELING)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(Sporeling entity) {
        return TEXTURE;
    }
}
