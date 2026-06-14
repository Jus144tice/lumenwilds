/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.entity.CragWraith;
import com.jus144tice.lumenwilds.entity.EchoSentinel;
import com.jus144tice.lumenwilds.entity.Glowmoth;
import com.jus144tice.lumenwilds.entity.LanternBeetle;
import com.jus144tice.lumenwilds.entity.LumenFish;
import com.jus144tice.lumenwilds.entity.LumenGrazer;
import com.jus144tice.lumenwilds.entity.Mirelurker;
import com.jus144tice.lumenwilds.entity.Rootback;
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
        event.put(ModEntities.ROOTBACK.get(), Rootback.createAttributes().build());
        event.put(ModEntities.CRAG_WRAITH.get(), CragWraith.createAttributes().build());
        event.put(
                ModEntities.ECHO_SENTINEL.get(), EchoSentinel.createAttributes().build());
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
        // Sporeling: native jungle/cave swarm — light-AGNOSTIC (checkAnyLight, not the darkness rule), so the
        // dim-but-not-dark Sporefall Jungle actually teems with them day and night (they're ambient fauna, not
        // a night-only ambush mob like the Shade Stalker).
        event.register(
                ModEntities.SPORELING.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Mirelurker: native Moonmire amphibian — also light-agnostic so the glowing swamp is actually populated
        // (the dim swamp rarely hit the darkness threshold, so it was nearly empty before).
        event.register(
                ModEntities.MIRELURKER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
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
        // Rootback: a large ground creature.
        event.register(
                ModEntities.ROOTBACK.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Crag Wraith: hostile darkness rule; spawns on a crag ledge then takes to the air.
        event.register(
                ModEntities.CRAG_WRAITH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        // Echo Sentinel: a CONSTRUCT guardian — spawns regardless of light (checkAnyLight, not the darkness
        // rule), so the lit vault spawner + the crystal-lit Undercrown actually produce it.
        event.register(
                ModEntities.ECHO_SENTINEL.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
