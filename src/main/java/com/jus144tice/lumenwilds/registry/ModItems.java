/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.item.LumenStrikerItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
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

    // --- Sign items (one item per sign pair; the wall variants share it) -------------------------
    public static final DeferredItem<SignItem> GLOWWOOD_SIGN = ITEMS.registerItem(
            "glowwood_sign",
            props -> new SignItem(props, ModBlocks.GLOWWOOD_SIGN.get(), ModBlocks.GLOWWOOD_WALL_SIGN.get()),
            new Item.Properties().stacksTo(16));

    public static final DeferredItem<HangingSignItem> GLOWWOOD_HANGING_SIGN = ITEMS.registerItem(
            "glowwood_hanging_sign",
            props -> new HangingSignItem(
                    ModBlocks.GLOWWOOD_HANGING_SIGN.get(), ModBlocks.GLOWWOOD_WALL_HANGING_SIGN.get(), props),
            new Item.Properties().stacksTo(16));

    // --- Block items ----------------------------------------------------------------------------
    // Auto-register a simple BlockItem for every registered block EXCEPT the portal interior (placed by
    // portal mechanics) and the sign blocks (handled above as SignItem/HangingSignItem; wall variants get
    // no item). Runs after the standalone + sign items so the Lumen Striker stays first in the tab. New
    // blocks get an item for free — no edits here.
    static {
        for (var block : ModBlocks.BLOCKS.getEntries()) {
            if (block == ModBlocks.LUMEN_PORTAL || block.getId().getPath().contains("sign")) {
                continue;
            }
            ITEMS.registerSimpleBlockItem(block);
        }
    }
}
