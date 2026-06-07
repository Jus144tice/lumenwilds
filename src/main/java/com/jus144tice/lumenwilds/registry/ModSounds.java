/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Sound events added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO: portal ambience/whoosh, striker ignition, dimension ambience, mob sounds. Register with
 * {@code SoundEvent.createVariableRangeEvent(ResourceLocationHelper.modLoc("..."))} and add matching
 * entries to {@code assets/lumenwilds/sounds.json}.</p>
 */
public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Lumenwilds.MOD_ID);

    private ModSounds() {}
}
