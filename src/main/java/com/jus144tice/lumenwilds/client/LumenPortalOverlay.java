/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.portal.LumenPortalBlock;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

/**
 * The "you're teleporting" screen effect for the Lumen Portal (the deferred Phase-7 teal overlay). While the
 * local player stands inside a {@link LumenPortalBlock}, a teal swirl veil scrolls across the screen and
 * intensifies over the ~80-tick dwell (calmer than the Nether's nausea spin — no wobble, just a rising glow),
 * so it's obvious the portal is charging. It fades quickly once you step out.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, value = Dist.CLIENT)
public final class LumenPortalOverlay {

    private static final ResourceLocation OVERLAY =
            ResourceLocationHelper.modLoc("textures/gui/lumen_portal_overlay.png");
    private static final float RISE = 1.0f / 70.0f; // ~full just before the 80-tick teleport
    private static final float FALL = 1.0f / 12.0f; // quick fade-out when you leave
    private static final float MAX_ALPHA = 0.82f;

    private static float intensity = 0.0f;
    private static int ticks = 0;

    private LumenPortalOverlay() {}

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            intensity = 0.0f;
            return;
        }
        ticks++;
        boolean inPortal = isInPortal(player);
        intensity = inPortal ? Math.min(1.0f, intensity + RISE) : Math.max(0.0f, intensity - FALL);
    }

    private static boolean isInPortal(LocalPlayer player) {
        // The portal plane is thin, so check both the feet and eye block.
        return player.level().getBlockState(player.blockPosition()).getBlock() instanceof LumenPortalBlock
                || player.level()
                                .getBlockState(BlockPos.containing(player.getEyePosition()))
                                .getBlock()
                        instanceof LumenPortalBlock;
    }

    @SubscribeEvent
    public static void onRenderGui(final RenderGuiEvent.Post event) {
        if (intensity <= 0.001f) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        int w = event.getGuiGraphics().guiWidth();
        int h = event.getGuiGraphics().guiHeight();
        float alpha = (float) Math.pow(intensity, 1.3) * MAX_ALPHA;
        // Two scroll layers (opposite directions, different scale) read as a slow swirling distortion.
        float t = ticks * 0.012f;
        drawVeil(w, h, alpha * 0.7f, 2.3f, t, -t * 0.6f);
        drawVeil(w, h, alpha * 0.5f, 3.7f, -t * 0.5f, t * 0.8f);
    }

    private static void drawVeil(int w, int h, float alpha, float tile, float uOff, float vOff) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, OVERLAY);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

        float u0 = uOff, u1 = uOff + tile;
        float v0 = vOff, v1 = vOff + tile * ((float) h / (float) w);
        BufferBuilder bb = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bb.addVertex(0.0f, h, -90.0f).setUv(u0, v1);
        bb.addVertex(w, h, -90.0f).setUv(u1, v1);
        bb.addVertex(w, 0.0f, -90.0f).setUv(u1, v0);
        bb.addVertex(0.0f, 0.0f, -90.0f).setUv(u0, v0);
        BufferUploader.drawWithShader(bb.buildOrThrow());

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}
