/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

/**
 * Makes <b>Lumenwater</b> cancel fall damage, like real water (v1.4.2 fix).
 *
 * <p>Why this is needed: NeoForge redirects vanilla's {@code Entity#updateFluidHeightAndDoFluidPushing(FluidTags.WATER,…)}
 * to {@code isInFluidType(NeoForgeMod.WATER_TYPE)} — i.e. the in-water fall-distance reset keys on the
 * <em>FluidType</em>, not the {@code #minecraft:water} tag. Lumenwater has its own {@code LUMENWATER_TYPE}, so
 * vanilla never resets {@code fallDistance} (or reports {@code isInWater()}) for it despite the tag membership.
 * We restore the water-like behaviour explicitly: if the falling entity is standing in any {@code #minecraft:water}
 * fluid (which includes Lumenwater), cancel the fall damage. Real water is unaffected (vanilla already cancels it).</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenFallEvents {

    private LumenFallEvents() {}

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        Entity entity = event.getEntity();
        // Feet or eye in a water-tagged fluid (covers landing in shallow or deep Lumenwater).
        boolean inWater = entity.level().getFluidState(entity.blockPosition()).is(FluidTags.WATER)
                || entity.level()
                        .getFluidState(BlockPos.containing(entity.getEyePosition()))
                        .is(FluidTags.WATER);
        if (inWater) {
            event.setCanceled(true);
        }
    }
}
