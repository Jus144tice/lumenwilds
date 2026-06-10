/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

/**
 * The Lumenwilds' bespoke sky (Phase 7a). Registered (client-side) for the {@code lumenwilds:lumenwilds}
 * dimension-type effects id via {@code RegisterDimensionSpecialEffectsEvent} in {@link LumenwildsClient}.
 *
 * <p>{@link #renderSky} fully replaces vanilla sky rendering with: a deep-indigo-to-teal <b>twilight dome</b>
 * gradient, a <b>weak blurred sun</b> (no harsh daylight), and <b>Veyra</b> — an oversized pale blue-white
 * moon (~3× the vanilla moon) that the dimension is meant to live under. The structure mirrors vanilla
 * {@code LevelRenderer#renderSky} on 1.21.1 (inline {@link Tesselator} draws, careful depth/blend state).</p>
 *
 * <p><b>Verification note:</b> sky rendering can't be validated on a headless server — the look needs an
 * in-game {@code runClient} pass through a portal. This builds + registers cleanly; the visual is best-effort.</p>
 */
public class LumenDimensionEffects extends DimensionSpecialEffects {

    /** The dimension-type effects id this is registered under (matches {@code dimension_type.effects}). */
    public static final ResourceLocation EFFECTS_ID = ResourceLocationHelper.modLoc("lumenwilds");

    private static final ResourceLocation VEYRA = ResourceLocationHelper.modLoc("textures/environment/veyra.png");
    private static final ResourceLocation SUN = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");

    /** Veyra is huge — the vanilla moon quad is 20; the sun 30. */
    private static final float VEYRA_RADIUS = 55.0F;

    private static final float SUN_RADIUS = 16.0F;

    public LumenDimensionEffects() {
        // cloudLevel = NaN (no vanilla clouds), hasGround = true, NORMAL sky, no forced-bright lightmap,
        // not constant ambient light.
        super(Float.NaN, true, DimensionSpecialEffects.SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        // Pull the fog toward a dim teal-indigo regardless of daytime brightness (perpetual twilight).
        float r = brightness * 0.82F + 0.04F;
        float g = brightness * 0.88F + 0.10F;
        float b = brightness * 0.92F + 0.16F;
        return new Vec3(fogColor.x * r, fogColor.y * g, fogColor.z * b);
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    /** No sunrise/sunset band — the Lumenwilds never has a bright horizon. */
    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTick) {
        return null;
    }

    @Override
    public boolean renderSky(
            ClientLevel level,
            int ticks,
            float partialTick,
            Matrix4f modelViewMatrix,
            Camera camera,
            Matrix4f projectionMatrix,
            boolean isFoggy,
            Runnable setupFog) {
        setupFog.run();
        if (isFoggy) {
            return true; // underwater / blinding fog: let the fog be the sky
        }

        Tesselator tesselator = Tesselator.getInstance();
        PoseStack pose = new PoseStack();
        pose.mulPose(modelViewMatrix);

        RenderSystem.depthMask(false);

        // --- 1) Twilight dome: a fan disc overhead, deep-indigo zenith -> dim-teal horizon. ---
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f domeMatrix = pose.last().pose();
        BufferBuilder dome = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        dome.addVertex(domeMatrix, 0.0F, 16.0F, 0.0F).setColor(0.05F, 0.11F, 0.20F, 1.0F);
        for (int i = 0; i <= 16; i++) {
            float angle = (float) i * (float) (Math.PI * 2) / 16.0F;
            float sx = Mth.sin(angle);
            float cz = Mth.cos(angle);
            dome.addVertex(domeMatrix, sx * 120.0F, 16.0F, cz * 120.0F).setColor(0.09F, 0.27F, 0.31F, 1.0F);
        }
        BufferUploader.drawWithShader(dome.buildOrThrow());

        // --- 2) Celestial bodies, slowly tracking the day cycle. ---
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
        pose.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
        Matrix4f skyMatrix = pose.last().pose();

        // Weak blurred sun (faint, no harsh daylight).
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(0.70F, 0.84F, 0.90F, 0.45F);
        RenderSystem.setShaderTexture(0, SUN);
        BufferBuilder sun = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        sun.addVertex(skyMatrix, -SUN_RADIUS, 100.0F, -SUN_RADIUS).setUv(0.0F, 0.0F);
        sun.addVertex(skyMatrix, SUN_RADIUS, 100.0F, -SUN_RADIUS).setUv(1.0F, 0.0F);
        sun.addVertex(skyMatrix, SUN_RADIUS, 100.0F, SUN_RADIUS).setUv(1.0F, 1.0F);
        sun.addVertex(skyMatrix, -SUN_RADIUS, 100.0F, SUN_RADIUS).setUv(0.0F, 1.0F);
        BufferUploader.drawWithShader(sun.buildOrThrow());

        // Veyra — the oversized pale blue-white moon (opposite the sun).
        RenderSystem.setShaderColor(0.85F, 0.92F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, VEYRA);
        BufferBuilder moon = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        moon.addVertex(skyMatrix, -VEYRA_RADIUS, -100.0F, VEYRA_RADIUS).setUv(0.0F, 1.0F);
        moon.addVertex(skyMatrix, VEYRA_RADIUS, -100.0F, VEYRA_RADIUS).setUv(1.0F, 1.0F);
        moon.addVertex(skyMatrix, VEYRA_RADIUS, -100.0F, -VEYRA_RADIUS).setUv(1.0F, 0.0F);
        moon.addVertex(skyMatrix, -VEYRA_RADIUS, -100.0F, -VEYRA_RADIUS).setUv(0.0F, 0.0F);
        BufferUploader.drawWithShader(moon.buildOrThrow());

        pose.popPose();

        // --- Restore render state. ---
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        return true;
    }
}
