/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.mixin;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Restores the fish-strike animation in Lumenwater (v1.1.3). Vanilla {@code FishingHook#catchingFish}
 * hardcodes its approaching-bubble trail and splash particles to {@code Blocks.WATER}, so over Lumenwater
 * (a different block) the lure looks "dead" — no bubbles racing toward the bobber. This redirects those
 * {@code BlockState#is(Block)} checks so they also accept Lumenwater (only when the check was for water);
 * every other {@code is(...)} call is unaffected.
 */
@Mixin(FishingHook.class)
public class FishingHookMixin {

    @Redirect(
            method = "catchingFish",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean lumenwilds$waterParticlesInLumenwater(BlockState state, Block block) {
        return state.is(block) || (block == Blocks.WATER && state.is(ModBlocks.LUMENWATER_BLOCK.get()));
    }
}
