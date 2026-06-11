/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.LumenGrazer;
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
 * Bespoke Lumen Grazer model (Phase 9b) — the bible's <b>six-legged</b> herd herbivore: a sturdy body, a
 * lowered grazing head, and three pairs of legs. Replaces the cow placeholder.
 */
public class LumenGrazerModel extends HierarchicalModel<LumenGrazer> {

    private final ModelPart root;
    private final ModelPart[] legs = new ModelPart[6];

    public LumenGrazerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        String[] names = {"leg_fr", "leg_fl", "leg_mr", "leg_ml", "leg_br", "leg_bl"};
        for (int i = 0; i < 6; i++) {
            this.legs[i] = root.getChild(names[i]);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 7.0F, 16.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));
        body.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F) // head
                        .texOffs(24, 24)
                        .addBox(-2.0F, 0.0F, -8.0F, 4.0F, 3.0F, 2.0F), // muzzle
                PartPose.offset(0.0F, 4.0F, -8.0F)); // lowered to graze

        // Six legs (3 per side) reaching to the ground.
        float[] z = {-6.0F, 0.0F, 6.0F};
        String[] names = {"leg_fr", "leg_fl", "leg_mr", "leg_ml", "leg_br", "leg_bl"};
        for (int i = 0; i < 6; i++) {
            float x = (i % 2 == 0) ? 3.0F : -3.0F;
            part.addOrReplaceChild(
                    names[i],
                    CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F),
                    PartPose.offset(x, 15.0F, z[i / 2]));
        }

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            LumenGrazer entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        // Alternating tripod-ish gait.
        for (int i = 0; i < 6; i++) {
            float phase = ((i + (i / 2)) % 2 == 0) ? 0.0F : Mth.PI;
            this.legs[i].xRot = Mth.cos(limbSwing * 0.6F + phase) * 0.7F * limbSwingAmount;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
