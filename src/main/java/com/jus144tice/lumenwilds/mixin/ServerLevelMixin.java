/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.mixin;

import com.jus144tice.lumenwilds.world.time.LumenwildsTickTime;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Makes {@code ServerLevel}'s otherwise-{@code final} {@code tickTime} flag settable (Phase 7d), so the
 * Lumenwilds can be switched to tick its own clock (vanilla builds only the Overworld with {@code tickTime =
 * true}). Toggled from {@code event.LumenTimeEvents} on level load; paired with the day-time decoupling in
 * {@code DerivedLevelDataMixin}.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements LumenwildsTickTime {

    @Shadow
    @Final
    @Mutable
    private boolean tickTime;

    @Override
    public void lumenwilds$setTickTime(boolean value) {
        this.tickTime = value;
    }
}
