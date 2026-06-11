/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.time;

/**
 * Duck-type interface mixed into {@code ServerLevel} (see {@code mixin.ServerLevelMixin}) to flip the
 * otherwise-{@code final} {@code tickTime} flag. Vanilla constructs only the Overworld with {@code tickTime
 * = true}; the Lumenwilds needs it enabled so its {@code tickTime()} (and thus its own day clock) runs.
 */
public interface LumenwildsTickTime {

    void lumenwilds$setTickTime(boolean value);
}
