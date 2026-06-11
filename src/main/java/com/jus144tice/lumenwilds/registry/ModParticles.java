/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Particle types added by The Lumenwilds (Phase 7b — atmosphere). Each is a plain {@link SimpleParticleType}
 * (no extra data); the client-side render factory is wired in {@code client.LumenwildsClient} via
 * {@code RegisterParticleProvidersEvent}, and each needs a {@code assets/lumenwilds/particles/<name>.json}
 * texture-list + a {@code textures/particle/<name>.png} sprite.
 *
 * <ul>
 *   <li>{@link #LUMEN_SPORE} — the dimension's signature drifting glow mote (biome ambience + the portal).</li>
 *   <li>{@link #GLOW_POLLEN} — gentle floating pollen for the flower-rich biomes.</li>
 *   <li>{@link #CRYSTAL_SHIMMER} — a sparkle for the Glasspetal Crags.</li>
 * </ul>
 */
public final class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Lumenwilds.MOD_ID);

    /** {@code overrideLimiter = false}: respects the client particle budget (these are ambient, not critical). */
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LUMEN_SPORE =
            PARTICLES.register("lumen_spore", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GLOW_POLLEN =
            PARTICLES.register("glow_pollen", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CRYSTAL_SHIMMER =
            PARTICLES.register("crystal_shimmer", () -> new SimpleParticleType(false));

    private ModParticles() {}
}
