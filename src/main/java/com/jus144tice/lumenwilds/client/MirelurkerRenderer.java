/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.MirelurkerModel;
import com.jus144tice.lumenwilds.entity.Mirelurker;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Mirelurker with the bespoke {@link MirelurkerModel} (Phase 9b — an anglerfish-like lurker with
 * a dangling glowing lure, replacing the salmon placeholder).
 */
public class MirelurkerRenderer extends MobRenderer<Mirelurker, MirelurkerModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/mirelurker.png");

    public MirelurkerRenderer(EntityRendererProvider.Context context) {
        super(context, new MirelurkerModel(context.bakeLayer(LumenModelLayers.MIRELURKER)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Mirelurker entity) {
        return TEXTURE;
    }
}
