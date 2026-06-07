/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.effects;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * Applies the Lumenwilds' reduced gravity and soft landings — but ONLY while the entity is inside
 * {@code lumenwilds:lumenwilds}.
 *
 * <p>Implemented entirely with vanilla 1.20.5+ movement attributes via transient modifiers (added on
 * dimension enter, removed on exit) — no tick-by-tick velocity hacking, no client/server desync. Stable
 * {@link ResourceLocation} ids keep the modifiers from stacking and make removal exact.</p>
 *
 * <ul>
 *   <li>{@link Attributes#GRAVITY} ×{@value #GRAVITY_MULTIPLIER} — floaty falls. Because jump height ∝
 *       1/gravity for a fixed jump impulse, this alone lifts the player's jump from ~1.25 to ~1.79
 *       blocks, matching the bible's "~1.75". We therefore do NOT also modify {@code JUMP_STRENGTH}
 *       (that would double-count and overshoot).</li>
 *   <li>{@link Attributes#SAFE_FALL_DISTANCE} +3 — fall damage starts at ~6 blocks instead of ~3.</li>
 *   <li>{@link Attributes#FALL_DAMAGE_MULTIPLIER} −0.5 — fall damage halved.</li>
 * </ul>
 *
 * <p>Native Lumenwilds mobs will get reduced gravity in their own attribute suppliers (Phase 6), so this
 * dimension hook stays player-only and doesn't perturb Overworld mobs that wander through.</p>
 */
public final class LowGravityHandler {

    private LowGravityHandler() {}

    /** Gravity scale inside the Lumenwilds (1.0 = vanilla). */
    public static final double GRAVITY_MULTIPLIER = 0.7D;

    /** Extra blocks of safe-fall distance added inside the dimension (vanilla base is 3). */
    public static final double SAFE_FALL_BONUS = 3.0D;

    /** Fall-damage reduction inside the dimension (−0.5 on the 1.0 base = ×0.5 damage). */
    public static final double FALL_DAMAGE_REDUCTION = -0.5D;

    private static final ResourceLocation GRAVITY_ID = ResourceLocationHelper.modLoc("lumenwilds_low_gravity");
    private static final ResourceLocation SAFE_FALL_ID = ResourceLocationHelper.modLoc("lumenwilds_safe_fall");
    private static final ResourceLocation FALL_DAMAGE_ID = ResourceLocationHelper.modLoc("lumenwilds_soft_landing");

    private static final AttributeModifier GRAVITY_MOD = new AttributeModifier(
            GRAVITY_ID, GRAVITY_MULTIPLIER - 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final AttributeModifier SAFE_FALL_MOD =
            new AttributeModifier(SAFE_FALL_ID, SAFE_FALL_BONUS, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier FALL_DAMAGE_MOD =
            new AttributeModifier(FALL_DAMAGE_ID, FALL_DAMAGE_REDUCTION, AttributeModifier.Operation.ADD_VALUE);

    /** True if the entity is currently in the Lumenwilds dimension. */
    public static boolean isInLumenwilds(Entity entity) {
        return entity.level().dimension().equals(LumenDimensionConstants.LUMENWILDS_LEVEL);
    }

    /** Add or remove the Lumenwilds movement modifiers to match the entity's current dimension. */
    public static void refresh(LivingEntity entity) {
        if (entity.level().isClientSide) {
            return; // server-authoritative; the GRAVITY attribute is syncable and reaches the client.
        }
        if (isInLumenwilds(entity)) {
            apply(entity);
        } else {
            remove(entity);
        }
    }

    /** Convenience for the dimension-change hook ({@code event.PlayerDimensionEvents}). */
    public static void onChangedDimension(Player player) {
        refresh(player);
    }

    private static void apply(LivingEntity entity) {
        put(entity, Attributes.GRAVITY, GRAVITY_MOD);
        put(entity, Attributes.SAFE_FALL_DISTANCE, SAFE_FALL_MOD);
        put(entity, Attributes.FALL_DAMAGE_MULTIPLIER, FALL_DAMAGE_MOD);
    }

    /** Strip the Lumenwilds modifiers (safe to call even if they aren't present). */
    public static void remove(LivingEntity entity) {
        rm(entity, Attributes.GRAVITY, GRAVITY_ID);
        rm(entity, Attributes.SAFE_FALL_DISTANCE, SAFE_FALL_ID);
        rm(entity, Attributes.FALL_DAMAGE_MULTIPLIER, FALL_DAMAGE_ID);
    }

    private static void put(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.addOrUpdateTransientModifier(modifier);
        }
    }

    private static void rm(LivingEntity entity, Holder<Attribute> attribute, ResourceLocation id) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }
}
