/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import com.jus144tice.lumenwilds.world.event.LumenEventManager;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Ticks {@link LumenEventManager} once per Lumenwilds level tick (Phase 7d.2), and resets it when the server
 * stops (the manager's state is transient per session). Game-bus, server-side.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenEventDriver {

    private LumenEventDriver() {}

    @SubscribeEvent
    public static void onLevelTick(final LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level
                && level.dimension().equals(LumenDimensionConstants.LUMENWILDS_LEVEL)) {
            LumenEventManager.tick(level);
        }
    }

    @SubscribeEvent
    public static void onServerStopping(final ServerStoppingEvent event) {
        LumenEventManager.reset();
    }
}
