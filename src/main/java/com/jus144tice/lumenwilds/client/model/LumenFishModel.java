/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * Bespoke small-fish model (Phase 9b) — a slim body, a swishing tail fin, and a little dorsal fin. Used by the
 * Lumen Fish and (v1.4.2) the Prismfin (generic over the entity type; only the texture differs).
 */
public class LumenFishModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart tail;

    public LumenFishModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.tail = root.getChild("body").getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 4.0F, 6.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        body.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 3.0F));
        body.addOrReplaceChild(
                "dorsal",
                CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        float speed = entity.isInWater() ? 1.0F : 1.8F;
        this.tail.yRot = -Mth.cos(ageInTicks * 0.2F * speed) * 0.7F;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
