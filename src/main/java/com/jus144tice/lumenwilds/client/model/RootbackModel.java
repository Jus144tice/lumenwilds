/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.Rootback;
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
 * Bespoke Rootback model (Phase 9b) — the massive living-feature turtle, built ~3 blocks wide × ~2 tall to
 * fill its hitbox at 1:1 (no scale hack): a wide domed shell over a flat belly, a big head, a stubby tail,
 * and four thick legs. Replaces the scaled cow placeholder. (The growing shell-plants layer is a later task.)
 */
public class RootbackModel extends HierarchicalModel<Rootback> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart legFR;
    private final ModelPart legFL;
    private final ModelPart legBR;
    private final ModelPart legBL;

    public RootbackModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.head = root.getChild("head");
        this.legFR = root.getChild("leg_fr");
        this.legFL = root.getChild("leg_fl");
        this.legBR = root.getChild("leg_br");
        this.legBL = root.getChild("leg_bl");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        // Flat belly plate (y 18–22).
        part.addOrReplaceChild(
                "belly",
                CubeListBuilder.create().texOffs(0, 0).addBox(-20.0F, 0.0F, -22.0F, 40.0F, 4.0F, 44.0F),
                PartPose.offset(0.0F, 18.0F, 0.0F));
        // Domed shell (rim → mid → peak), rising to ~y -12.
        part.addOrReplaceChild(
                "shell",
                CubeListBuilder.create()
                        .texOffs(0, 60)
                        .addBox(-22.0F, 0.0F, -24.0F, 44.0F, 6.0F, 48.0F) // rim
                        .texOffs(0, 116)
                        .addBox(-17.0F, -7.0F, -19.0F, 34.0F, 7.0F, 38.0F) // mid
                        .texOffs(120, 120)
                        .addBox(-11.0F, -12.0F, -13.0F, 22.0F, 5.0F, 26.0F), // peak
                PartPose.offset(0.0F, 6.0F, 0.0F));

        part.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(140, 0).addBox(-5.0F, -5.0F, -10.0F, 10.0F, 7.0F, 10.0F),
                PartPose.offset(0.0F, 18.0F, -22.0F));
        part.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(140, 20).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 4.0F, 9.0F),
                PartPose.offset(0.0F, 18.0F, 22.0F));

        addLeg(part, "leg_fr", 15.0F, -14.0F);
        addLeg(part, "leg_fl", -15.0F, -14.0F);
        addLeg(part, "leg_br", 15.0F, 14.0F);
        addLeg(part, "leg_bl", -15.0F, 14.0F);

        return LayerDefinition.create(mesh, 256, 256);
    }

    private static void addLeg(PartDefinition part, String name, float x, float z) {
        part.addOrReplaceChild(
                name,
                CubeListBuilder.create().texOffs(160, 40).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 6.0F, 8.0F),
                PartPose.offset(x, 18.0F, z));
    }

    @Override
    public void setupAnim(
            Rootback entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        float s = Mth.cos(limbSwing * 0.3F) * 0.3F * limbSwingAmount;
        this.legFR.xRot = s;
        this.legBL.xRot = s;
        this.legFL.xRot = -s;
        this.legBR.xRot = -s;
        this.head.xRot = Mth.sin(ageInTicks * 0.05F) * 0.05F;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
