/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The portal interior block ({@code lumenwilds:lumen_portal}).
 *
 * <p>Phase 1 placeholder: a non-solid, no-collision, light-emitting block that occupies the inside of
 * a Lumenbound Stone frame. It has an empty outline shape (you can walk through it) and glows, so it
 * "reads" as a portal, but it does NOT teleport yet.</p>
 *
 * <p>TODO (Phase 2): on entity collision, hand off to {@link LumenPortalTeleporter} /
 * {@link LumenPortalManager} to move the entity to {@code lumenwilds:lumenwilds} (and back), with a
 * cooldown and the usual "stand in portal for N ticks" delay, mirroring Nether portal behaviour.</p>
 */
public class LumenPortalBlock extends Block {

    public static final MapCodec<LumenPortalBlock> CODEC = simpleCodec(LumenPortalBlock::new);

    public LumenPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends LumenPortalBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Empty outline so the player passes through cleanly (collision is already off via noCollission()).
        return Shapes.empty();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // TODO (Phase 2): trigger teleport via LumenPortalTeleporter once the destination dimension and
        // portal-link logic exist. Guard with a per-entity cooldown so it doesn't bounce every tick.
        super.entityInside(state, level, pos, entity);
    }
}
