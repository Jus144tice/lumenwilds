/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Lets a player collect <b>Lumen Nectar</b> from a Stillbloom with a glass bottle (Phase 8b): right-clicking
 * a Stillbloom Core or Petal with a {@code GLASS_BOTTLE} fills it into {@link ModItems#LUMEN_NECTAR}. The
 * bloom isn't consumed (renewable, like honey). Game-bus, server-authoritative.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class StillbloomInteractEvents {

    private StillbloomInteractEvents() {}

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        ItemStack held = event.getItemStack();
        if (!held.is(Items.GLASS_BOTTLE)) {
            return;
        }
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!state.is(ModBlocks.STILLBLOOM_CORE.get()) && !state.is(ModBlocks.STILLBLOOM_PETAL.get())) {
            return;
        }

        Level level = event.getLevel();
        Player player = event.getEntity();
        if (!level.isClientSide) {
            held.shrink(1);
            ItemStack nectar = new ItemStack(ModItems.LUMEN_NECTAR.get());
            if (!player.getInventory().add(nectar)) {
                player.drop(nectar, false);
            }
            level.playSound(null, event.getPos(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        player.swing(event.getHand());
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }
}
