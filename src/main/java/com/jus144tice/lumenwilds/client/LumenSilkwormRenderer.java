/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.LumenSilkwormModel;
import com.jus144tice.lumenwilds.entity.LumenSilkworm;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders the Lumen Silkworm (v1.4.4) with the bespoke {@link LumenSilkwormModel} + emissive glow. */
public class LumenSilkwormRenderer extends MobRenderer<LumenSilkworm, LumenSilkwormModel> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/lumen_silkworm.png");

    public LumenSilkwormRenderer(EntityRendererProvider.Context context) {
        super(context, new LumenSilkwormModel(context.bakeLayer(LumenModelLayers.LUMEN_SILKWORM)), 0.2F);
    }

    /** Babies render at ~half scale so the silkworm has a visible size progression as it grows. */
    @Override
    protected void scale(LumenSilkworm entity, PoseStack poseStack, float partialTick) {
        float s = entity.isBaby() ? 0.45F : 1.0F;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(LumenSilkworm entity) {
        return TEXTURE;
    }
}
