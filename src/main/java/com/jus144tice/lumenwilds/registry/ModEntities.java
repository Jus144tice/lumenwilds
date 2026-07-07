/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

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
import com.jus144tice.lumenwilds.entity.SporeTrader;
import com.jus144tice.lumenwilds.entity.Sporeling;
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

    // Lantern Beetle — the small glowing flying ambience insect (Phase 6c). AMBIENT (like vanilla bats), NOT
    // creature (v1.4.12): it gets its own spawn cap, so the prolific, Moonwake-boosted, persistent flyers stop
    // saturating the shared CREATURE cap and starving the ground fauna (Grazer/Silkworm/Rootback). Ambient mobs
    // also despawn when far, so beetles stay dense near the player and refresh instead of piling up map-wide.
    // (Spawner entries moved creature->ambient in the biome JSONs; the ON_GROUND placement is unchanged.)
    public static final DeferredHolder<EntityType<?>, EntityType<LanternBeetle>> LANTERN_BEETLE =
            ENTITIES.register("lantern_beetle", () -> EntityType.Builder.of(LanternBeetle::new, MobCategory.AMBIENT)
                    .sized(0.5F, 0.4F)
                    .clientTrackingRange(8)
                    .build("lantern_beetle"));

    /** Sporeling — the small hostile fungal swarm mob with a death spore cloud (Phase 6d). */
    public static final DeferredHolder<EntityType<?>, EntityType<Sporeling>> SPORELING =
            ENTITIES.register("sporeling", () -> EntityType.Builder.of(Sporeling::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.7F)
                    .clientTrackingRange(8)
                    .build("sporeling"));

    /** Mirelurker — the Moonmire amphibious ambush predator (Phase 6e). */
    public static final DeferredHolder<EntityType<?>, EntityType<Mirelurker>> MIRELURKER =
            ENTITIES.register("mirelurker", () -> EntityType.Builder.of(Mirelurker::new, MobCategory.MONSTER)
                    .sized(0.7F, 0.7F)
                    .clientTrackingRange(8)
                    .build("mirelurker"));

    /** Lumen Fish — the native passive schooling swimmer of the glowing water (Phase 6f). */
    public static final DeferredHolder<EntityType<?>, EntityType<LumenFish>> LUMEN_FISH =
            ENTITIES.register("lumen_fish", () -> EntityType.Builder.of(LumenFish::new, MobCategory.WATER_AMBIENT)
                    .sized(0.5F, 0.4F)
                    .clientTrackingRange(4)
                    .build("lumen_fish"));

    /** Prismfin (v1.4.2) — the catchable tropical aquarium fish. */
    public static final DeferredHolder<EntityType<?>, EntityType<com.jus144tice.lumenwilds.entity.Prismfin>> PRISMFIN =
            ENTITIES.register("prismfin", () -> EntityType.Builder.of(
                            com.jus144tice.lumenwilds.entity.Prismfin::new, MobCategory.WATER_AMBIENT)
                    .sized(0.5F, 0.4F)
                    .clientTrackingRange(4)
                    .build("prismfin"));

    /** Sky Jelly — the floating, drifting air ambience mob (Phase 6g). */
    public static final DeferredHolder<EntityType<?>, EntityType<SkyJelly>> SKY_JELLY =
            ENTITIES.register("sky_jelly", () -> EntityType.Builder.of(SkyJelly::new, MobCategory.CREATURE)
                    .sized(1.2F, 1.2F)
                    .clientTrackingRange(10)
                    .build("sky_jelly"));

    /** Glowmoth — the neutral flying flower guardian (Phase 6h). */
    public static final DeferredHolder<EntityType<?>, EntityType<Glowmoth>> GLOWMOTH =
            ENTITIES.register("glowmoth", () -> EntityType.Builder.of(Glowmoth::new, MobCategory.CREATURE)
                    .sized(0.8F, 0.6F)
                    .clientTrackingRange(8)
                    .build("glowmoth"));

    /** Rootback — the MASSIVE slow neutral "living-feature" turtle (Phase 6i); nearly a roaming landmark. */
    public static final DeferredHolder<EntityType<?>, EntityType<Rootback>> ROOTBACK =
            ENTITIES.register("rootback", () -> EntityType.Builder.of(Rootback::new, MobCategory.CREATURE)
                    .sized(3.0F, 2.2F)
                    .clientTrackingRange(12)
                    .build("rootback"));

    /** Crag Wraith — the Glasspetal Crags aerial dive-attacker (Phase 6j). */
    public static final DeferredHolder<EntityType<?>, EntityType<CragWraith>> CRAG_WRAITH =
            ENTITIES.register("crag_wraith", () -> EntityType.Builder.of(CragWraith::new, MobCategory.MONSTER)
                    .sized(1.4F, 0.7F)
                    .clientTrackingRange(10)
                    .build("crag_wraith"));

    /** Echo Sentinel — the Vestige City ruin guardian: a floating construct with a ranged light pulse (Phase 10f). */
    public static final DeferredHolder<EntityType<?>, EntityType<EchoSentinel>> ECHO_SENTINEL =
            ENTITIES.register("echo_sentinel", () -> EntityType.Builder.of(EchoSentinel::new, MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(10)
                    .build("echo_sentinel"));

    /** Sporeman — the rare "fully grown Sporeling" wandering trader of the Sporefall Jungle (v1.2). */
    public static final DeferredHolder<EntityType<?>, EntityType<SporeTrader>> SPORE_TRADER =
            ENTITIES.register("spore_trader", () -> EntityType.Builder.of(SporeTrader::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.6F)
                    .clientTrackingRange(10)
                    .build("spore_trader"));

    /** Lumen Silkworm — the small glowing larva that drops Lumensilk (the dimension's wool source, v1.4.4). */
    public static final DeferredHolder<EntityType<?>, EntityType<com.jus144tice.lumenwilds.entity.LumenSilkworm>>
            LUMEN_SILKWORM = ENTITIES.register("lumen_silkworm", () -> EntityType.Builder.of(
                    com.jus144tice.lumenwilds.entity.LumenSilkworm::new, MobCategory.CREATURE)
            .sized(0.5F, 0.4F)
            .clientTrackingRange(8)
            .build("lumen_silkworm"));

    private ModEntities() {}
}
