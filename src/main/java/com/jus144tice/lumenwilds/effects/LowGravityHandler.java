/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.effects;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Applies the Lumenwilds' reduced gravity — but ONLY while the entity is inside
 * {@code lumenwilds:lumenwilds}.
 *
 * <p>Phase 1 placeholder: the dimension check is real ({@link #isInLumenwilds}) and the dimension-
 * change hook ({@link #onChangedDimension}) logs, but no movement is modified yet. This keeps the
 * scaffolding stable until we pick the clean, standard approach.</p>
 *
 * <p>TODO (Phase 4) — intended future behaviour, applied on entering the dimension and cleared on
 * leaving:</p>
 * <ul>
 *   <li>Higher jump height.</li>
 *   <li>Slower falling (reduced downward acceleration).</li>
 *   <li>Reduced/zero fall damage.</li>
 *   <li>Possibly altered projectile arcs.</li>
 *   <li>Possibly tuned Elytra glide.</li>
 * </ul>
 *
 * <p>The cleanest standard route in 1.21.1 is the vanilla {@code minecraft:generic.gravity}
 * attribute ({@code net.minecraft.world.entity.ai.attributes.Attributes#GRAVITY}): add a transient
 * {@code AttributeModifier} (MULTIPLY) when entering and remove it when leaving — no tick-by-tick
 * velocity hacking, no client/server desync. Fall damage can be softened via
 * {@code LivingFallEvent}.</p>
 */
public final class LowGravityHandler {

    private LowGravityHandler() {}

    /** Placeholder multiplier on normal gravity (1.0 = vanilla). Tuned for real in Phase 4. */
    public static final double LUMENWILDS_GRAVITY_MULTIPLIER = 0.4D;

    /** True if the entity is currently in the Lumenwilds dimension. */
    public static boolean isInLumenwilds(Entity entity) {
        return entity.level().dimension().equals(LumenDimensionConstants.LUMENWILDS_LEVEL);
    }

    /**
     * Called when a player changes dimension (see {@code event.PlayerDimensionEvents}). Currently logs
     * only; this is where the gravity {@code AttributeModifier} will be added/removed in Phase 4.
     */
    public static void onChangedDimension(Player player) {
        if (isInLumenwilds(player)) {
            Lumenwilds.LOGGER.debug(
                    "[{}] {} entered the Lumenwilds — low gravity TODO (multiplier {}).",
                    Lumenwilds.MOD_ID,
                    player.getName().getString(),
                    LUMENWILDS_GRAVITY_MULTIPLIER);
            // TODO (Phase 4): apply transient GRAVITY attribute modifier (MULTIPLY).
        } else {
            // TODO (Phase 4): remove the gravity modifier if present (player left the dimension).
        }
    }
}
