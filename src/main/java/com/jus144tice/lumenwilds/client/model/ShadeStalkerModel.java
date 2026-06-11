/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.ShadeStalker;
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
 * Bespoke Shade Stalker model (Phase 9b) — a sleek, low, dark ambush predator: a long body, a forward head,
 * and four long stalking legs. Replaces the spider placeholder.
 */
public class ShadeStalkerModel extends HierarchicalModel<ShadeStalker> {

    private final ModelPart root;
    private final ModelPart legFR;
    private final ModelPart legFL;
    private final ModelPart legBR;
    private final ModelPart legBL;

    public ShadeStalkerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.legFR = root.getChild("leg_fr");
        this.legFL = root.getChild("leg_fl");
        this.legBR = root.getChild("leg_br");
        this.legBL = root.getChild("leg_bl");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -7.0F, 6.0F, 5.0F, 14.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        body.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F) // head
                        .texOffs(0, 28)
                        .addBox(-1.0F, 0.0F, -6.0F, 2.0F, 2.0F, 2.0F), // snout
                PartPose.offset(0.0F, 0.0F, -7.0F));

        // Four long legs (pivot high on the body, reaching to the ground).
        addLeg(part, "leg_fr", 3.0F, -4.0F);
        addLeg(part, "leg_fl", -3.0F, -4.0F);
        addLeg(part, "leg_br", 3.0F, 4.0F);
        addLeg(part, "leg_bl", -3.0F, 4.0F);

        return LayerDefinition.create(mesh, 64, 64);
    }

    private static void addLeg(PartDefinition part, String name, float x, float z) {
        part.addOrReplaceChild(
                name,
                CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F),
                PartPose.offset(x, 15.0F, z));
    }

    @Override
    public void setupAnim(
            ShadeStalker entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        float s = Mth.cos(limbSwing * 0.7F) * 0.6F * limbSwingAmount;
        this.legFR.xRot = s;
        this.legBL.xRot = s;
        this.legFL.xRot = -s;
        this.legBR.xRot = -s;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
