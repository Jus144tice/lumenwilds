/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Dormant Light Engine (Phase 10e.2) — the dead centrepiece of a Vestige City's power. Right-click it with a
 * {@code resonance_core_fragment} to <b>restore</b> it: the fragment is consumed and the block becomes an
 * {@link ActiveLightEngineBlock} (a Resonance Core), waking the surrounding conduit network and its doors.
 * Until then it just sits, barely glowing — the strongest hint the city was once technologically alive.
 */
public class DormantLightEngineBlock extends Block {

    public DormantLightEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (!stack.is(ModItems.RESONANCE_CORE_FRAGMENT.get())) {
            hint(player);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!level.isClientSide) {
            level.setBlockAndUpdate(pos, ModBlocks.ACTIVE_LIGHT_ENGINE.get().defaultBlockState());
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.4F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        hint(player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** Tells the player what the dead engine needs (a Resonance Core Fragment) — a gentle discovery nudge. */
    private static void hint(Player player) {
        player.displayClientMessage(
                Component.literal("The Light Engine is dormant. It hungers for a Resonance Core Fragment.")
                        .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC),
                true);
    }
}
