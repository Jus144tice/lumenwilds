/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.network;

import com.jus144tice.lumenwilds.world.event.LumenEvent;

/**
 * The client's view of the active ambient event (Phase 7d.2), written by the {@link LumenEventPayload}
 * handler and read by client render/effect code (the sky's Moonwake brightening + event particles).
 *
 * <p>Deliberately a <b>common</b> holder with no client-only imports, so the payload handler (registered on
 * both sides) can reference it safely; only the readers live in {@code client}.</p>
 */
public final class LumenEventClientState {

    private static volatile LumenEvent active = LumenEvent.NONE;
    private static volatile int ticksRemaining = 0;

    private LumenEventClientState() {}

    public static void set(int eventId, int ticks) {
        active = LumenEvent.byId(eventId);
        ticksRemaining = ticks;
    }

    public static LumenEvent active() {
        return active;
    }

    public static int ticksRemaining() {
        return ticksRemaining;
    }

    public static void clear() {
        active = LumenEvent.NONE;
        ticksRemaining = 0;
    }
}
