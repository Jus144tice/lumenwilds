/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import net.minecraft.world.level.block.Block;

/**
 * Gravity Repeater (Phase 11b) — the player's range-extender for liftshafts. A full, flush block meant to be
 * built into a shaft's wall: whenever one of its faces touches an active gravity field cell, it re-projects the
 * field another full range beyond, so columns can be made arbitrarily tall without anything cluttering the
 * shaft. Purely a marker — all the logic is in {@link LiftShaftNetwork}, which on the flood resets its range
 * budget whenever a column cell is orthogonally adjacent to a repeater. Glows faintly (light 3).
 */
public class GravityRepeaterBlock extends Block {

    public GravityRepeaterBlock(Properties properties) {
        super(properties);
    }
}
