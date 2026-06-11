/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.block.LumenAnchorBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/**
 * Looks up a Lumen Anchor link near a portal (Phase 8c). If a {@link LumenAnchorBlockEntity} within a small
 * radius of the source portal is linked to an anchor in the destination dimension, the teleporter routes
 * there (precise return) instead of find-or-building at scaled coordinates.
 */
public final class LumenAnchorLinks {

    /** How far from the portal opening to look for a governing anchor. */
    private static final int SEARCH_RADIUS = 4;

    private LumenAnchorLinks() {}

    /**
     * @return the linked target position in {@code destination} if a nearby anchor links there, else {@code null}.
     */
    @Nullable
    public static BlockPos findLinkedTarget(
            ServerLevel sourceLevel, BlockPos portalPos, ResourceKey<Level> destination) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dy = -SEARCH_RADIUS; dy <= SEARCH_RADIUS; dy++) {
                for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                    cursor.set(portalPos.getX() + dx, portalPos.getY() + dy, portalPos.getZ() + dz);
                    if (sourceLevel.getBlockEntity(cursor) instanceof LumenAnchorBlockEntity anchor) {
                        GlobalPos linked = anchor.getLinkedTo();
                        if (linked != null && linked.dimension().equals(destination)) {
                            return linked.pos();
                        }
                    }
                }
            }
        }
        return null;
    }
}
