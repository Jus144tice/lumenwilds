/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.Mirelurker;
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
 * Bespoke Mirelurker model (Phase 9b) — the Moonmire ambusher: a chunky anglerfish-like body with a tail, a
 * dorsal fin, side fins, and a dangling glowing <b>lure</b> over its mouth. Replaces the salmon placeholder
 * (and folds in the deferred lure appendage).
 */
public class MirelurkerModel extends HierarchicalModel<Mirelurker> {

    private final ModelPart root;
    private final ModelPart tail;
    private final ModelPart lure;

    public MirelurkerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.tail = root.getChild("body").getChild("tail");
        this.lure = root.getChild("body").getChild("lure");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.0F, -3.5F, -5.0F, 6.0F, 7.0F, 10.0F) // body
                        .texOffs(0, 18)
                        .addBox(-2.0F, -2.5F, -8.0F, 4.0F, 5.0F, 3.0F), // head/jaw
                PartPose.offset(0.0F, 19.0F, 0.0F));

        body.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(0, 26).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 5.0F),
                PartPose.offset(0.0F, 0.0F, 5.0F));
        body.addOrReplaceChild(
                "dorsal",
                CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, -6.0F, -3.0F, 1.0F, 3.0F, 7.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild(
                "fin_r",
                CubeListBuilder.create().texOffs(20, 12).addBox(0.0F, 0.0F, -1.0F, 4.0F, 0.0F, 3.0F),
                PartPose.offset(3.0F, 1.0F, -1.0F));
        body.addOrReplaceChild(
                "fin_l",
                CubeListBuilder.create().texOffs(20, 12).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 0.0F, 3.0F),
                PartPose.offset(-3.0F, 1.0F, -1.0F));
        // Dangling glowing lure over the mouth.
        body.addOrReplaceChild(
                "lure",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(-0.5F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F) // rod
                        .texOffs(28, 8)
                        .addBox(-1.0F, -7.5F, -0.5F, 2.0F, 2.0F, 2.0F), // glow bulb
                PartPose.offsetAndRotation(0.0F, -3.0F, -7.0F, -0.4F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            Mirelurker entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        float speed = entity.isInWater() ? 1.0F : 1.5F;
        this.tail.yRot = -Mth.cos(ageInTicks * 0.15F * speed) * 0.6F;
        this.lure.xRot = -0.4F + Mth.sin(ageInTicks * 0.08F) * 0.15F; // gentle bob
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
