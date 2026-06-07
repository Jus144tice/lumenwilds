/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.item.LumenStrikerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All items added by The Lumenwilds: the standalone items plus the {@link net.minecraft.world.item.BlockItem}s
 * for the blocks registered in {@link ModBlocks}.
 *
 * <p>The portal interior block ({@link ModBlocks#LUMEN_PORTAL}) intentionally has no block item — it
 * is placed by portal mechanics, not by hand.</p>
 */
public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Lumenwilds.MOD_ID);

    private ModItems() {}

    // --- Standalone items -----------------------------------------------------------------------

    /**
     * Lumen Striker — ignites a Lumenbound Stone portal frame. See {@link LumenStrikerItem}. A durable,
     * reusable mystical tool (not single-use); each ignition costs one point of durability.
     */
    public static final DeferredItem<Item> LUMEN_STRIKER = ITEMS.registerItem(
            "lumen_striker",
            LumenStrikerItem::new,
            new Item.Properties().stacksTo(1).durability(64));

    public static final DeferredItem<Item> LUMEN_CRYSTAL_SHARD = ITEMS.registerSimpleItem("lumen_crystal_shard");
    public static final DeferredItem<Item> GLOW_POLLEN = ITEMS.registerSimpleItem("glow_pollen");
    public static final DeferredItem<Item> LIVING_FIBER = ITEMS.registerSimpleItem("living_fiber");

    // Foods are plain items for now; nutrition/effects come later (TODO: FoodProperties).
    public static final DeferredItem<Item> LUMEN_FRUIT = ITEMS.registerSimpleItem("lumen_fruit");
    public static final DeferredItem<Item> LUMEN_NECTAR = ITEMS.registerSimpleItem("lumen_nectar");
    public static final DeferredItem<Item> AIR_GEL = ITEMS.registerSimpleItem("air_gel");

    // --- Block items (one per block, except the portal interior) ---------------------------------

    public static final DeferredItem<BlockItem> LUMENBOUND_STONE =
            ITEMS.registerSimpleBlockItem(ModBlocks.LUMENBOUND_STONE);
    public static final DeferredItem<BlockItem> MOONLOAM = ITEMS.registerSimpleBlockItem(ModBlocks.MOONLOAM);
    public static final DeferredItem<BlockItem> LUMEN_GRASS_BLOCK =
            ITEMS.registerSimpleBlockItem(ModBlocks.LUMEN_GRASS_BLOCK);
    public static final DeferredItem<BlockItem> MOONSTONE = ITEMS.registerSimpleBlockItem(ModBlocks.MOONSTONE);
    public static final DeferredItem<BlockItem> COBBLED_MOONSTONE =
            ITEMS.registerSimpleBlockItem(ModBlocks.COBBLED_MOONSTONE);
    public static final DeferredItem<BlockItem> GLOWWOOD_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_LOG);
    public static final DeferredItem<BlockItem> GLOWWOOD_PLANKS =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_PLANKS);
    public static final DeferredItem<BlockItem> GLOWROOT_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.GLOWROOT_LOG);
    public static final DeferredItem<BlockItem> GLOWVINE = ITEMS.registerSimpleBlockItem(ModBlocks.GLOWVINE);
    public static final DeferredItem<BlockItem> MOONBLOSSOM = ITEMS.registerSimpleBlockItem(ModBlocks.MOONBLOSSOM);
    public static final DeferredItem<BlockItem> LUMENBULB = ITEMS.registerSimpleBlockItem(ModBlocks.LUMENBULB);
    public static final DeferredItem<BlockItem> LUMEN_CRYSTAL_BLOCK =
            ITEMS.registerSimpleBlockItem(ModBlocks.LUMEN_CRYSTAL_BLOCK);

    // --- Glowwood building set (Phase 4) --------------------------------------------------------
    public static final DeferredItem<BlockItem> GLOWWOOD_WOOD = ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_WOOD);
    public static final DeferredItem<BlockItem> STRIPPED_GLOWWOOD_LOG =
            ITEMS.registerSimpleBlockItem(ModBlocks.STRIPPED_GLOWWOOD_LOG);
    public static final DeferredItem<BlockItem> STRIPPED_GLOWWOOD_WOOD =
            ITEMS.registerSimpleBlockItem(ModBlocks.STRIPPED_GLOWWOOD_WOOD);
    public static final DeferredItem<BlockItem> GLOWWOOD_LEAVES =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_LEAVES);
    public static final DeferredItem<BlockItem> GLOWWOOD_STAIRS =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_STAIRS);
    public static final DeferredItem<BlockItem> GLOWWOOD_SLAB = ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_SLAB);
    public static final DeferredItem<BlockItem> GLOWWOOD_FENCE =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_FENCE);
    public static final DeferredItem<BlockItem> GLOWWOOD_FENCE_GATE =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_FENCE_GATE);
    public static final DeferredItem<BlockItem> GLOWWOOD_DOOR = ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_DOOR);
    public static final DeferredItem<BlockItem> GLOWWOOD_TRAPDOOR =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_TRAPDOOR);
    public static final DeferredItem<BlockItem> GLOWWOOD_BUTTON =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_BUTTON);
    public static final DeferredItem<BlockItem> GLOWWOOD_PRESSURE_PLATE =
            ITEMS.registerSimpleBlockItem(ModBlocks.GLOWWOOD_PRESSURE_PLATE);
}
