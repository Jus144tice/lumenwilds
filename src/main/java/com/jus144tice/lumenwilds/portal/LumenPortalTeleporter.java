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
 * Builds the {@link DimensionTransition} that places an entity at its destination Lumen portal.
 *
 * <p>It finds-or-builds the exit portal via {@link LumenPortalManager}, centres the entity at the base of
 * the opening (collision-free, via vanilla {@link PortalShape#findCollisionFreePosition}), stops its
 * momentum so it doesn't shoot out, and attaches the standard portal-sound + chunk-ticket post effects.</p>
 */
public final class LumenPortalTeleporter {

    private LumenPortalTeleporter() {}

    /**
     * Resolve (or create) the exit portal in {@code target} near the scaled {@code approx} position and
     * return a transition that lands {@code entity} in it. Returns {@code null} if no portal could be
     * found or built.
     */
    @Nullable
    public static DimensionTransition createDestinationTransition(
            ServerLevel target, Entity entity, BlockPos approx, Direction.Axis axis) {
        LumenPortalManager.ExitPortal exit = LumenPortalManager.getOrCreateExitPortal(target, approx, axis);
        if (exit == null) {
            return null;
        }

        Direction right = exit.axis() == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockPos bottomLeft = exit.bottomLeftInterior();
        // Centre of the opening along its width, at the base of the interior.
        double centerX = bottomLeft.getX() + 0.5 + right.getStepX() * (exit.width() - 1) / 2.0;
        double centerZ = bottomLeft.getZ() + 0.5 + right.getStepZ() * (exit.width() - 1) / 2.0;
        Vec3 base = new Vec3(centerX, bottomLeft.getY(), centerZ);

        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        Vec3 placed = PortalShape.findCollisionFreePosition(base, target, entity, dimensions);

        // Face perpendicular to the portal plane (out of the opening).
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
