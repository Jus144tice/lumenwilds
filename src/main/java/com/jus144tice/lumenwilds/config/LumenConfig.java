/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Tunable gameplay knobs (Phase 9h), a {@code COMMON} {@link ModConfigSpec} registered in the {@code Lumenwilds}
 * ctor. Read at runtime by the systems they govern so a config edit takes effect on the next dimension entry /
 * world load:
 *
 * <ul>
 *   <li>{@link #GRAVITY_STRENGTH} → {@code effects.LowGravityHandler} (the {@code GRAVITY} attribute multiplier).</li>
 *   <li>{@link #AMBIENT_EVENTS} → {@code world.event.LumenEventManager} (whether Sporefall/Moonwake/Deep Hush roll).</li>
 *   <li>{@link #DAY_CYCLE_MULTIPLIER} → {@code event.LumenTimeEvents} (day-time per tick; 0.5 = the 48k half-rate day).</li>
 * </ul>
 */
public final class LumenConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue GRAVITY_STRENGTH;
    public static final ModConfigSpec.BooleanValue AMBIENT_EVENTS;
    public static final ModConfigSpec.DoubleValue DAY_CYCLE_MULTIPLIER;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.push("lumenwilds");

        GRAVITY_STRENGTH = b.comment(
                        "Lumenwilds gravity as a fraction of normal (lower = floatier). 1.0 disables low gravity.")
                .defineInRange("gravityStrength", 0.7, 0.1, 1.0);

        AMBIENT_EVENTS = b.comment("Enable the ambient events (Sporefall / Moonwake / Deep Hush).")
                .define("ambientEvents", true);

        DAY_CYCLE_MULTIPLIER = b.comment(
                        "Lumenwilds day-time advanced per tick. 0.5 = the 48000-tick half-rate day; 1.0 = Overworld rate.")
                .defineInRange("dayCycleMultiplier", 0.5, 0.1, 2.0);

        b.pop();
        SPEC = b.build();
    }

    private LumenConfig() {}
}
