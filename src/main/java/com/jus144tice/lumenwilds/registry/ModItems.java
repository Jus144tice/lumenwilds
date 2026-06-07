/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.item.LumenStrikerItem;
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

    // --- Block items ----------------------------------------------------------------------------
    // Auto-register a simple BlockItem for every registered block EXCEPT the portal interior (which is
    // placed by portal mechanics, never held). Runs after the standalone items above so the Lumen
    // Striker stays first in the creative tab. New blocks get an item for free — no edits here.
    static {
        for (var block : ModBlocks.BLOCKS.getEntries()) {
            if (block == ModBlocks.LUMEN_PORTAL) {
                continue;
            }
            ITEMS.registerSimpleBlockItem(block);
        }
    }
}
