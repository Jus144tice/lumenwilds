/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.item;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.portal.LumenPortalManager;
import com.jus144tice.lumenwilds.portal.LumenPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Lumen Striker — the portal igniter for The Lumenwilds.
 *
 * <p>Right-click a {@code lumenwilds:lumenbound_stone} frame: the striker seeds frame detection from the
 * adjacent air, and on a valid empty 2×3..21×21 frame fills the interior with {@code lumen_portal} (see
 * {@link LumenPortalManager}/{@link LumenPortalShape}). Successful ignition costs one durability point.</p>
 *
 * <p>IMPORTANT: the frame material is {@code lumenwilds:lumenbound_stone} — NEVER vanilla lodestone.</p>
 */
public class LumenStrikerItem extends Item {

    public LumenStrikerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clicked = level.getBlockState(pos);

        if (!LumenPortalShape.isFrameBlock(clicked)) {
            // Not a frame block — do nothing (and don't swing as if something happened).
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            // Let the client swing; the server does the real work below.
            return InteractionResult.SUCCESS;
        }

        // Seed detection from the air next to the clicked face, falling back to the block above.
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
                "[{}] Lumen Striker used on Lumenbound Stone at {}, but no valid empty frame was found.",
                Lumenwilds.MOD_ID,
                pos);
        return InteractionResult.CONSUME;
    }

    private static boolean tryIgnite(Level level, BlockPos seed) {
        return level.getBlockState(seed).isAir() && LumenPortalManager.tryActivatePortal(level, seed);
    }
}
