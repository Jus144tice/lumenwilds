/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.portal.DuskPortalManager;
import com.jus144tice.lumenwilds.portal.DuskPortalShape;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Lights a <b>Dusk Portal</b> (Lumenwilds ↔ Nether) when a player right-clicks a Duskglass frame with flint &amp;
 * steel — "ignited like the obsidian portal." Only fires <b>inside the Lumenwilds or the Nether</b> (the two dims
 * a Dusk portal bridges); elsewhere flint &amp; steel behaves normally, so the frame simply won't light. On a
 * valid empty frame it fills the interior with {@code dusk_portal}, spends one flint-and-steel durability, and
 * cancels the default fire placement. No valid frame → not cancelled (flint &amp; steel does its usual thing).
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class DuskPortalIgnitionEvents {

    private DuskPortalIgnitionEvents() {}

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.FLINT_AND_STEEL)) {
            return;
        }
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!DuskPortalShape.isFrameBlock(level.getBlockState(pos))) {
            return;
        }
        ResourceKey<Level> dim = level.dimension();
        if (dim != LumenDimensionConstants.LUMENWILDS_LEVEL && dim != Level.NETHER) {
            return; // Dusk portals only bridge the Lumenwilds and the Nether
        }
        if (level.isClientSide) {
            return; // server does the real work; the client's flint-and-steel prediction is corrected by it
        }

        BlockPos faceSeed = pos.relative(event.getFace());
        boolean lit = tryIgnite(level, faceSeed) || tryIgnite(level, pos.above());
        if (!lit) {
            return; // no valid empty frame — let flint & steel behave normally
        }

        Player player = event.getEntity();
        EquipmentSlot slot =
                event.getHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        stack.hurtAndBreak(1, player, slot);
        level.playSound(
                null,
                pos,
                SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.BLOCKS,
                1.0F,
                level.getRandom().nextFloat() * 0.4F + 0.8F);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.CONSUME);
    }

    private static boolean tryIgnite(Level level, BlockPos seed) {
        return level.getBlockState(seed).isAir() && DuskPortalManager.tryActivatePortal(level, seed);
    }
}
