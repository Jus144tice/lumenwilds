/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client.model;

import com.jus144tice.lumenwilds.entity.SkyJelly;
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
 * Bespoke Sky Jelly model (Phase 9b) — a jellyfish: a stepped translucent <b>bell</b> dome with six hanging
 * <b>tentacles</b> that sway. Replaces the vanilla ghast placeholder. Built ~1.2 blocks tall to sit in the
 * hitbox (tune the bell offset if it floats high/low). Texture {@code textures/entity/sky_jelly.png} (64×64),
 * kept near-uniform so the box UVs are forgiving.
 */
public class SkyJellyModel extends HierarchicalModel<SkyJelly> {

    private final ModelPart root;
    private final ModelPart bell;
    private final ModelPart[] tentacles = new ModelPart[6];

    public SkyJellyModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.bell = root.getChild("bell");
        for (int i = 0; i < tentacles.length; i++) {
            this.tentacles[i] = root.getChild("tentacle" + i);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        // Bell: a wide rim box + a narrower dome on top (a stepped dome).
        part.addOrReplaceChild(
                "bell",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(-5.0F, 9.0F, -5.0F, 10.0F, 4.0F, 10.0F) // rim
                        .texOffs(0, 0)
                        .addBox(-3.5F, 6.0F, -3.5F, 7.0F, 3.0F, 7.0F), // dome
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Six tentacles hanging from the rim (their own parts so they can sway).
        int[][] rim = {{4, 3}, {4, -3}, {-4, 3}, {-4, -3}, {0, 4}, {0, -4}};
        for (int i = 0; i < rim.length; i++) {
            part.addOrReplaceChild(
                    "tentacle" + i,
                    CubeListBuilder.create().texOffs(44, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                    PartPose.offset(rim[i][0], 13.0F, rim[i][1]));
        }

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(
            SkyJelly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        // Gentle pulsing bell + swaying tentacles.
        float pulse = Mth.cos(ageInTicks * 0.12F) * 0.06F;
        this.bell.xScale = 1.0F + pulse;
        this.bell.zScale = 1.0F + pulse;
        this.bell.yScale = 1.0F - pulse;
        for (int i = 0; i < tentacles.length; i++) {
            float sway = Mth.cos(ageInTicks * 0.1F + i * 0.7F) * 0.18F;
            this.tentacles[i].xRot = sway;
            this.tentacles[i].zRot = sway * 0.6F;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
