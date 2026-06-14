/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

/**
 * Registers the Glowwood sign blocks with the vanilla sign block entities. Modded signs reuse vanilla
 * {@code SignBlockEntity}/{@code HangingSignBlockEntity} (whose type is hardcoded to {@code SIGN}/
 * {@code HANGING_SIGN}), so rather than creating a new {@link BlockEntityType} we add our blocks to the
 * vanilla ones via NeoForge's {@link BlockEntityTypeAddBlocksEvent} (a mod-bus event).
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class ModBlockEntityTypes {

    private ModBlockEntityTypes() {}

    @SubscribeEvent
    public static void addSignBlocks(final BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, ModBlocks.GLOWWOOD_SIGN.get(), ModBlocks.GLOWWOOD_WALL_SIGN.get());
        event.modify(
                BlockEntityType.HANGING_SIGN,
                ModBlocks.GLOWWOOD_HANGING_SIGN.get(),
                ModBlocks.GLOWWOOD_WALL_HANGING_SIGN.get());
    }
}
