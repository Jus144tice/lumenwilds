/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.entity.Glowmoth;
import com.jus144tice.lumenwilds.entity.LanternBeetle;
import com.jus144tice.lumenwilds.entity.LumenFish;
import com.jus144tice.lumenwilds.entity.LumenGrazer;
import com.jus144tice.lumenwilds.entity.Mirelurker;
import com.jus144tice.lumenwilds.entity.ShadeStalker;
import com.jus144tice.lumenwilds.entity.SkyJelly;
import com.jus144tice.lumenwilds.entity.Sporeling;
import com.jus144tice.lumenwilds.registry.ModEntities;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
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
        event.put(
                ModEntities.SHADE_STALKER.get(), ShadeStalker.createAttributes().build());
        event.put(
                ModEntities.LANTERN_BEETLE.get(),
                LanternBeetle.createAttributes().build());
        event.put(ModEntities.SPORELING.get(), Sporeling.createAttributes().build());
        event.put(ModEntities.MIRELURKER.get(), Mirelurker.createAttributes().build());
        event.put(ModEntities.LUMEN_FISH.get(), LumenFish.createAttributes().build());
        event.put(ModEntities.SKY_JELLY.get(), SkyJelly.createAttributes().build());
        event.put(ModEntities.GLOWMOTH.get(), Glowmoth.createAttributes().build());
    }

    @SubscribeEvent
    public static void onRegisterSpawnPlacements(final RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.LUMEN_GRAZER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Shade Stalker: standard hostile darkness rule (spawns in low light), so living light keeps it away.
        event.register(
                ModEntities.SHADE_STALKER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Lantern Beetle: spawns on the ground (like bees/parrots), then takes flight.
        event.register(
                ModEntities.LANTERN_BEETLE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Sporeling: hostile darkness rule (jungle/cave swarm).
        event.register(
                ModEntities.SPORELING.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Mirelurker: amphibious ambusher; spawns on the Moonmire floor (incl. under shallow water) in the dark.
        event.register(
                ModEntities.MIRELURKER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Lumen Fish: in the water, like vanilla cod/salmon.
        event.register(
                ModEntities.LUMEN_FISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Sky Jelly: spawns on the surface (then drifts up into the air).
        event.register(
                ModEntities.SKY_JELLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Glowmoth: spawns on the surface (then flies up to circle flowers/lights).
        event.register(
                ModEntities.GLOWMOTH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
