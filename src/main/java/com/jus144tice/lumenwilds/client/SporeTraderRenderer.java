/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.client.model.SporelingModel;
import com.jus144tice.lumenwilds.entity.SporeTrader;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Sporeman trader — a "fully grown Sporeling": the same bespoke {@link SporelingModel} geometry
 * scaled up ~1.7×, with its own (older/merchant-tinted) texture.
 */
public class SporeTraderRenderer extends MobRenderer<SporeTrader, SporelingModel<SporeTrader>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/spore_trader.png");
    private static final float SCALE = 1.7F;

    public SporeTraderRenderer(EntityRendererProvider.Context context) {
        super(context, new SporelingModel<>(context.bakeLayer(LumenModelLayers.SPORELING)), 0.5F);
    }

    @Override
    protected void scale(SporeTrader entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(SporeTrader entity) {
        return TEXTURE;
    }
}
