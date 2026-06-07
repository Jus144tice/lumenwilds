/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.portal.LumenPortalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All blocks added by The Lumenwilds.
 *
 * <p>Phase 1 placeholders. Every block is registered cleanly so it can be fleshed out later (custom
 * models, block entities, plant/vine behaviour, etc.). Block <em>items</em> are registered separately
 * in {@link ModItems} so the item layer stays in one place.</p>
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Lumenwilds.MOD_ID);

    private ModBlocks() {}

    // --- Portal & progression -------------------------------------------------------------------

    /**
     * Lumenbound Stone — the REQUIRED portal frame block (replaces the earlier vanilla-lodestone
     * idea, which is too expensive for this progression point). Visually: stone infused with
     * amethyst resonance and Nether light. Sturdy, pickaxe-mined, stone-like. No special behaviour
     * yet — the {@link com.jus144tice.lumenwilds.item.LumenStrikerItem} keys off this block.
     */
    public static final DeferredBlock<Block> LUMENBOUND_STONE = BLOCKS.registerSimpleBlock(
            "lumenbound_stone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.0F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    /**
     * Lumen Portal — the portal interior block. Non-solid, no collision, glows. Placeholder: it does
     * not yet teleport (see {@link com.jus144tice.lumenwilds.portal.LumenPortalBlock}).
     */
    public static final DeferredBlock<LumenPortalBlock> LUMEN_PORTAL = BLOCKS.registerBlock(
            "lumen_portal",
            LumenPortalBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .noLootTable()
                    .strength(-1.0F)
                    .lightLevel(state -> 11)
                    .pushReaction(PushReaction.BLOCK));

    // --- Terrain & world blocks -----------------------------------------------------------------

    public static final DeferredBlock<Block> MOONLOAM = BLOCKS.registerSimpleBlock(
            "moonloam",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(0.6F)
                    .sound(SoundType.GRAVEL));

    public static final DeferredBlock<Block> LUMEN_GRASS_BLOCK = BLOCKS.registerSimpleBlock(
            "lumen_grass_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(0.6F)
                    .sound(SoundType.GRASS));

    public static final DeferredBlock<Block> MOONSTONE = BLOCKS.registerSimpleBlock(
            "moonstone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    public static final DeferredBlock<Block> COBBLED_MOONSTONE = BLOCKS.registerSimpleBlock(
            "cobbled_moonstone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    // --- Wood & plants --------------------------------------------------------------------------

    public static final DeferredBlock<Block> GLOWWOOD_LOG = BLOCKS.registerSimpleBlock(
            "glowwood_log",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.WOOD));

    public static final DeferredBlock<Block> GLOWWOOD_PLANKS = BLOCKS.registerSimpleBlock(
            "glowwood_planks",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD));

    public static final DeferredBlock<Block> GLOWROOT_LOG = BLOCKS.registerSimpleBlock(
            "glowroot_log",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD));

    // TODO: glowvine should become a climbable/decay vine block (LeavesBlock/GrowingPlantBlock-like).
    public static final DeferredBlock<Block> GLOWVINE = BLOCKS.registerSimpleBlock(
            "glowvine",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.2F)
                    .lightLevel(state -> 7)
                    .sound(SoundType.VINE));

    // TODO: moonblossom should become a flower/BushBlock with proper placement rules.
    public static final DeferredBlock<Block> MOONBLOSSOM = BLOCKS.registerSimpleBlock(
            "moonblossom",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .instabreak()
                    .lightLevel(state -> 9)
                    .noCollission()
                    .sound(SoundType.GRASS));

    /** Lumenbulb — a native living light source. Placeholder full cube that emits max light. */
    public static final DeferredBlock<Block> LUMENBULB = BLOCKS.registerSimpleBlock(
            "lumenbulb",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.3F)
                    .lightLevel(state -> 15)
                    .sound(SoundType.GLASS));

    // --- Resource / storage ---------------------------------------------------------------------

    public static final DeferredBlock<Block> LUMEN_CRYSTAL_BLOCK = BLOCKS.registerSimpleBlock(
            "lumen_crystal_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 6)
                    .sound(SoundType.AMETHYST));
}
