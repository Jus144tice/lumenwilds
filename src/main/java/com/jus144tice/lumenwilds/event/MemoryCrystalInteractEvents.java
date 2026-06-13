/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Right-click a Memory Crystal ({@link ModBlocks#MEMORY_CRYSTAL}) to read a fragment of the Lumenwrights'
 * memory (Phase 10c). The line is chosen deterministically from the crystal's position, so each crystal always
 * "remembers" the same thing — and some are broken/unreadable. The crystal is not consumed (it can be read
 * again, mined for a {@code memory_crystal_shard}).
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class MemoryCrystalInteractEvents {

    private MemoryCrystalInteractEvents() {}

    private static final String[] FRAGMENTS = {
        "A city of light beneath a moon that never slept.",
        "They grew their roads from stone and taught the roots to sing.",
        "The sky engines failed first.",
        "The lower vaults were sealed before the final Sporefall.",
        "We were not the first to shape the light. We will not be the last.",
        "The crystal hums, but the memory is incomplete.",
        "…static… the memory is broken, unreadable.",
        "When the conduits went dark, so did we.",
        // Liftshaft / mine lore (Phase 11c) — eerie fragments tied to the gravity shafts and deep extraction.
        "The shaft remembers weight, and teaches it to soften.",
        "Workers descended without rope, carried by the patience of the field.",
        "Ore rose in silence through the blue column.",
        "The lower seams brightened before the city dimmed.",
        "Gravity failed in the eastern shaft. The rescue never returned.",
        "The engines below still pull against the moon.",
    };

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!level.getBlockState(pos).is(ModBlocks.MEMORY_CRYSTAL.get())) {
            return;
        }
        if (!level.isClientSide) {
            int idx = Math.floorMod(pos.getX() * 31 + pos.getY() * 17 + pos.getZ() * 13, FRAGMENTS.length);
            event.getEntity()
                    .displayClientMessage(
                            Component.literal(FRAGMENTS[idx]).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC),
                            false);
        }
        event.getEntity().swing(event.getHand());
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }
}
