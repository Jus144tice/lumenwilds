/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.network.LumenEventClientState;
import com.jus144tice.lumenwilds.registry.ModParticles;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import com.jus144tice.lumenwilds.world.event.LumenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * Client-side ambient-event visuals (Phase 7d.2): while a {@link LumenEvent} is active (synced into
 * {@link LumenEventClientState}) and the player is in the Lumenwilds, sprinkles extra particles around them —
 * dense spores in a Sporefall, soft pollen in a Moonwake, a faint crystal shimmer in a Deep Hush. The
 * Moonwake sky-brightening lives in {@code LumenDimensionEffects} (which reads the same state).
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, value = Dist.CLIENT)
public final class LumenEventClientEffects {

    private LumenEventClientEffects() {}

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        LocalPlayer player = mc.player;
        if (level == null || player == null || mc.isPaused()) {
            return;
        }
        if (!level.dimension().equals(LumenDimensionConstants.LUMENWILDS_LEVEL)) {
            return;
        }
        if ((level.getGameTime() & 1L) != 0L) {
            return; // every other tick is plenty
        }
        switch (LumenEventClientState.active()) {
            case SPOREFALL:
                emit(level, player, ModParticles.LUMEN_SPORE.get(), 4, 14.0);
                break;
            case MOONWAKE:
                emit(level, player, ModParticles.GLOW_POLLEN.get(), 2, 14.0);
                break;
            case DEEP_HUSH:
                emit(level, player, ModParticles.CRYSTAL_SHIMMER.get(), 1, 10.0);
                break;
            default:
                break;
        }
    }

    private static void emit(Level level, LocalPlayer player, ParticleOptions particle, int count, double radius) {
        RandomSource rand = level.random;
        for (int i = 0; i < count; i++) {
            double x = player.getX() + (rand.nextDouble() - 0.5) * radius;
            double y = player.getY() + rand.nextDouble() * 8.0 - 2.0;
            double z = player.getZ() + (rand.nextDouble() - 0.5) * radius;
            level.addParticle(particle, x, y, z, 0.0, -0.02, 0.0);
        }
    }
}
