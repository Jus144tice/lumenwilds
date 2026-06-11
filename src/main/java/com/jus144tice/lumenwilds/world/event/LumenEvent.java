/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.event;

/**
 * The Lumenwilds' ambient "weather" events (Phase 7d.2). One is active at a time (or {@link #NONE}), chosen
 * on a timer by {@code LumenEventManager} and synced to clients (by ordinal id) via {@code network}.
 *
 * <ul>
 *   <li>{@link #SPOREFALL} — drifting spores + boosted Sporeling spawns (Sporefall Jungle).</li>
 *   <li>{@link #MOONWAKE} — a bright, calm night: brighter Veyra + more Lantern Beetles (night only).</li>
 *   <li>{@link #DEEP_HUSH} — an uneasy underground quiet: more hostiles near deep players.</li>
 * </ul>
 */
public enum LumenEvent {
    NONE,
    SPOREFALL,
    MOONWAKE,
    DEEP_HUSH;

    private static final LumenEvent[] BY_ID = values();

    public int id() {
        return this.ordinal();
    }

    public static LumenEvent byId(int id) {
        return (id >= 0 && id < BY_ID.length) ? BY_ID[id] : NONE;
    }
}
