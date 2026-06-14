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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Lets a player <b>harvest a Glowberry Bush by right-clicking it</b> (Phase 9 harvestables) — the intuitive
 * "pick the berries" action, since the bush evokes a sweet-berry bush. Pops 1–2 {@link ModItems#GLOWBERRY} and
 * removes the bush (breaking it by hand drops the same via its loot table; this just makes right-click work too).
 * Game-bus, server-authoritative.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class GlowberryInteractEvents {

    private GlowberryInteractEvents() {}

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (!level.getBlockState(event.getPos()).is(ModBlocks.GLOWBERRY_BUSH.get())) {
            return;
        }
        Player player = event.getEntity();
        if (!level.isClientSide) {
            int count = 1 + level.getRandom().nextInt(2);
            Block.popResource(level, event.getPos(), new ItemStack(ModItems.GLOWBERRY.get(), count));
            level.playSound(
                    null,
                    event.getPos(),
                    SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.BLOCKS,
                    1.0F,
                    0.9F + level.getRandom().nextFloat() * 0.2F);
            level.setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
        }
        player.swing(event.getHand());
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }
}
