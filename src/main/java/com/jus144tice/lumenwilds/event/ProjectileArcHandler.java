/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.effects.LowGravityHandler;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * Gives ballistic projectiles subtly flatter arcs inside the Lumenwilds, matching the dimension's low
 * gravity (the bible: "arrows, thrown tridents, snowballs, eggs … have slightly flatter arcs").
 *
 * <p>Vanilla applies a fixed downward acceleration to each projectile every tick (arrows ~0.05, thrown
 * items ~0.03); the gravity attribute does NOT apply to non-living projectiles. So after a projectile
 * ticks we restore a fraction of the gravity it just lost, flattening the arc and extending range
 * without turning the game into a different one. Conservative and limited to the projectile types the
 * bible calls out.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class ProjectileArcHandler {

    /** Fraction of per-tick gravity to restore (0 = vanilla arcs, 1 = no gravity). */
    private static final double FLATTEN_FRACTION = 0.4D;

    private static final double ARROW_GRAVITY = 0.05D;
    private static final double THROWABLE_GRAVITY = 0.03D;

    private ProjectileArcHandler() {}

    @SubscribeEvent
    public static void onEntityTick(final EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractArrow) && !(event.getEntity() instanceof ThrowableProjectile)) {
            return;
        }
        var projectile = event.getEntity();
        if (projectile.level().isClientSide || !LowGravityHandler.isInLumenwilds(projectile)) {
            return;
        }
        Vec3 motion = projectile.getDeltaMovement();
        if (projectile.onGround() || motion.lengthSqr() < 1.0E-6D) {
            return; // stuck/at rest — don't make a grounded arrow twitch upward.
        }
        double gravity = projectile instanceof AbstractArrow ? ARROW_GRAVITY : THROWABLE_GRAVITY;
        projectile.setDeltaMovement(motion.add(0.0D, gravity * FLATTEN_FRACTION, 0.0D));
    }
}
