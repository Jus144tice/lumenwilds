/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import net.minecraft.world.level.block.Block;

/**
 * Lumen Relay (Phase 10e.2) — a small ancient device that repeats resonance across gaps. The
 * {@link ResonanceNetwork} flood treats a relay as a conduit node <em>and</em> bridges from it to other
 * relays a short distance away (so a network can jump a wall or span an open plaza). A faint constant glow.
 * Also a crafting component for later gravity tech (the Lumen Field Projector).
 */
public class LumenRelayBlock extends Block {

    public LumenRelayBlock(Properties properties) {
        super(properties);
    }
}
