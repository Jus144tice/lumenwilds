/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.CragWraith;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

/**
 * Bespoke Crag Wraith model (Phase 9b) — a manta-like aerial hunter: a flat body, two big sweeping wings, and
 * a long thin tail. Replaces the scaled-ghast placeholder.
 */
public class CragWraithModel extends HierarchicalModel<CragWraith> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart wingR;
    private final ModelPart wingL;
    private final ModelPart tail;

    public CragWraithModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");
        this.wingR = body.getChild("wing_r");
        this.wingL = body.getChild("wing_l");
        this.tail = body.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, 0.0F, -5.0F, 4.0F, 1.0F, 11.0F) // flat central body
                        .texOffs(0, 14)
                        .addBox(-2.5F, -0.5F, -6.0F, 5.0F, 1.0F, 2.0F), // wider leading edge
                PartPose.offset(0.0F, 14.0F, 0.0F));

        body.addOrReplaceChild(
                "wing_r",
                CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, 0.0F, -5.0F, 11.0F, 0.0F, 11.0F),
                PartPose.offset(2.0F, 0.5F, -1.0F));
        body.addOrReplaceChild(
                "wing_l",
                CubeListBuilder.create().texOffs(0, 20).addBox(-11.0F, 0.0F, -5.0F, 11.0F, 0.0F, 11.0F),
                PartPose.offset(-2.0F, 0.5F, -1.0F));
        body.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(44, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 9.0F),
                PartPose.offset(0.0F, 0.0F, 6.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            CragWraith entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        float flap = Mth.cos(ageInTicks * 0.18F) * 0.5F;
        this.wingR.zRot = -flap;
        this.wingL.zRot = flap;
        this.tail.yRot = Mth.sin(ageInTicks * 0.12F) * 0.2F;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
