/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.item.LumenStrikerItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.material.Fluids;
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

    // Luminite chain (Phase 10a) — the Lumenwrights' structural metal. Ore → raw_luminite → luminite_ingot;
    // ingot + lumen crystal shard + glow pollen craft Glowbrick (the Vestige City material). See ancient_cities.txt.
    public static final DeferredItem<Item> RAW_LUMINITE = ITEMS.registerSimpleItem("raw_luminite");
    public static final DeferredItem<Item> LUMINITE_INGOT = ITEMS.registerSimpleItem("luminite_ingot");

    // Mining-overhaul materials (v1.3 Phase C). Emberglow = furnace fuel (the dimension's coal analog; burn
    // time wired in event.ModFuels); Pale Opal = a decorative/trade gem; Raw Resonite → smelt to Resonite
    // Ingot → the Phase-D gear tier. Dropped by their ores (ModLootTableProvider#createOreDrop, by name).
    public static final DeferredItem<Item> EMBERGLOW = ITEMS.registerSimpleItem("emberglow");
    public static final DeferredItem<Item> PALE_OPAL = ITEMS.registerSimpleItem("pale_opal");
    public static final DeferredItem<Item> RAW_RESONITE = ITEMS.registerSimpleItem("raw_resonite");
    public static final DeferredItem<Item> RESONITE_INGOT = ITEMS.registerSimpleItem("resonite_ingot");

    // Lore items (Phase 10c) — recovered from the Vestige Cities. Memory crystal shards (a broken piece of
    // ancient data storage; future crafting/repair) + six Ancient Glyph Tablets (right-click to read a fragment
    // of the Lumenwrights' story; see GlyphTabletItem). The lore lines are deliberately short and mysterious.
    public static final DeferredItem<Item> MEMORY_CRYSTAL_SHARD = ITEMS.registerSimpleItem("memory_crystal_shard");

    // Resonance tech component (Phase 10e) — a broken piece of an ancient machine core. Loot (reliquary/
    // engineer's cache/Echo Sentinel) + crafts the functional Resonance Core; future gravity/projector tech.
    public static final DeferredItem<Item> RESONANCE_CORE_FRAGMENT =
            ITEMS.registerSimpleItem("resonance_core_fragment");

    /** Gravity Lens Fragment (10e.2) — from a cracked lens / loot; crafts gravity tech (the future projector). */
    public static final DeferredItem<Item> GRAVITY_LENS_FRAGMENT = ITEMS.registerSimpleItem("gravity_lens_fragment");

    public static final DeferredItem<Item> GLYPH_TABLET_SKY = ITEMS.registerItem(
            "glyph_tablet_sky",
            props -> new com.jus144tice.lumenwilds.item.GlyphTabletItem(props, "The sky engines failed first."),
            new Item.Properties());
    public static final DeferredItem<Item> GLYPH_TABLET_ROOTS = ITEMS.registerItem(
            "glyph_tablet_roots",
            props -> new com.jus144tice.lumenwilds.item.GlyphTabletItem(
                    props, "They grew their roads from stone and taught the roots to sing."),
            new Item.Properties());
    public static final DeferredItem<Item> GLYPH_TABLET_LIGHT = ITEMS.registerItem(
            "glyph_tablet_light",
            props -> new com.jus144tice.lumenwilds.item.GlyphTabletItem(
                    props, "A city of light beneath a moon that never slept."),
            new Item.Properties());
    public static final DeferredItem<Item> GLYPH_TABLET_FALL = ITEMS.registerItem(
            "glyph_tablet_fall",
            props -> new com.jus144tice.lumenwilds.item.GlyphTabletItem(
                    props, "The lower vaults were sealed before the final Sporefall."),
            new Item.Properties());
    public static final DeferredItem<Item> GLYPH_TABLET_SILENCE = ITEMS.registerItem(
            "glyph_tablet_silence",
            props -> new com.jus144tice.lumenwilds.item.GlyphTabletItem(
                    props, "When the conduits went dark, so did we."),
            new Item.Properties());
    public static final DeferredItem<Item> GLYPH_TABLET_RETURN = ITEMS.registerItem(
            "glyph_tablet_return",
            props -> new com.jus144tice.lumenwilds.item.GlyphTabletItem(props, "We will wake when the light returns."),
            new Item.Properties());

    // Foods (Phase 8b). Lumen Fruit → brief night vision; Lumen Nectar → brief regeneration.
    public static final DeferredItem<Item> LUMEN_FRUIT = ITEMS.registerItem(
            "lumen_fruit",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(0.3F)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 140, 0), 1.0F)
                            .build()));

    /**
     * Glowberries — a sweet alien berry; a quick snack with a little glow, and (like vanilla sweet berries) an
     * {@code ItemNameBlockItem} so right-clicking valid soil <b>plants a Glowberry Bush</b> (v1.1c). The bush
     * itself has no separate BlockItem (skipped in the loop below).
     */
    public static final DeferredItem<ItemNameBlockItem> GLOWBERRY = ITEMS.registerItem(
            "glowberry",
            props -> new ItemNameBlockItem(ModBlocks.GLOWBERRY_BUSH.get(), props),
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.1F)
                            .alwaysEdible()
                            .build()));

    public static final DeferredItem<Item> LUMEN_NECTAR = ITEMS.registerItem(
            "lumen_nectar",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.2F)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F)
                            .build()));

    public static final DeferredItem<Item> AIR_GEL = ITEMS.registerSimpleItem("air_gel");

    /** Glowcap Stew (8b) — bowl + glowcap + lumen fruit + moonblossom; fills hunger + night vision, returns a bowl. */
    public static final DeferredItem<Item> GLOWCAP_STEW = ITEMS.registerItem(
            "glowcap_stew",
            Item::new,
            new Item.Properties()
                    .stacksTo(1)
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.6F)
                            .usingConvertsTo(Items.BOWL)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 1.0F)
                            .build()));

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

    /** Sporeling drops (6d): spore sac + glowcap spores; + spawn egg. */
    public static final DeferredItem<Item> SPORE_SAC = ITEMS.registerSimpleItem("spore_sac");

    public static final DeferredItem<Item> GLOWCAP_SPORES = ITEMS.registerSimpleItem("glowcap_spores");

    public static final DeferredItem<DeferredSpawnEggItem> SPORELING_SPAWN_EGG = ITEMS.registerItem(
            "sporeling_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.SPORELING, 0x3E5A34, 0x9ED85A, props),
            new Item.Properties());

    /** Mirelurker drops (6e): tooth, lumen algae, raw/cooked mirefish (foods); + spawn egg. */
    public static final DeferredItem<Item> MIRE_TOOTH = ITEMS.registerSimpleItem("mire_tooth");

    public static final DeferredItem<Item> LUMEN_ALGAE = ITEMS.registerSimpleItem("lumen_algae");

    public static final DeferredItem<Item> RAW_MIREFISH = ITEMS.registerItem(
            "raw_mirefish",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.2F)
                            .build()));

    public static final DeferredItem<Item> COOKED_MIREFISH = ITEMS.registerItem(
            "cooked_mirefish",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7F)
                            .build()));

    // --- Native fish caught by Lumenwater fishing (v1.1.3) — the Lumenwilds has no earth fish ------
    /** Glimmerfish — a common glowing food fish (the cod/salmon analog); better cooked. */
    public static final DeferredItem<Item> GLIMMERFISH = ITEMS.registerItem(
            "glimmerfish",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.2F)
                            .build()));

    public static final DeferredItem<Item> COOKED_GLIMMERFISH = ITEMS.registerItem(
            "cooked_glimmerfish",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8F)
                            .build()));

    /** Sporefin — the pufferfish analog: edible but risky (often inflicts Sporeblind on eating). */
    public static final DeferredItem<Item> SPOREFIN = ITEMS.registerItem(
            "sporefin",
            Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1F)
                            .alwaysEdible()
                            .effect(() -> new MobEffectInstance(ModMobEffects.SPOREBLIND, 120, 0), 0.7F)
                            .build()));

    public static final DeferredItem<DeferredSpawnEggItem> MIRELURKER_SPAWN_EGG = ITEMS.registerItem(
            "mirelurker_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.MIRELURKER, 0x2A3E36, 0x5AC0A0, props),
            new Item.Properties());

    /** Lumen Fish (6f): a fish bucket (catch it with a water bucket) + spawn egg. */
    public static final DeferredItem<MobBucketItem> LUMEN_FISH_BUCKET = ITEMS.registerItem(
            "lumen_fish_bucket",
            props ->
                    new MobBucketItem(ModEntities.LUMEN_FISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<DeferredSpawnEggItem> LUMEN_FISH_SPAWN_EGG = ITEMS.registerItem(
            "lumen_fish_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.LUMEN_FISH, 0x2E6A7A, 0x8FE8D0, props),
            new Item.Properties());

    /** Sky Jelly (6g): spawn egg (it drops the existing {@link #AIR_GEL}). */
    public static final DeferredItem<DeferredSpawnEggItem> SKY_JELLY_SPAWN_EGG = ITEMS.registerItem(
            "sky_jelly_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.SKY_JELLY, 0x7AB0D8, 0xCDEAF4, props),
            new Item.Properties());

    /** Glowmoth (6h): glow scales drop + spawn egg. */
    public static final DeferredItem<Item> GLOW_SCALES = ITEMS.registerSimpleItem("glow_scales");

    public static final DeferredItem<DeferredSpawnEggItem> GLOWMOTH_SPAWN_EGG = ITEMS.registerItem(
            "glowmoth_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.GLOWMOTH, 0x4A3A5A, 0xD8E060, props),
            new Item.Properties());

    /** Rootback drops (6i): plate + moonloam clumps (Living Fiber already exists); + spawn egg. */
    public static final DeferredItem<Item> ROOTBACK_PLATE = ITEMS.registerSimpleItem("rootback_plate");

    public static final DeferredItem<Item> MOONLOAM_CLUMPS = ITEMS.registerSimpleItem("moonloam_clumps");

    public static final DeferredItem<DeferredSpawnEggItem> ROOTBACK_SPAWN_EGG = ITEMS.registerItem(
            "rootback_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.ROOTBACK, 0x4A5E3A, 0x8FB060, props),
            new Item.Properties());

    /** Crag Wraith drops (6j): wraith membrane + crystal dust; + spawn egg. */
    public static final DeferredItem<Item> WRAITH_MEMBRANE = ITEMS.registerSimpleItem("wraith_membrane");

    public static final DeferredItem<Item> CRYSTAL_DUST = ITEMS.registerSimpleItem("crystal_dust");

    public static final DeferredItem<DeferredSpawnEggItem> CRAG_WRAITH_SPAWN_EGG = ITEMS.registerItem(
            "crag_wraith_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.CRAG_WRAITH, 0x2C2440, 0x9A6CE0, props),
            new Item.Properties());

    /** Echo Sentinel (10f) — the city ruin guardian; drops resonance/luminite/memory + rare relay. Spawn egg. */
    public static final DeferredItem<DeferredSpawnEggItem> ECHO_SENTINEL_SPAWN_EGG = ITEMS.registerItem(
            "echo_sentinel_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.ECHO_SENTINEL, 0x1A2A3A, 0x6FE0D8, props),
            new Item.Properties());

    public static final DeferredItem<DeferredSpawnEggItem> SPORE_TRADER_SPAWN_EGG = ITEMS.registerItem(
            "spore_trader_spawn_egg",
            props -> new DeferredSpawnEggItem(ModEntities.SPORE_TRADER, 0x6E4A7A, 0xC8E060, props),
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

    public static final DeferredItem<BoatItem> GLOWROOT_BOAT = ITEMS.registerItem(
            "glowroot_boat",
            props -> new BoatItem(false, ModBoatTypes.glowroot(), props),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<BoatItem> GLOWROOT_CHEST_BOAT = ITEMS.registerItem(
            "glowroot_chest_boat",
            props -> new BoatItem(true, ModBoatTypes.glowroot(), props),
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

    public static final DeferredItem<SignItem> GLOWROOT_SIGN = ITEMS.registerItem(
            "glowroot_sign",
            props -> new SignItem(props, ModBlocks.GLOWROOT_SIGN.get(), ModBlocks.GLOWROOT_WALL_SIGN.get()),
            new Item.Properties().stacksTo(16));

    public static final DeferredItem<HangingSignItem> GLOWROOT_HANGING_SIGN = ITEMS.registerItem(
            "glowroot_hanging_sign",
            props -> new HangingSignItem(
                    ModBlocks.GLOWROOT_HANGING_SIGN.get(), ModBlocks.GLOWROOT_WALL_HANGING_SIGN.get(), props),
            new Item.Properties().stacksTo(16));

    // --- Tools (v1.2) — a stone→iron progression so you can gear up in-dimension. Moonstone = stone-tier
    // (crafted from Cobbled Moonstone), Luminite = iron-tier (from Luminite Ingots). Tiers in ModToolTiers;
    // attack damage/speed mirror the matching vanilla tier per tool type. Recipes in ModRecipeProvider. -----
    public static final DeferredItem<PickaxeItem> MOONSTONE_PICKAXE =
            pickaxe("moonstone_pickaxe", ModToolTiers.MOONSTONE);
    public static final DeferredItem<AxeItem> MOONSTONE_AXE = axe("moonstone_axe", ModToolTiers.MOONSTONE, 7.0F, -3.2F);
    public static final DeferredItem<ShovelItem> MOONSTONE_SHOVEL = shovel("moonstone_shovel", ModToolTiers.MOONSTONE);
    public static final DeferredItem<HoeItem> MOONSTONE_HOE =
            hoe("moonstone_hoe", ModToolTiers.MOONSTONE, -1.0F, -2.0F);
    public static final DeferredItem<SwordItem> MOONSTONE_SWORD = sword("moonstone_sword", ModToolTiers.MOONSTONE);

    public static final DeferredItem<PickaxeItem> LUMINITE_PICKAXE = pickaxe("luminite_pickaxe", ModToolTiers.LUMINITE);
    public static final DeferredItem<AxeItem> LUMINITE_AXE = axe("luminite_axe", ModToolTiers.LUMINITE, 6.0F, -3.1F);
    public static final DeferredItem<ShovelItem> LUMINITE_SHOVEL = shovel("luminite_shovel", ModToolTiers.LUMINITE);
    public static final DeferredItem<HoeItem> LUMINITE_HOE = hoe("luminite_hoe", ModToolTiers.LUMINITE, -2.0F, -1.0F);
    public static final DeferredItem<SwordItem> LUMINITE_SWORD = sword("luminite_sword", ModToolTiers.LUMINITE);

    // Resonite tools (v1.3 Phase D) — the dimension's top, diamond-capable tier (see ModToolTiers#RESONITE).
    public static final DeferredItem<PickaxeItem> RESONITE_PICKAXE = pickaxe("resonite_pickaxe", ModToolTiers.RESONITE);
    public static final DeferredItem<AxeItem> RESONITE_AXE = axe("resonite_axe", ModToolTiers.RESONITE, 7.0F, -3.0F);
    public static final DeferredItem<ShovelItem> RESONITE_SHOVEL = shovel("resonite_shovel", ModToolTiers.RESONITE);
    public static final DeferredItem<HoeItem> RESONITE_HOE = hoe("resonite_hoe", ModToolTiers.RESONITE, -3.0F, 0.0F);
    public static final DeferredItem<SwordItem> RESONITE_SWORD = sword("resonite_sword", ModToolTiers.RESONITE);

    private static DeferredItem<PickaxeItem> pickaxe(String name, Tier tier) {
        return ITEMS.registerItem(
                name,
                p -> new PickaxeItem(tier, p.attributes(DiggerItem.createAttributes(tier, 1.0F, -2.8F))),
                new Item.Properties());
    }

    private static DeferredItem<AxeItem> axe(String name, Tier tier, float dmg, float speed) {
        return ITEMS.registerItem(
                name,
                p -> new AxeItem(tier, p.attributes(DiggerItem.createAttributes(tier, dmg, speed))),
                new Item.Properties());
    }

    private static DeferredItem<ShovelItem> shovel(String name, Tier tier) {
        return ITEMS.registerItem(
                name,
                p -> new ShovelItem(tier, p.attributes(DiggerItem.createAttributes(tier, 1.5F, -3.0F))),
                new Item.Properties());
    }

    private static DeferredItem<HoeItem> hoe(String name, Tier tier, float dmg, float speed) {
        return ITEMS.registerItem(
                name,
                p -> new HoeItem(tier, p.attributes(DiggerItem.createAttributes(tier, dmg, speed))),
                new Item.Properties());
    }

    private static DeferredItem<SwordItem> sword(String name, Tier tier) {
        return ITEMS.registerItem(
                name,
                p -> new SwordItem(tier, p.attributes(SwordItem.createAttributes(tier, 3.0F, -2.4F))),
                new Item.Properties());
    }

    // --- Block items ----------------------------------------------------------------------------
    // Auto-register a simple BlockItem for every registered block EXCEPT the portal interior (placed by
    // portal mechanics) and the sign blocks (handled above as SignItem/HangingSignItem; wall variants get
    // no item). Runs after the standalone + sign items so the Lumen Striker stays first in the tab. New
    // blocks get an item for free — no edits here.
    static {
        for (var block : ModBlocks.BLOCKS.getEntries()) {
            if (block == ModBlocks.LUMEN_PORTAL
                    || block == ModBlocks.LUMENWATER_BLOCK
                    || block == ModBlocks.ASCENSION_FIELD
                    || block == ModBlocks.DESCENT_FIELD
                    || block
                            == ModBlocks
                                    .GLOWBERRY_BUSH // planted by the Glowberry item (ItemNameBlockItem), no own item
                    || block.getId().getPath().contains("sign")) {
                continue;
            }
            ITEMS.registerSimpleBlockItem(block);
        }
    }
}
