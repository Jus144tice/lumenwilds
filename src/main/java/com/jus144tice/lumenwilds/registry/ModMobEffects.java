/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.effect.LumenMobEffect;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The Lumenwilds' status effects (Phase 8a). Most are pure attribute modifiers (server-verifiable);
 * Glowmarked's outline is applied in {@code event.LumenEffectEvents}, and the Sporeblind/visibility overlay
 * is a Phase 9 client render (Sporeblind here is the slow + a marker the Sporeling cloud applies).
 *
 * <ul>
 *   <li>{@link #LIGHTFOOT} (beneficial) — higher jumps + a longer safe fall (Air Gel / Lumen Nectar brews).</li>
 *   <li>{@link #GLOWMARKED} (neutral) — the target glows / is easy to see (glow-pollen, Glowmoth scales).</li>
 *   <li>{@link #SPOREBLIND} (harmful) — a spore-clouded slow (Sporeling clouds / Sporefall).</li>
 *   <li>{@link #ROOTED} (harmful) — heavily slowed + can't jump (root traps / Shade Stalker hits).</li>
 * </ul>
 */
public final class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Lumenwilds.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> LIGHTFOOT =
            MOB_EFFECTS.register("lightfoot", () -> new LumenMobEffect(MobEffectCategory.BENEFICIAL, 0x8FE0E8)
                    .addAttributeModifier(
                            Attributes.JUMP_STRENGTH,
                            ResourceLocationHelper.modLoc("effect.lightfoot.jump"),
                            0.12,
                            AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(
                            Attributes.SAFE_FALL_DISTANCE,
                            ResourceLocationHelper.modLoc("effect.lightfoot.fall"),
                            3.0,
                            AttributeModifier.Operation.ADD_VALUE));

    /** No attribute modifier — the glow is driven by {@code event.LumenEffectEvents} (setGlowingTag). */
    public static final DeferredHolder<MobEffect, MobEffect> GLOWMARKED =
            MOB_EFFECTS.register("glowmarked", () -> new LumenMobEffect(MobEffectCategory.NEUTRAL, 0xE8F0FF));

    public static final DeferredHolder<MobEffect, MobEffect> SPOREBLIND =
            MOB_EFFECTS.register("sporeblind", () -> new LumenMobEffect(MobEffectCategory.HARMFUL, 0x4E6B3A)
                    .addAttributeModifier(
                            Attributes.MOVEMENT_SPEED,
                            ResourceLocationHelper.modLoc("effect.sporeblind.slow"),
                            -0.15,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> ROOTED =
            MOB_EFFECTS.register("rooted", () -> new LumenMobEffect(MobEffectCategory.HARMFUL, 0x5A4632)
                    .addAttributeModifier(
                            Attributes.MOVEMENT_SPEED,
                            ResourceLocationHelper.modLoc("effect.rooted.slow"),
                            -0.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(
                            Attributes.JUMP_STRENGTH,
                            ResourceLocationHelper.modLoc("effect.rooted.jump"),
                            -0.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    private ModMobEffects() {}
}
