/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.time;

/**
 * Duck-type interface mixed into the Lumenwilds' {@code DerivedLevelData} (see
 * {@code mixin.DerivedLevelDataMixin}) so its day-time clock can be <b>decoupled</b> from the Overworld's.
 *
 * <p>Non-Overworld dimensions normally read their time straight from the Overworld via {@code DerivedLevelData}
 * (its {@code setDayTime} is a no-op). Once decoupled, the Lumenwilds stores its own {@code dayTime} /
 * {@code dayTimeFraction} / {@code dayTimePerTick}, letting it run at half rate (a 48,000-tick day).</p>
 */
public interface LumenwildsTimeData {

    /**
     * Begin tracking an independent clock, seeded from the current (derived) day time.
     *
     * @param startDayTime the day time to start from (so the cutover is seamless)
     * @param dayTimePerTick day-time units advanced per game tick (0.5 → half rate / 48k-tick day)
     */
    void lumenwilds$decouple(long startDayTime, float dayTimePerTick);

    boolean lumenwilds$isDecoupled();
}
