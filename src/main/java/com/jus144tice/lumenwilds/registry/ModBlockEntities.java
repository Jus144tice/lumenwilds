/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block entity types added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO: candidate block entities — a portal controller/anchor, a growing light-source bulb, a
 * crystal resonator. Register with {@code BlockEntityType.Builder.of(factory, blocks...)}.</p>
 */
public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Lumenwilds.MOD_ID);

    private ModBlockEntities() {}
}
