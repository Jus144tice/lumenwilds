/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.block.LumenAnchorBlockEntity;
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

    private ModBlockEntities() {}
}
