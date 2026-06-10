/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.Glowmoth;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Glowmoth. <b>Placeholder:</b> reuses the vanilla {@link EndermiteModel} (the already-registered
 * {@code ModelLayers.ENDERMITE}) — a small segmented bug — scaled up with a glowing texture. The final winged
 * luminous-moth model is a Phase 9 art task.
 */
public class GlowmothRenderer extends MobRenderer<Glowmoth, EndermiteModel<Glowmoth>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/glowmoth.png");

    public GlowmothRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermiteModel<>(context.bakeLayer(ModelLayers.ENDERMITE)), 0.3F);
    }

    @Override
    protected void scale(Glowmoth entity, PoseStack pose, float partialTick) {
        pose.scale(1.6F, 1.6F, 1.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(Glowmoth entity) {
        return TEXTURE;
    }
}
