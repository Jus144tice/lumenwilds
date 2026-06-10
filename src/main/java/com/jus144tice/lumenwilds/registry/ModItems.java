/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.item.LumenStrikerItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
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

    /** Lumenwater bucket (Phase 5e) — picks up/places the {@link ModFluids#LUMENWATER} source. */
    public static final DeferredItem<BucketItem> LUMENWATER_BUCKET = ITEMS.registerItem(
            "lumenwater_bucket",
            props -> new BucketItem(ModFluids.LUMENWATER.get(), props),
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));

    // --- Mob drops & spawn eggs (Phase 6) -------------------------------------------------------

    /** Lumen Grazer drops (6a): raw/cooked meat (foods), hide (leather-like), rare glow sinew. */
    public static final DeferredItem<Item> RAW_GRAZER_MEAT = ITEMS.registerItem(
            "raw_grazer_meat",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationModifier(0.3F)
                            .build()));

    public static final DeferredItem<Item> COOKED_GRAZER_MEAT = ITEMS.registerItem(
            "cooked_grazer_meat",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(0.8F)
                            .build()));

    public static final DeferredItem<Item> GRAZER_HIDE = ITEMS.registerSimpleItem("grazer_hide");
    public static final DeferredItem<Item> GLOW_SINEW = ITEMS.registerSimpleItem("glow_sinew");

    /** Lumen Grazer spawn egg — NeoForge deferred egg (the EntityType isn't built when the item registers). */
    public static final DeferredItem<DeferredSpawnEggItem> LUMEN_GRAZER_SPAWN_EGG = ITEMS.registerItem(
            "lumen_grazer_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.LUMEN_GRAZER, 0x3A5A6E, 0x8FE0C8, props),
            new Item.Properties());

    /** Shade Stalker drops (6b): claw, dark hide, rare echo dust. */
    public static final DeferredItem<Item> SHADE_CLAW = ITEMS.registerSimpleItem("shade_claw");

    public static final DeferredItem<Item> DARK_HIDE = ITEMS.registerSimpleItem("dark_hide");
    public static final DeferredItem<Item> ECHO_DUST = ITEMS.registerSimpleItem("echo_dust");

    public static final DeferredItem<DeferredSpawnEggItem> SHADE_STALKER_SPAWN_EGG = ITEMS.registerItem(
            "shade_stalker_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.SHADE_STALKER, 0x14121C, 0x2FD0C0, props),
            new Item.Properties());

    /**
     * Lantern Beetle products (6c): the spawn egg. The Bottled Lantern Beetle is a BLOCK
     * ({@link ModBlocks#BOTTLED_LANTERN_BEETLE}) — a placeable glowing lamp — and its {@code BlockItem} is
     * auto-registered by the loop below, so there is no standalone item here.
     */
    public static final DeferredItem<DeferredSpawnEggItem> LANTERN_BEETLE_SPAWN_EGG = ITEMS.registerItem(
            "lantern_beetle_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.LANTERN_BEETLE, 0x2A3A30, 0xCFE860, props),
            new Item.Properties());

    // --- Boats (vanilla Boat/ChestBoat with the Glowwood Boat.Type from ModBoatTypes) ------------
    public static final DeferredItem<BoatItem> GLOWWOOD_BOAT = ITEMS.registerItem(
            "glowwood_boat",
            props -> new BoatItem(false, ModBoatTypes.glowwood(), props),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<BoatItem> GLOWWOOD_CHEST_BOAT = ITEMS.registerItem(
            "glowwood_chest_boat",
            props -> new BoatItem(true, ModBoatTypes.glowwood(), props),
            new Item.Properties().stacksTo(1));

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
            if (block == ModBlocks.LUMEN_PORTAL
                    || block == ModBlocks.LUMENWATER_BLOCK
                    || block.getId().getPath().contains("sign")) {
                continue;
            }
            ITEMS.registerSimpleBlockItem(block);
        }
    }
}
