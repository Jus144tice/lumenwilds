/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.block.GravityLensBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * The Gravity Lens lift (Phase 10e.2). A powered {@link GravityLensBlock} a few blocks below a living entity
 * gently floats it upward — controlled velocity (capped, not real gravity reversal), fall distance zeroed,
 * sneaking holds position — so a single lens reads as a soft anti-gravity riser. This is the velocity-field
 * approach the liftshafts ({@code docs/lumenwright_liftshafts.txt}) will scale up into full descent/ascension
 * columns; doing it this way now keeps the two compatible.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenGravityEvents {

    private LumenGravityEvents() {}

    private static final int REACH_BELOW = 4;
    private static final double TARGET_SPEED = 0.30;
    private static final double STEP = 0.08;

    @SubscribeEvent
    public static void onEntityTick(final EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        Level level = entity.level();
        if (level.isClientSide) {
            return;
        }
        BlockPos.MutableBlockPos p = entity.blockPosition().mutable();
        for (int i = 1; i <= REACH_BELOW; i++) {
            p.setY(entity.blockPosition().getY() - i);
            BlockState state = level.getBlockState(p);
            if (state.getBlock() instanceof GravityLensBlock && state.getValue(GravityLensBlock.POWERED)) {
                lift(entity);
                return;
            }
            // A solid block between the entity and a lens blocks the lift (it's standing on the floor).
            if (!state.getCollisionShape(level, p).isEmpty()) {
                return;
            }
        }
    }

    private static void lift(LivingEntity entity) {
        double target = entity.isShiftKeyDown() ? 0.0 : TARGET_SPEED;
        Vec3 m = entity.getDeltaMovement();
        double ny = m.y < target ? Math.min(target, m.y + STEP) : m.y;
        entity.setDeltaMovement(m.x * 0.9, ny, m.z * 0.9);
        entity.resetFallDistance();
        entity.hasImpulse = true;
        if (entity instanceof net.minecraft.server.level.ServerPlayer sp) {
            sp.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(sp));
        }
    }
}
