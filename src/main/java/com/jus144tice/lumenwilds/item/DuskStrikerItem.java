/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.item;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.portal.DuskPortalManager;
import com.jus144tice.lumenwilds.portal.DuskPortalShape;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Dusk Striker — the portal igniter for <b>Dusk Portals</b> (Lumenwilds ↔ Nether), the Duskglass sibling of the
 * {@link LumenStrikerItem}. Each portal type has its own key: obsidian = fire, Lumenbound Stone = Lumen Striker,
 * Duskglass = <b>Dusk Striker</b> (flint &amp; steel no longer lights a Duskglass frame — see the deleted
 * {@code DuskPortalIgnitionEvents}). Forged from Lumenwilds materials (Emberglow + Duskglass + Luminite).
 *
 * <p>Right-click a {@code lumenwilds:duskglass} frame: seeds detection from the adjacent air and fills a valid
 * empty 2×3..21×21 frame with {@code dusk_portal} (see {@link DuskPortalManager}/{@link DuskPortalShape}).
 * Only ignites <b>inside the Lumenwilds or the Nether</b> — inert elsewhere. Ignition costs one durability.</p>
 */
public class DuskStrikerItem extends Item {

    public DuskStrikerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clicked = level.getBlockState(pos);

        if (!DuskPortalShape.isFrameBlock(clicked)) {
            return InteractionResult.PASS;
        }

        // Dusk portals only bridge the Lumenwilds and the Nether — inert (a no-op) in any other realm.
        if (level.dimension() != LumenDimensionConstants.LUMENWILDS_LEVEL && level.dimension() != Level.NETHER) {
            if (!level.isClientSide && context.getPlayer() != null) {
                context.getPlayer()
                        .displayClientMessage(
                                Component.translatable("lumenwilds.portal.dusk_wrong_realm")
                                        .withStyle(ChatFormatting.GRAY),
                                true);
            }
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos faceSeed = pos.relative(context.getClickedFace());
        boolean lit = tryIgnite(level, faceSeed) || tryIgnite(level, pos.above());

        if (lit) {
            Player player = context.getPlayer();
            if (player != null) {
                EquipmentSlot slot =
                        context.getHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                context.getItemInHand().hurtAndBreak(1, player, slot);
            }
            return InteractionResult.CONSUME;
        }

        Lumenwilds.LOGGER.info(
                "[{}] Dusk Striker used on Duskglass at {}, but no valid empty frame was found.",
                Lumenwilds.MOD_ID,
                pos);
        return InteractionResult.CONSUME;
    }

    private static boolean tryIgnite(Level level, BlockPos seed) {
        return level.getBlockState(seed).isAir() && DuskPortalManager.tryActivatePortal(level, seed);
    }
}
