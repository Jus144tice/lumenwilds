/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModItems;
import com.jus144tice.lumenwilds.registry.ModPotions;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

/**
 * Brewing mixes for the Lumenwilds potions (Phase 8h). Each is an <b>awkward potion + a native ingredient</b>:
 * Air Gel → Lightfoot, Glow Pollen → Glowmarked, Spore Sac → Sporeblind, Living Fiber → Rooted. Mod-bus.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class ModBrewing {

    private ModBrewing() {}

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(final RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ModItems.AIR_GEL.get(), ModPotions.LIGHTFOOT);
        builder.addMix(Potions.AWKWARD, ModItems.GLOW_POLLEN.get(), ModPotions.GLOWMARKED);
        builder.addMix(Potions.AWKWARD, ModItems.SPORE_SAC.get(), ModPotions.SPOREBLIND);
        builder.addMix(Potions.AWKWARD, ModItems.LIVING_FIBER.get(), ModPotions.ROOTED);
    }
}
