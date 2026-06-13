/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModParticles;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

/**
 * Ascension Field (Phase 11a) — the upward gravity column of a Lumenwright liftshaft. While an entity is
 * inside, it eases the entity's vertical speed up toward {@link #TARGET_UP} (so it floats up smoothly);
 * <b>sneaking</b> bleeds the ascent back toward zero (hold position), and a <b>jump</b> that pushes past the
 * cap is preserved (a small boost), per the bible. Fall distance is zeroed each tick, and horizontal drift is
 * damped so the rider stays centred. The player can step out sideways at any height. Glows (light 7).
 */
public class AscensionFieldBlock extends AbstractFieldBlock {

    public static final MapCodec<AscensionFieldBlock> CODEC = simpleCodec(AscensionFieldBlock::new);

    /** Target ascent speed (within the bible's +0.32…+0.45 window). */
    private static final double TARGET_UP = 0.40;

    public AscensionFieldBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void applyField(Entity entity) {
        Vec3 m = entity.getDeltaMovement();
        double ny;
        if (entity.isShiftKeyDown()) {
            ny = approach(m.y, 0.0); // sneak holds/stops the climb
        } else if (m.y < TARGET_UP) {
            ny = Math.min(TARGET_UP, m.y + STEP); // ease up to the cap
        } else {
            ny = m.y; // already at/above the cap (e.g. a jump boost) — leave it
        }
        entity.setDeltaMovement(m.x * 0.9, ny, m.z * 0.9);
        entity.resetFallDistance();
    }

    @Override
    protected SimpleParticleType mote() {
        return ModParticles.ASCENSION_MOTE.get();
    }

    @Override
    protected double riseSign() {
        return 1.0;
    }

    @Override
    protected void hum(Level level, BlockPos pos, RandomSource random) {
        // A higher-pitched antigravity hum.
        level.playLocalSound(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                SoundEvents.BEACON_AMBIENT,
                SoundSource.BLOCKS,
                0.18F,
                1.5F + random.nextFloat() * 0.2F,
                false);
    }
}
