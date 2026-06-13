/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.EchoSentinel;
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
 * Bespoke Echo Sentinel model (Phase 10f) — a floating ruin-guardian construct: a faceted central shell with a
 * single forward crystal eye, ringed by three broken orbiting fragments that slowly rotate. The whole thing
 * bobs gently in the air.
 */
public class EchoSentinelModel extends HierarchicalModel<EchoSentinel> {

    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart rings;

    public EchoSentinelModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.core = root.getChild("core");
        this.rings = root.getChild("rings");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition core = part.addOrReplaceChild(
                "core",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F) // faceted shell
                        .texOffs(0, 12)
                        .addBox(-1.5F, -1.5F, -4.5F, 3.0F, 3.0F, 1.0F), // crystal eye (front)
                PartPose.offset(0.0F, 14.0F, 0.0F));
        core.addOrReplaceChild(
                "cap_top",
                CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 4.0F),
                PartPose.ZERO);
        core.addOrReplaceChild(
                "cap_bottom",
                CubeListBuilder.create().texOffs(24, 6).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 2.0F, 4.0F),
                PartPose.ZERO);

        PartDefinition rings =
                part.addOrReplaceChild("rings", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 0.0F));
        rings.addOrReplaceChild(
                "seg_a",
                CubeListBuilder.create().texOffs(0, 20).addBox(5.0F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F),
                PartPose.ZERO);
        rings.addOrReplaceChild(
                "seg_b",
                CubeListBuilder.create().texOffs(0, 20).addBox(-8.0F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F),
                PartPose.ZERO);
        rings.addOrReplaceChild(
                "seg_c",
                CubeListBuilder.create().texOffs(0, 25).addBox(-1.5F, -0.5F, 5.0F, 3.0F, 1.0F, 3.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            EchoSentinel entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        this.rings.yRot = ageInTicks * 0.06F;
        this.rings.xRot = Mth.cos(ageInTicks * 0.04F) * 0.25F;
        this.core.y = 14.0F + Mth.sin(ageInTicks * 0.08F) * 0.6F;
        this.core.yRot = -this.rings.yRot * 0.4F;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
