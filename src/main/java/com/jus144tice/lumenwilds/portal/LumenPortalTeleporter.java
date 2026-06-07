/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

/**
 * Moves an entity between the overworld and {@code lumenwilds:lumenwilds}.
 *
 * <p>Phase 1 placeholder: {@link #teleport} only logs intent. The class deliberately implements no
 * vanilla teleport interface yet, because 1.21.1 routes cross-dimension travel through
 * {@code Entity#changeDimension(DimensionTransition)} — wiring that up (placement, portal-link search,
 * cooldown) is Phase 2 work.</p>
 */
public final class LumenPortalTeleporter {

    private LumenPortalTeleporter() {}

    /**
     * Send {@code entity} to the Lumenwilds (or back to its return dimension).
     *
     * <p>TODO (Phase 2): resolve the destination {@link ServerLevel} via
     * {@link LumenDimensionConstants#LUMENWILDS_LEVEL}, find/create the matching portal through
     * {@link LumenPortalManager}, build a {@code DimensionTransition} (target pos, speed, yaw/pitch,
     * post-teleport effects) and call {@code entity.changeDimension(transition)}.</p>
     */
    public static void teleport(Entity entity, ServerLevel destination) {
        Lumenwilds.LOGGER.info(
                "[{}] Teleport requested for {} -> {} (Phase 2 TODO: not yet implemented).",
                Lumenwilds.MOD_ID,
                entity.getName().getString(),
                destination.dimension().location());
    }
}
