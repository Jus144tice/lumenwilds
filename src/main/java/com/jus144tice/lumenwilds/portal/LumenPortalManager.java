/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * High-level orchestrator for Lumenbound Stone portals: validating a frame, filling/clearing the
 * {@code lumen_portal} interior, and (eventually) linking a portal to its counterpart in the other
 * dimension.
 *
 * <p>Phase 1 placeholder: {@link #tryActivatePortal} performs the frame check via
 * {@link LumenPortalShape} and logs the outcome, but does not place portal blocks yet.</p>
 */
public final class LumenPortalManager {

    private LumenPortalManager() {}

    /**
     * Attempt to activate a portal whose frame contains/adjoins {@code framePos}.
     *
     * @return {@code true} if a valid frame was found and (eventually) filled; {@code false} otherwise.
     */
    public static boolean tryActivatePortal(Level level, BlockPos framePos) {
        return LumenPortalShape.findEmptyPortalShape(level, framePos)
                .map(shape -> {
                    // TODO (Phase 2): fill the interior with ModBlocks.LUMEN_PORTAL using the shape's
                    // bounds/axis, register the portal, and play ignition sound/particles.
                    Lumenwilds.LOGGER.info(
                            "[{}] Valid Lumenbound frame found at {} — filling TODO.", Lumenwilds.MOD_ID, framePos);
                    return true;
                })
                .orElseGet(() -> {
                    Lumenwilds.LOGGER.info(
                            "[{}] No valid Lumenbound Stone frame around {} yet (frame detection is a Phase 2 TODO).",
                            Lumenwilds.MOD_ID,
                            framePos);
                    return false;
                });
    }

    // TODO (Phase 2): BlockPos findOrCreateDestinationPortal(ServerLevel origin, ServerLevel dest, Entity e)
    //   — mirror Nether portal search/creation (PortalForcer-style) to place a return frame.
}
