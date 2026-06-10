/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.entity.LumenGrazer;
import com.jus144tice.lumenwilds.registry.ModEntities;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/**
 * Mod-bus entity lifecycle wiring (Phase 6): builds each native mob's {@link net.minecraft.world.entity.ai.attributes.AttributeSupplier}
 * and registers its spawn placement. The per-biome spawn lists (which biomes / how many) live in the biome
 * JSON {@code spawners}; this just declares <em>where on the ground</em> a type may spawn.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEntityEvents {

    private ModEntityEvents() {}

    @SubscribeEvent
    public static void onAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.LUMEN_GRAZER.get(), LumenGrazer.createAttributes().build());
    }

    @SubscribeEvent
    public static void onRegisterSpawnPlacements(final RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.LUMEN_GRAZER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
