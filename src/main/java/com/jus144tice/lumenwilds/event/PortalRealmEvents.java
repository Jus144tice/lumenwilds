/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Keeps each portal type "targeted" to its two realms — a portal is a no-op in the third (alien) realm (v1.6.1).
 *
 * <p>This handler covers the vanilla side: a <b>Nether portal (obsidian)</b> is the Overworld↔Nether pair, so it
 * must be inert in <b>the Lumenwilds</b>. Cancelling {@link BlockEvent.PortalSpawnEvent} there means lighting an
 * obsidian frame in the Lumenwilds just makes fire, no portal — you use a Duskglass Dusk Portal to reach the
 * Nether from the Lumenwilds. (The event fires only for vanilla portals; the Lumen/Dusk portals build via their
 * own shape classes and are unaffected.) The Lumen portal's own Overworld↔Lumenwilds gating lives in
 * {@code item.LumenStrikerItem} + {@code portal.LumenPortalBlock}; the Dusk portal's Lumenwilds↔Nether gating in
 * {@code item.DuskStrikerItem} + {@code portal.DuskPortalBlock}.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class PortalRealmEvents {

    private PortalRealmEvents() {}

    @SubscribeEvent
    public static void onNetherPortalSpawn(final BlockEvent.PortalSpawnEvent event) {
        if (event.getLevel() instanceof Level level && level.dimension() == LumenDimensionConstants.LUMENWILDS_LEVEL) {
            event.setCanceled(true);
        }
    }
}
