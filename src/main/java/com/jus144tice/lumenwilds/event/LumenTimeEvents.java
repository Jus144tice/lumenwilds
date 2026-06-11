/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import com.jus144tice.lumenwilds.world.time.LumenwildsTickTime;
import com.jus144tice.lumenwilds.world.time.LumenwildsTimeData;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Gives the Lumenwilds its own <b>half-rate day cycle</b> (Phase 7d). On level load it (1) flips the
 * Lumenwilds {@code ServerLevel} to tick its own clock and (2) decouples that clock from the Overworld at
 * 0.5 day-time/tick → a <b>48,000-tick day</b> (twice the Overworld's). Both steps go through the Mixins in
 * {@code mixin/}; from there, the Lumenwilds advances its own time and NeoForge's per-dimension time sync
 * (the {@code ClientboundCustomSetTimePayload}, which carries the per-tick rate) keeps Lumenwilds clients
 * smoothly in step — no once-per-second snap.
 *
 * <p><b>Documented side effects of decoupling:</b> sleeping in the Overworld no longer advances Lumenwilds
 * time (its clock is independent), and the two dimensions' day times diverge over time. Game-tick-based
 * timing (redstone, scheduling) is unaffected — only the day cycle is decoupled.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenTimeEvents {

    /** 0.5 day-time units per game tick → a 48,000-tick Lumenwilds day (twice the Overworld). */
    private static final float LUMENWILDS_DAY_TIME_PER_TICK = 0.5F;

    private LumenTimeEvents() {}

    @SubscribeEvent
    public static void onLevelLoad(final LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level
                && level.dimension().equals(LumenDimensionConstants.LUMENWILDS_LEVEL)) {
            ((LumenwildsTickTime) level).lumenwilds$setTickTime(true);
            if (level.getLevelData() instanceof LumenwildsTimeData data && !data.lumenwilds$isDecoupled()) {
                data.lumenwilds$decouple(level.getDayTime(), LUMENWILDS_DAY_TIME_PER_TICK);
            }
        }
    }
}
