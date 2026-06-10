/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.CragWraith;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Crag Wraith. <b>Placeholder:</b> reuses the vanilla {@link GhastModel} (the already-registered
 * {@code ModelLayers.GHAST}) scaled to a mid size with a dark crystal-violet texture — a hovering aerial
 * menace. (The Sky Jelly uses the same model far smaller and pale, so they read differently.) The final
 * manta-like winged model with crystal wings is a Phase 9 art task.
 */
public class CragWraithRenderer extends MobRenderer<CragWraith, GhastModel<CragWraith>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/crag_wraith.png");

    public CragWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new GhastModel<>(context.bakeLayer(ModelLayers.GHAST)), 0.7F);
    }

    @Override
    protected void scale(CragWraith entity, PoseStack pose, float partialTick) {
        pose.scale(0.7F, 0.55F, 0.7F); // flattened a little, manta-ish
    }

    @Override
    public ResourceLocation getTextureLocation(CragWraith entity) {
        return TEXTURE;
    }
}
