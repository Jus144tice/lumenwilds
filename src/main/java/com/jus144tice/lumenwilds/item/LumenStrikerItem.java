/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.item;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.portal.LumenPortalManager;
import com.jus144tice.lumenwilds.portal.LumenPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Lumen Striker — the portal igniter for The Lumenwilds.
 *
 * <p>Phase 1 behaviour: when right-clicking a block, detect whether it is the Lumenbound Stone frame
 * material and log a portal-activation attempt. It never crashes on invalid targets and returns a
 * sensible {@link InteractionResult}.</p>
 *
 * <p>IMPORTANT: the frame material is {@code lumenwilds:lumenbound_stone} — NOT vanilla lodestone.</p>
 *
 * <p>TODO (Phase 2): on a valid frame, validate the full rectangle and fill it with
 * {@code lumen_portal} blocks (see {@link LumenPortalManager} / {@link LumenPortalShape}); consume
 * durability/uses; play ignition sound + particles.</p>
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
            Lumenwilds.LOGGER.debug(
                    "[{}] Lumen Striker used on non-frame block {} at {} — ignored.",
                    Lumenwilds.MOD_ID,
                    clicked.getBlock().getName().getString(),
                    pos);
            return InteractionResult.PASS;
        }

        // Server-side only: log + delegate to the (placeholder) portal manager.
        if (!level.isClientSide) {
            Lumenwilds.LOGGER.info(
                    "[{}] Lumen Striker used on Lumenbound Stone at {} — portal activation attempted.",
                    Lumenwilds.MOD_ID,
                    pos);
            LumenPortalManager.tryActivatePortal(level, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
