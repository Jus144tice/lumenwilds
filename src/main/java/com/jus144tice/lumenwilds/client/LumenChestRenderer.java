/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.block.LumenChestBlockEntity;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;

/**
 * Renders the glowing wood chests (v1.1.3): picks the per-species chest texture (Glowwood vs Glowroot) and
 * draws the chest <b>fullbright</b> so it looks luminous in any light, matching the rest of the lumen wood.
 * The textures live on the vanilla chests atlas ({@code Sheets.CHEST_SHEET}; its {@code entity/chest}
 * directory source scans all namespaces, so {@code lumenwilds:entity/chest/*} is included).
 */
public class LumenChestRenderer extends ChestRenderer<LumenChestBlockEntity> {

    private static Material mat(String name) {
        return new Material(Sheets.CHEST_SHEET, ResourceLocationHelper.modLoc("entity/chest/" + name));
    }

    private static final Material GLOWWOOD = mat("glowwood");
    private static final Material GLOWWOOD_LEFT = mat("glowwood_left");
    private static final Material GLOWWOOD_RIGHT = mat("glowwood_right");
    private static final Material GLOWROOT = mat("glowroot");
    private static final Material GLOWROOT_LEFT = mat("glowroot_left");
    private static final Material GLOWROOT_RIGHT = mat("glowroot_right");

    public LumenChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getMaterial(LumenChestBlockEntity blockEntity, ChestType chestType) {
        boolean glowroot = blockEntity.getBlockState().is(ModBlocks.GLOWROOT_CHEST.get());
        return switch (chestType) {
            case LEFT -> glowroot ? GLOWROOT_LEFT : GLOWWOOD_LEFT;
            case RIGHT -> glowroot ? GLOWROOT_RIGHT : GLOWWOOD_RIGHT;
            default -> glowroot ? GLOWROOT : GLOWWOOD;
        };
    }

    @Override
    public void render(
            LumenChestBlockEntity blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {
        // Force fullbright so the chest reads as glowing in any light (its blocks also emit light 7).
        super.render(blockEntity, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT, packedOverlay);
    }
}
