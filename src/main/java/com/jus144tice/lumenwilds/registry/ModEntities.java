/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.entity.LumenGrazer;
import com.jus144tice.lumenwilds.entity.ShadeStalker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Entity types added by The Lumenwilds (Phase 6 — the native fauna). Each entity also needs: attributes
 * (registered in {@code event.ModEntityEvents#onAttributeCreation}), a spawn placement (same class), a
 * client renderer ({@code client.LumenwildsClient}), a loot table ({@code loot_table/entities/}), and
 * biome {@code spawners} entries.
 */
public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Lumenwilds.MOD_ID);

    /** Lumen Grazer — the peaceful herd herbivore (Phase 6a). */
    public static final DeferredHolder<EntityType<?>, EntityType<LumenGrazer>> LUMEN_GRAZER =
            ENTITIES.register("lumen_grazer", () -> EntityType.Builder.of(LumenGrazer::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.4F)
                    .clientTrackingRange(10)
                    .build("lumen_grazer"));

    /** Shade Stalker — the main hostile surface predator (Phase 6b). */
    public static final DeferredHolder<EntityType<?>, EntityType<ShadeStalker>> SHADE_STALKER =
            ENTITIES.register("shade_stalker", () -> EntityType.Builder.of(ShadeStalker::new, MobCategory.MONSTER)
                    .sized(0.9F, 0.8F)
                    .clientTrackingRange(8)
                    .build("shade_stalker"));

    private ModEntities() {}
}
