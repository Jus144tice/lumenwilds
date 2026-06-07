/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.effects.LowGravityHandler;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Dimension-transition handlers — the seam where "you are now in the Lumenwilds" behaviour attaches
 * (low gravity, ambient effects, etc.). Registered automatically via {@link EventBusSubscriber}.
 *
 * <p>Phase 1: forwards dimension changes to {@link LowGravityHandler}, which currently only logs.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class PlayerDimensionEvents {

    private PlayerDimensionEvents() {}

    @SubscribeEvent
    public static void onPlayerChangedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
        Lumenwilds.LOGGER.debug(
                "[{}] {} changed dimension {} -> {}.",
                Lumenwilds.MOD_ID,
                event.getEntity().getName().getString(),
                event.getFrom().location(),
                event.getTo().location());

        // Show the calm teal transition message when crossing the Lumenwilds boundary.
        Player player = event.getEntity();
        boolean enteringLumenwilds = event.getTo().equals(LumenDimensionConstants.LUMENWILDS_LEVEL);
        boolean leavingLumenwilds = event.getFrom().equals(LumenDimensionConstants.LUMENWILDS_LEVEL);
        if (enteringLumenwilds) {
            player.displayClientMessage(Component.translatable("lumenwilds.portal.entering"), true);
        } else if (leavingLumenwilds) {
            player.displayClientMessage(Component.translatable("lumenwilds.portal.leaving"), true);
        }

        // Apply/remove dimension-specific effects (low gravity is a Phase 3 TODO inside the handler).
        LowGravityHandler.onChangedDimension(player);
    }
}
