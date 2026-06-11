/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModEntities;
import com.jus144tice.lumenwilds.registry.ModFluidTypes;
import com.jus144tice.lumenwilds.registry.ModParticles;
import com.jus144tice.lumenwilds.registry.ModWoodTypes;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * Client-only setup ({@link Dist#CLIENT}). Registers the Glowwood
 * {@link net.minecraft.world.level.block.state.properties.WoodType} with {@link Sheets} so its sign /
 * hanging-sign materials are atlased and the vanilla sign renderers can draw Glowwood signs.
 *
 * <p>Boat + chest-boat model layers do NOT need registering here: vanilla {@code LayerDefinitions}
 * already iterates {@code Boat.Type.values()} (which now includes our enum-extended Glowwood type), and
 * the vanilla {@code BoatRenderer} draws it from {@code lumenwilds:entity/boat|chest_boat/glowwood}.
 * Registering them again caused a duplicate-layer crash.</p>
 *
 * <p>Also registers the Lumenwater {@code FluidType}'s client render extensions (Phase 5e): it reuses the
 * vanilla water still/flow animations with a teal tint, so Lumenwater reads as glowing teal water without
 * bespoke fluid textures yet.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class LumenwildsClient {

    private LumenwildsClient() {}

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> Sheets.addWoodType(ModWoodTypes.GLOWWOOD));
    }

    /** Phase 9b: register the bespoke entity-model layer definitions (one per mob as they're authored). */
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                LumenModelLayers.SKY_JELLY, com.jus144tice.lumenwilds.client.model.SkyJellyModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.GLOWMOTH, com.jus144tice.lumenwilds.client.model.GlowmothModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.CRAG_WRAITH, com.jus144tice.lumenwilds.client.model.CragWraithModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.LANTERN_BEETLE,
                com.jus144tice.lumenwilds.client.model.LanternBeetleModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.SPORELING, com.jus144tice.lumenwilds.client.model.SporelingModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.SHADE_STALKER,
                com.jus144tice.lumenwilds.client.model.ShadeStalkerModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.LUMEN_GRAZER,
                com.jus144tice.lumenwilds.client.model.LumenGrazerModel::createBodyLayer);
        event.registerLayerDefinition(
                LumenModelLayers.ROOTBACK, com.jus144tice.lumenwilds.client.model.RootbackModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.LUMEN_GRAZER.get(), LumenGrazerRenderer::new);
        event.registerEntityRenderer(ModEntities.SHADE_STALKER.get(), ShadeStalkerRenderer::new);
        event.registerEntityRenderer(ModEntities.LANTERN_BEETLE.get(), LanternBeetleRenderer::new);
        event.registerEntityRenderer(ModEntities.SPORELING.get(), SporelingRenderer::new);
        event.registerEntityRenderer(ModEntities.MIRELURKER.get(), MirelurkerRenderer::new);
        event.registerEntityRenderer(ModEntities.LUMEN_FISH.get(), LumenFishRenderer::new);
        event.registerEntityRenderer(ModEntities.SKY_JELLY.get(), SkyJellyRenderer::new);
        event.registerEntityRenderer(ModEntities.GLOWMOTH.get(), GlowmothRenderer::new);
        event.registerEntityRenderer(ModEntities.ROOTBACK.get(), RootbackRenderer::new);
        event.registerEntityRenderer(ModEntities.CRAG_WRAITH.get(), CragWraithRenderer::new);
    }

    /** Phase 7a: bind the bespoke Lumenwilds sky (Veyra moon + twilight dome) to the dimension. */
    @SubscribeEvent
    public static void onRegisterDimensionEffects(final RegisterDimensionSpecialEffectsEvent event) {
        event.register(LumenDimensionEffects.EFFECTS_ID, new LumenDimensionEffects());
    }

    /**
     * Phase 7b: render factories for the atmosphere particles. <b>Placeholders reuse vanilla render classes</b>
     * (the same approach as the mob model placeholders): Lumen Spore → {@link EndRodParticle} (slow glowing
     * drift), Glow Pollen → {@link SuspendedTownParticle} (gentle float), Crystal Shimmer →
     * {@link GlowParticle} glow-squid sparkle. Each pulls its sprite from {@code particles/<name>.json}.
     */
    @SubscribeEvent
    public static void onRegisterParticleProviders(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.LUMEN_SPORE.get(), EndRodParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GLOW_POLLEN.get(), SuspendedTownParticle.Provider::new);
        event.registerSpriteSet(ModParticles.CRYSTAL_SHIMMER.get(), GlowParticle.GlowSquidProvider::new);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(final RegisterClientExtensionsEvent event) {
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    private static final ResourceLocation STILL =
                            ResourceLocation.withDefaultNamespace("block/water_still");
                    private static final ResourceLocation FLOW =
                            ResourceLocation.withDefaultNamespace("block/water_flow");

                    @Override
                    public ResourceLocation getStillTexture() {
                        return STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return FLOW;
                    }

                    @Override
                    public int getTintColor() {
                        return 0xFF36E0C0; // ARGB — glowing teal
                    }
                },
                ModFluidTypes.LUMENWATER_TYPE.get());
    }
}
