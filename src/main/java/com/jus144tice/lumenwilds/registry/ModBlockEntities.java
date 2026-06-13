/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.block.LumenAnchorBlockEntity;
import com.jus144tice.lumenwilds.block.LumenFieldProjectorBlockEntity;
import com.jus144tice.lumenwilds.block.ResonanceCoreBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block entity types added by The Lumenwilds. {@link #LUMEN_ANCHOR} is the portal-link anchor (Phase 8c).
 */
public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Lumenwilds.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LumenAnchorBlockEntity>> LUMEN_ANCHOR =
            BLOCK_ENTITIES.register("lumen_anchor", () -> BlockEntityType.Builder.of(
                            LumenAnchorBlockEntity::new, ModBlocks.LUMEN_ANCHOR.get())
                    .build(null));

    /**
     * Resonance Core (10e) — ticks the conduit power network (see {@code block.ResonanceNetwork}). Shared by
     * the hand-built {@code RESONANCE_CORE} and the restored {@code ACTIVE_LIGHT_ENGINE} (also a core).
     */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResonanceCoreBlockEntity>> RESONANCE_CORE =
            BLOCK_ENTITIES.register("resonance_core", () -> BlockEntityType.Builder.of(
                            ResonanceCoreBlockEntity::new,
                            ModBlocks.RESONANCE_CORE.get(),
                            ModBlocks.ACTIVE_LIGHT_ENGINE.get())
                    .build(null));

    /**
     * Lumen Field Projector (Phase 11b) — drives a liftshaft's gravity column. The 3rd block entity type.
     */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LumenFieldProjectorBlockEntity>>
            LUMEN_FIELD_PROJECTOR = BLOCK_ENTITIES.register("lumen_field_projector", () -> BlockEntityType.Builder.of(
                    LumenFieldProjectorBlockEntity::new, ModBlocks.LUMEN_FIELD_PROJECTOR.get())
            .build(null));

    private ModBlockEntities() {}
}
