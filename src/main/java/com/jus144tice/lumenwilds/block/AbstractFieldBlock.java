/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Shared base for the Lumenwright liftshaft field blocks (Phase 11a) — the {@link AscensionFieldBlock} and
 * {@link DescentFieldBlock} that fill a gravity shaft. Built like {@code portal.LumenPortalBlock}: a
 * non-solid, no-collision, unbreakable, light-emitting cell with no selection outline ({@link Shapes#empty()}),
 * so the player passes straight through and never accidentally targets it. The ride physics are the bible's
 * "controlled velocity, not real gravity reversal" approach (mirrors {@code event.LumenGravityEvents#lift}):
 * each tick an entity is inside, the field eases its vertical speed toward a capped target and zeroes its fall
 * distance, then re-syncs a {@link ServerPlayer}'s motion to the client.
 *
 * <p>These cells are projected/cleared by a {@code LumenFieldProjectorBlock} (and pre-placed in ruins), never
 * hand-placed — they have no BlockItem and {@code noLootTable}.</p>
 */
public abstract class AbstractFieldBlock extends Block {

    /** Per-tick velocity nudge toward the field's target speed. */
    protected static final double STEP = 0.08;

    protected AbstractFieldBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) {
            return;
        }
        applyField(entity);
        resync(entity);
    }

    /** Apply this field's controlled vertical velocity to {@code entity} (server-side, once per tick inside). */
    protected abstract void applyField(Entity entity);

    /** The drifting glint particle this field emits. */
    protected abstract SimpleParticleType mote();

    /** +1 for an upward field, −1 for a downward field — drives particle direction. */
    protected abstract double riseSign();

    /** Play this field's pitched hum (called rarely from {@link #animateTick}). */
    protected abstract void hum(Level level, BlockPos pos, RandomSource random);

    /**
     * The shared "alive" feel: a directional stream of motes, a horizontal energy ring every few cells (so the
     * column reads as banded pulses of light), and an occasional pitched hum. Subclasses just supply the mote,
     * direction, and hum.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double sign = riseSign();
        SimpleParticleType mote = mote();
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + 0.15 + random.nextDouble() * 0.7;
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + 0.15 + random.nextDouble() * 0.7;
            double vy = sign * (0.18 + random.nextDouble() * 0.14);
            level.addParticle(mote, x, y, z, 0.0, vy, 0.0);
        }
        // Horizontal energy ring on every 4th cell — many cells doing this reads as travelling pulses.
        if ((pos.getY() & 3) == 0 && random.nextInt(2) == 0) {
            double cy = pos.getY() + 0.5;
            for (int k = 0; k < 8; k++) {
                double a = k / 8.0 * Math.PI * 2.0;
                double x = pos.getX() + 0.5 + Math.cos(a) * 0.45;
                double z = pos.getZ() + 0.5 + Math.sin(a) * 0.45;
                level.addParticle(mote, x, cy, z, 0.0, sign * 0.04, 0.0);
            }
        }
        if (random.nextInt(140) == 0) {
            hum(level, pos, random);
        }
    }

    /** Move {@code cur} toward {@code tgt} by at most {@link #STEP} (used for the "sneak holds position" ease). */
    protected static double approach(double cur, double tgt) {
        if (cur < tgt) {
            return Math.min(tgt, cur + STEP);
        }
        if (cur > tgt) {
            return Math.max(tgt, cur - STEP);
        }
        return cur;
    }

    /** Re-sync the entity's motion to the client so the velocity change is actually seen (players especially). */
    protected static void resync(Entity entity) {
        entity.hasImpulse = true;
        if (entity instanceof ServerPlayer sp) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
        }
    }
}
