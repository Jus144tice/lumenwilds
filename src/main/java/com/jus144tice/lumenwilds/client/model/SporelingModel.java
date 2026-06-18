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
 * Bespoke Sporeling model (Phase 9b) — a small fungal creeper: a squat body crowned by a glowing mushroom
 * cap, with two stubby feet. Replaces the slime placeholder. It bobs as it scuttles. Generic over the entity
 * type so the {@code SporeTrader} ("fully grown Sporeling", v1.2) can reuse it at a larger scale.
 */
public class SporelingModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart footR;
    private final ModelPart footL;

    public SporelingModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");
        this.footR = root.getChild("foot_r");
        this.footL = root.getChild("foot_l");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        // Glowing mushroom cap.
        body.addOrReplaceChild(
                "cap",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-4.5F, -2.0F, -4.5F, 9.0F, 2.0F, 9.0F)
                        .texOffs(0, 24)
                        .addBox(-3.0F, -4.0F, -3.0F, 6.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, -6.0F, 0.0F));

        part.addOrReplaceChild(
                "foot_r",
                CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(-2.0F, 22.0F, 0.0F));
        part.addOrReplaceChild(
                "foot_l",
                CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(2.0F, 22.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        this.footR.xRot = Mth.cos(limbSwing * 0.8F) * 0.8F * limbSwingAmount;
        this.footL.xRot = Mth.cos(limbSwing * 0.8F + Mth.PI) * 0.8F * limbSwingAmount;
        this.body.y = 24.0F + Mth.abs(Mth.sin(limbSwing * 0.8F)) * -1.0F * limbSwingAmount; // little hop
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
