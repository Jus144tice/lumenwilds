/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.EchoSentinelModel;
import com.jus144tice.lumenwilds.entity.EchoSentinel;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders the Echo Sentinel with the bespoke {@link EchoSentinelModel} (floating shell + eye + ring fragments). */
public class EchoSentinelRenderer extends MobRenderer<EchoSentinel, EchoSentinelModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/echo_sentinel.png");

    public EchoSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new EchoSentinelModel(context.bakeLayer(LumenModelLayers.ECHO_SENTINEL)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(EchoSentinel entity) {
        return TEXTURE;
    }
}
