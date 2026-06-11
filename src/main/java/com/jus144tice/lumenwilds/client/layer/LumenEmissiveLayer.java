/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * Reusable emissive bioluminescence layer (Phase 9c — "native living light"). Extends vanilla {@link EyesLayer}
 * (which re-renders the model fullbright + additive), but driven by a per-mob glow texture
 * {@code textures/entity/<name>_glow.png} instead of fixed eyes — so any region painted bright in that texture
 * glows in the dark (lures, lanterns, caps, wings, eyes, the whole jelly…), while black areas add nothing.
 */
public class LumenEmissiveLayer<T extends Entity, M extends EntityModel<T>> extends EyesLayer<T, M> {

    private final RenderType renderType;

    public LumenEmissiveLayer(RenderLayerParent<T, M> parent, ResourceLocation glowTexture) {
        super(parent);
        this.renderType = RenderType.eyes(glowTexture);
    }

    @Override
    public RenderType renderType() {
        return this.renderType;
    }
}
