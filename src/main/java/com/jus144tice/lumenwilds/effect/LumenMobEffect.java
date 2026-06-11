/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Trivial public-constructor {@link MobEffect} (Phase 8a) — vanilla's constructor is {@code protected}, so a
 * subclass is needed to instantiate plain attribute-driven effects in {@code registry.ModMobEffects}. Effects
 * whose behaviour is purely attribute modifiers (Lightfoot / Sporeblind / Rooted) use this directly;
 * Glowmarked's glow is applied via {@code event.LumenEffectEvents}.
 */
public class LumenMobEffect extends MobEffect {

    public LumenMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
