/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Brewable potions for the Phase 8a status effects (Phase 8h). Each wraps one {@code ModMobEffects} effect;
 * the brewing mixes (awkward potion + a Lumenwilds ingredient → potion) are wired in {@code event.ModBrewing}.
 * The drinkable / splash / lingering / tipped-arrow item variants are vanilla and appear automatically.
 */
public final class ModPotions {

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, Lumenwilds.MOD_ID);

    public static final DeferredHolder<Potion, Potion> LIGHTFOOT = POTIONS.register(
            "lightfoot", () -> new Potion("lightfoot", new MobEffectInstance(ModMobEffects.LIGHTFOOT, 1800, 0)));

    public static final DeferredHolder<Potion, Potion> GLOWMARKED = POTIONS.register(
            "glowmarked", () -> new Potion("glowmarked", new MobEffectInstance(ModMobEffects.GLOWMARKED, 1200, 0)));

    public static final DeferredHolder<Potion, Potion> SPOREBLIND = POTIONS.register(
            "sporeblind", () -> new Potion("sporeblind", new MobEffectInstance(ModMobEffects.SPOREBLIND, 600, 0)));

    public static final DeferredHolder<Potion, Potion> ROOTED =
            POTIONS.register("rooted", () -> new Potion("rooted", new MobEffectInstance(ModMobEffects.ROOTED, 400, 0)));

    private ModPotions() {}
}
