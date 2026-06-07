/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Particle types added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO (Phase 4): drifting spores, portal sparkle, glow-pollen motes, sporefall weather. Register
 * the {@code ParticleType} here and a client-side factory via {@code RegisterParticleProvidersEvent}.</p>
 */
public final class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Lumenwilds.MOD_ID);

    private ModParticles() {}
}
