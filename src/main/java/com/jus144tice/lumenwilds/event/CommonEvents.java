/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * General game-bus event handlers that don't belong to a more specific topic. Registered
 * automatically via {@link EventBusSubscriber} (no manual hookup in {@code Lumenwilds}).
 *
 * <p>Phase 1: just a login log line, to confirm the handler wiring works. Add more cross-cutting game
 * events here as the mod grows; keep dimension-specific logic in {@link PlayerDimensionEvents}.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class CommonEvents {

    private CommonEvents() {}

    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        Lumenwilds.LOGGER.debug(
                "[{}] Player {} logged in.",
                Lumenwilds.MOD_ID,
                event.getEntity().getName().getString());
    }
}
