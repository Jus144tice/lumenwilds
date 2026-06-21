/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Makes Moonloam farm like Dirt (v1.4 Phase F1). NeoForge 1.21.1 has no data map for hoe-tilling /
 * shovel-flattening, so we hook the game-bus {@link BlockEvent.BlockToolModificationEvent}:
 *
 * <ul>
 *   <li><b>Hoe</b> ({@code HOE_TILL}) on Moonloam or Lumen Grass (with air above) → {@code LUMEN_FARMLAND}.</li>
 *   <li><b>Shovel</b> ({@code SHOVEL_FLATTEN}) on Lumen Grass or Moonloam → {@code LUMEN_DIRT_PATH}.</li>
 * </ul>
 *
 * <p>The handler is pure (only {@code setFinalState}) so it's also correct for the event's {@code simulate}
 * pass. NOTE: for {@code HOE_TILL} we must do the air-above check ourselves — vanilla's air check lives inside
 * its hardcoded, vanilla-only branch and never sees modded source blocks.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenFarmingEvents {

    private LumenFarmingEvents() {}

    @SubscribeEvent
    public static void onToolModify(BlockEvent.BlockToolModificationEvent event) {
        ItemAbility ability = event.getItemAbility();
        UseOnContext ctx = event.getContext();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = event.getState();
        boolean airAbove = ctx.getClickedFace() != Direction.DOWN
                && level.getBlockState(pos.above()).isAir();

        if (ability == ItemAbilities.HOE_TILL && airAbove) {
            if (state.is(ModBlocks.MOONLOAM.get()) || state.is(ModBlocks.LUMEN_GRASS_BLOCK.get())) {
                event.setFinalState(ModBlocks.LUMEN_FARMLAND.get().defaultBlockState());
            }
        } else if (ability == ItemAbilities.SHOVEL_FLATTEN && airAbove) {
            if (state.is(ModBlocks.LUMEN_GRASS_BLOCK.get()) || state.is(ModBlocks.MOONLOAM.get())) {
                event.setFinalState(ModBlocks.LUMEN_DIRT_PATH.get().defaultBlockState());
            }
        }
    }
}
