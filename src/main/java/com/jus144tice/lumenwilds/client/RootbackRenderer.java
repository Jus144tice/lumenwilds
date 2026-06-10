/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.entity.Rootback;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Rootback. <b>Placeholder:</b> reuses the vanilla {@link CowModel} (the already-registered
 * {@code ModelLayers.COW}) scaled up <em>massively</em> to read as a roaming-landmark-sized beast, with a
 * mossy texture. The final turtle-shell model with its growing shell-plants is a Phase 9 art task.
 */
public class RootbackRenderer extends MobRenderer<Rootback, CowModel<Rootback>> {

    private static final ResourceLocation TEXTURE = ResourceLocationHelper.modLoc("textures/entity/rootback.png");

    public RootbackRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 1.6F);
    }

    @Override
    protected void scale(Rootback entity, PoseStack pose, float partialTick) {
        pose.scale(3.4F, 3.4F, 3.4F); // massive — a roaming landmark
    }

    @Override
    public ResourceLocation getTextureLocation(Rootback entity) {
        return TEXTURE;
    }
}
