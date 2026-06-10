/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.SkyJelly;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Sky Jelly. <b>Placeholder:</b> reuses the vanilla {@link GhastModel} (the already-registered
 * {@code ModelLayers.GHAST}) — its floating body + hanging tentacles read as a jellyfish — scaled way down
 * (a ghast is ~4 blocks) with a translucent-teal texture. Final bespoke model + emissive glow → Phase 9.
 */
public class SkyJellyRenderer extends MobRenderer<SkyJelly, GhastModel<SkyJelly>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/sky_jelly.png");

    public SkyJellyRenderer(EntityRendererProvider.Context context) {
        super(context, new GhastModel<>(context.bakeLayer(ModelLayers.GHAST)), 0.5F);
    }

    @Override
    protected void scale(SkyJelly entity, PoseStack pose, float partialTick) {
        pose.scale(0.35F, 0.35F, 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(SkyJelly entity) {
        return TEXTURE;
    }
}
