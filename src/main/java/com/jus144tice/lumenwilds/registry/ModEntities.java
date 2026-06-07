/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Entity types added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO (Phase 5): native fauna — bioluminescent grazers, light-feeding predators, floating
 * "lantern" drifters, etc. Each will need an {@code EntityType}, a renderer, and attributes
 * registered via {@code EntityAttributeCreationEvent}.</p>
 */
public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Lumenwilds.MOD_ID);

    private ModEntities() {}
}
