/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.LanternBeetle;
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
 * Bespoke Lantern Beetle model (Phase 9b) — a small glowing beetle: a rounded body under a domed shell, a
 * head with two antennae, a glowing lantern abdomen, and six little legs. Replaces the silverfish placeholder.
 */
public class LanternBeetleModel extends HierarchicalModel<LanternBeetle> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart legR1;
    private final ModelPart legR2;
    private final ModelPart legR3;
    private final ModelPart legL1;
    private final ModelPart legL2;
    private final ModelPart legL3;

    public LanternBeetleModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root = root;
        this.body = root.getChild("body");
        this.legR1 = body.getChild("leg_r1");
        this.legR2 = body.getChild("leg_r2");
        this.legR3 = body.getChild("leg_r3");
        this.legL1 = body.getChild("leg_l1");
        this.legL2 = body.getChild("leg_l2");
        this.legL3 = body.getChild("leg_l3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 6.0F) // body
                        .texOffs(0, 10)
                        .addBox(-2.5F, -3.0F, -3.0F, 5.0F, 2.0F, 6.0F), // domed shell
                PartPose.offset(0.0F, 21.0F, 0.0F));

        body.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, -0.5F, -3.0F));
        // Glowing lantern abdomen tip (the texture's bright region).
        body.addOrReplaceChild(
                "lantern",
                CubeListBuilder.create().texOffs(22, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, -0.5F, 3.0F));

        // Six legs.
        addLeg(body, "leg_r1", 2.0F, 1.0F, -2.0F, 1);
        addLeg(body, "leg_r2", 2.0F, 1.0F, 0.0F, 1);
        addLeg(body, "leg_r3", 2.0F, 1.0F, 2.0F, 1);
        addLeg(body, "leg_l1", -2.0F, 1.0F, -2.0F, -1);
        addLeg(body, "leg_l2", -2.0F, 1.0F, 0.0F, -1);
        addLeg(body, "leg_l3", -2.0F, 1.0F, 2.0F, -1);

        return LayerDefinition.create(mesh, 64, 64);
    }

    private static void addLeg(PartDefinition body, String name, float x, float y, float z, int side) {
        body.addOrReplaceChild(
                name,
                CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -0.5F, 3.0F * side, 1.0F, 1.0F),
                PartPose.offset(x, y, z));
    }

    @Override
    public void setupAnim(
            LanternBeetle entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float headYaw,
            float headPitch) {
        // Skittering leg wiggle.
        float w = Mth.cos(limbSwing * 0.6F + ageInTicks * 0.3F) * 0.3F;
        this.legR1.zRot = -w;
        this.legR2.zRot = w;
        this.legR3.zRot = -w;
        this.legL1.zRot = w;
        this.legL2.zRot = -w;
        this.legL3.zRot = w;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
