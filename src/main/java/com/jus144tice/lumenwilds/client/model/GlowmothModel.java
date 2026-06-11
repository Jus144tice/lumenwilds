/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.Glowmoth;
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
 * Bespoke Glowmoth model (Phase 9b) — a luminous moth: a furry body + head with two antennae, and two pairs
 * of flat wings (fore + hind) that flutter. Replaces the scaled-endermite placeholder.
 */
public class GlowmothModel extends HierarchicalModel<Glowmoth> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart foreWingR;
    private final ModelPart foreWingL;
    private final ModelPart hindWingR;
    private final ModelPart hindWingL;

    public GlowmothModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");
        this.foreWingR = body.getChild("fore_wing_r");
        this.foreWingL = body.getChild("fore_wing_l");
        this.hindWingR = body.getChild("hind_wing_r");
        this.hindWingL = body.getChild("hind_wing_l");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F) // thorax
                        .texOffs(0, 10)
                        .addBox(-1.5F, -1.0F, 2.0F, 3.0F, 3.0F, 5.0F), // abdomen
                PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 0.0F, -2.0F));
        head.addOrReplaceChild(
                "antenna_r",
                CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 0.0F),
                PartPose.offsetAndRotation(0.8F, -1.5F, -1.0F, -0.3F, 0.3F, 0.0F));
        head.addOrReplaceChild(
                "antenna_l",
                CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 0.0F),
                PartPose.offsetAndRotation(-0.8F, -1.5F, -1.0F, -0.3F, -0.3F, 0.0F));

        body.addOrReplaceChild(
                "fore_wing_r",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, -4.0F, 10.0F, 0.0F, 8.0F),
                PartPose.offset(2.0F, -1.0F, -1.0F));
        body.addOrReplaceChild(
                "fore_wing_l",
                CubeListBuilder.create().texOffs(0, 32).addBox(-10.0F, 0.0F, -4.0F, 10.0F, 0.0F, 8.0F),
                PartPose.offset(-2.0F, -1.0F, -1.0F));
        body.addOrReplaceChild(
                "hind_wing_r",
                CubeListBuilder.create().texOffs(0, 44).addBox(0.0F, 0.0F, 0.0F, 7.0F, 0.0F, 6.0F),
                PartPose.offset(1.5F, -0.5F, 2.0F));
        body.addOrReplaceChild(
                "hind_wing_l",
                CubeListBuilder.create().texOffs(0, 44).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 0.0F, 6.0F),
                PartPose.offset(-1.5F, -0.5F, 2.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            Glowmoth entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        float flap = Mth.cos(ageInTicks * 1.3F) * 0.5F;
        this.foreWingR.zRot = -0.1F - flap;
        this.foreWingL.zRot = 0.1F + flap;
        this.hindWingR.zRot = -0.05F - flap * 0.8F;
        this.hindWingL.zRot = 0.05F + flap * 0.8F;
        this.body.y = 18.0F + Mth.sin(ageInTicks * 0.2F) * 0.4F; // gentle bob
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
