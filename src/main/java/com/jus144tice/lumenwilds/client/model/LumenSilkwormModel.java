/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.LumenSilkworm;
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
 * Bespoke Lumen Silkworm model (v1.4.4) — a small segmented caterpillar: a head and three tapering body
 * segments that gently undulate as it crawls. Tiny and glowing.
 */
public class LumenSilkwormModel extends HierarchicalModel<LumenSilkworm> {

    private final ModelPart root;
    private final ModelPart seg0;
    private final ModelPart seg1;
    private final ModelPart seg2;
    private final ModelPart seg3;

    public LumenSilkwormModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root;
        this.seg0 = root.getChild("seg0");
        this.seg1 = root.getChild("seg1");
        this.seg2 = root.getChild("seg2");
        this.seg3 = root.getChild("seg3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        // head + three body segments, front (z-) to back (z+), tapering.
        part.addOrReplaceChild(
                "seg0",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, -3.0F));
        part.addOrReplaceChild(
                "seg1",
                CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));
        part.addOrReplaceChild(
                "seg2",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, 3.0F));
        part.addOrReplaceChild(
                "seg3",
                CubeListBuilder.create().texOffs(16, 16).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, 5.5F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(
            LumenSilkworm entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        // A travelling undulation wave down the body (inch-worm crawl) + a small idle bob.
        float wave = limbSwingAmount > 0.02F ? limbSwingAmount : 0.15F;
        float phase = limbSwingAmount > 0.02F ? limbSwing * 0.6F : ageInTicks * 0.12F;
        this.seg0.y = 22.0F - Mth.cos(phase) * wave;
        this.seg1.y = 22.0F - Mth.cos(phase - 0.9F) * wave;
        this.seg2.y = 22.0F - Mth.cos(phase - 1.8F) * wave;
        this.seg3.y = 22.0F - Mth.cos(phase - 2.7F) * wave;
        this.seg0.xRot = Mth.cos(phase) * 0.12F;
        this.seg3.xRot = -Mth.cos(phase - 2.7F) * 0.15F;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
