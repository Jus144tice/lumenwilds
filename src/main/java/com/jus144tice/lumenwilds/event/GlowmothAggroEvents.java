/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.entity.Glowmoth;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Makes the Glowmoth a flower guardian (Phase 6h): when a player breaks one of the blooms the moth protects
 * (Moonblossom or any Stillbloom part), every Glowmoth within ~12 blocks turns hostile and targets the
 * culprit. Game-bus, server-side.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class GlowmothAggroEvents {

    private static final double AGGRO_RADIUS = 12.0;

    private GlowmothAggroEvents() {}

    @SubscribeEvent
    public static void onBlockBreak(final BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!isGuardedBloom(event.getState())) {
            return;
        }
        Player player = event.getPlayer();
        AABB area = new AABB(event.getPos()).inflate(AGGRO_RADIUS);
        for (Glowmoth moth : level.getEntitiesOfClass(Glowmoth.class, area)) {
            moth.setTarget(player);
        }
    }

    private static boolean isGuardedBloom(BlockState state) {
        return state.is(ModBlocks.MOONBLOSSOM.get())
                || state.is(ModBlocks.STILLBLOOM_CORE.get())
                || state.is(ModBlocks.STILLBLOOM_PETAL.get())
                || state.is(ModBlocks.STILLBLOOM_STEM.get());
    }
}
