/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;

/**
 * Builds the {@link DimensionTransition} that lands an entity at its destination Dusk portal (the sibling of
 * {@link LumenPortalTeleporter}). Finds-or-builds the exit via {@link DuskPortalManager}, centres the entity at
 * the base of the opening (collision-free), zeroes its momentum, and attaches the standard portal post effects.
 */
public final class DuskPortalTeleporter {

    private DuskPortalTeleporter() {}

    @Nullable
    public static DimensionTransition createDestinationTransition(
            ServerLevel target, Entity entity, BlockPos approx, Direction.Axis axis) {
        DuskPortalManager.ExitPortal exit = DuskPortalManager.getOrCreateExitPortal(target, approx, axis);
        if (exit == null) {
            return null;
        }

        Direction right = exit.axis() == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockPos bottomLeft = exit.bottomLeftInterior();
        double centerX = bottomLeft.getX() + 0.5 + right.getStepX() * (exit.width() - 1) / 2.0;
        double centerZ = bottomLeft.getZ() + 0.5 + right.getStepZ() * (exit.width() - 1) / 2.0;
        Vec3 base = new Vec3(centerX, bottomLeft.getY(), centerZ);

        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        Vec3 placed = PortalShape.findCollisionFreePosition(base, target, entity, dimensions);

        float yaw = exit.axis() == Direction.Axis.X ? 180.0F : 90.0F;

        return new DimensionTransition(
                target,
                placed,
                Vec3.ZERO,
                yaw,
                entity.getXRot(),
                DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
    }
}
