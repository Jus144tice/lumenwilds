/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModMobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

/**
 * Drives {@link ModMobEffects#GLOWMARKED}'s outline (Phase 8a): toggle the entity's glowing tag when the
 * effect is added / removed / expires (rather than every tick). Game-bus, server-side; the flag syncs to
 * clients, which draw the vanilla glowing outline.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenEffectEvents {

    private LumenEffectEvents() {}

    @SubscribeEvent
    public static void onAdded(final MobEffectEvent.Added event) {
        if (event.getEffectInstance().is(ModMobEffects.GLOWMARKED)) {
            event.getEntity().setGlowingTag(true);
        }
    }

    @SubscribeEvent
    public static void onExpired(final MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().is(ModMobEffects.GLOWMARKED)) {
            event.getEntity().setGlowingTag(false);
        }
    }

    @SubscribeEvent
    public static void onRemoved(final MobEffectEvent.Remove event) {
        if (event.getEffect().value() == ModMobEffects.GLOWMARKED.value()) {
            event.getEntity().setGlowingTag(false);
        }
    }
}
