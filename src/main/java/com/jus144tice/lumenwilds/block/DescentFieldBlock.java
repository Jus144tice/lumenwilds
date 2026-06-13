/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModParticles;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Descent Field (Phase 11a) — the downward gravity column of a Lumenwright liftshaft. It does not feel like
 * falling; it feels like being lowered. While an entity is inside it holds the descent in a gentle band: if the
 * entity is sinking slower than {@link #DOWN_SLOW} it nudges it down, if faster than {@link #DOWN_FAST} it eases
 * it back up, otherwise it leaves it. Fall distance is zeroed every tick, so there is never lethal fall damage,
 * and the rider can step out sideways at any height. <b>Sneaking</b> holds position. Glows (light 5).
 */
public class DescentFieldBlock extends AbstractFieldBlock {

    public static final MapCodec<DescentFieldBlock> CODEC = simpleCodec(DescentFieldBlock::new);

    /** Push down if the entity is sinking slower than this (magnitude). */
    private static final double DOWN_SLOW = 0.35;
    /** Ease back up if the entity is sinking faster than this (magnitude). */
    private static final double DOWN_FAST = 0.45;

    public DescentFieldBlock(Properties properties) {
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
            ny = approach(m.y, 0.0); // sneak holds position
        } else if (m.y > -DOWN_SLOW) {
            ny = Math.max(-DOWN_SLOW, m.y - STEP); // too slow → push down to the band
        } else if (m.y < -DOWN_FAST) {
            ny = Math.min(-DOWN_FAST, m.y + STEP); // too fast → ease up to the band
        } else {
            ny = m.y; // in the safe band
        }
        entity.setDeltaMovement(m.x * 0.9, ny, m.z * 0.9);
        entity.resetFallDistance();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) {
            double x = pos.getX() + 0.25 + random.nextDouble() * 0.5;
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + 0.25 + random.nextDouble() * 0.5;
            level.addParticle(ModParticles.LUMEN_SPORE.get(), x, y, z, 0.0, -(0.10 + random.nextDouble() * 0.06), 0.0);
        }
    }
}
