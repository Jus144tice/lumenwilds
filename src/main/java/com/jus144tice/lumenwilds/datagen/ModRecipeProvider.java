/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/**
 * Placeholder recipes. The two progression-critical recipes are real and final-ish; everything else
 * comes later.
 *
 * <ul>
 *   <li><b>Lumenbound Stone</b> (4×): {@code C G C / S A S / C G C} — chiseled stone bricks (C),
 *       glowstone dust (G), smooth stone (S), amethyst shard (A). Overworld stonework + amethyst
 *       resonance + Nether light; mid-game, not netherite-expensive.</li>
 *   <li><b>Lumen Striker</b> (1×): vertical {@code I / A / G} — iron ingot, amethyst shard,
 *       glow ink sac.</li>
 * </ul>
 *
 * <p>TODO: recipes for the other blocks/items (planks from logs, crystal block ↔ shards, etc.).</p>
 */
public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // --- Lumenbound Stone (portal frame) ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LUMENBOUND_STONE.get(), 4)
                .pattern("CGC")
                .pattern("SAS")
                .pattern("CGC")
                .define('C', Items.CHISELED_STONE_BRICKS)
                .define('G', Items.GLOWSTONE_DUST)
                .define('S', Items.SMOOTH_STONE)
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
                .unlockedBy("has_glowstone_dust", has(Items.GLOWSTONE_DUST))
                .save(recipeOutput);

        // --- Lumen Striker (portal igniter) ---
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.LUMEN_STRIKER.get(), 1)
                .pattern("I")
                .pattern("A")
                .pattern("G")
                .define('I', Items.IRON_INGOT)
                .define('A', Items.AMETHYST_SHARD)
                .define('G', Items.GLOW_INK_SAC)
                .unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
                .save(recipeOutput);

        // --- Luminite Umbrella (rain shield + stone-tier weapon) ---
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LUMINITE_UMBRELLA.get(), 1)
                .pattern("SLS")
                .pattern(" L ")
                .pattern(" L ")
                .define('S', ModItems.LUMENSILK.get())
                .define('L', ModItems.LUMINITE_INGOT.get())
                .unlockedBy("has_luminite_ingot", has(ModItems.LUMINITE_INGOT.get()))
                .save(recipeOutput);

        buildGlowwoodRecipes(recipeOutput);
        buildGlowrootRecipes(recipeOutput);
        buildMoonstoneRecipes(recipeOutput);
        buildVeinstoneRecipes(recipeOutput);
        buildShimmerstoneRecipes(recipeOutput);
        buildLumenCrystalRecipes(recipeOutput);
        buildLuminiteRecipes(recipeOutput);
        buildResonanceRecipes(recipeOutput);
        buildRebuildRecipes(recipeOutput);
        buildLiftshaftRecipes(recipeOutput);
        buildOrphanDropRecipes(recipeOutput);
        buildToolRecipes(recipeOutput);
        buildMiningOreRecipes(recipeOutput);
        buildArmorRecipes(recipeOutput);
        buildFarmingRecipes(recipeOutput);
        buildWoodVariantRecipes(recipeOutput);
    }

    /** v1.4.2 wood variants — bookshelf/ladder/post for both species (Quark parity). */
    private void buildWoodVariantRecipes(RecipeOutput out) {
        woodVariants(
                out,
                ModBlocks.GLOWWOOD_PLANKS.get(),
                ModBlocks.STRIPPED_GLOWWOOD_LOG.get(),
                ModBlocks.GLOWWOOD_BOOKSHELF.get(),
                ModBlocks.GLOWWOOD_LADDER.get(),
                ModBlocks.GLOWWOOD_POST.get());
        woodVariants(
                out,
                ModBlocks.GLOWROOT_PLANKS.get(),
                ModBlocks.STRIPPED_GLOWROOT_LOG.get(),
                ModBlocks.GLOWROOT_BOOKSHELF.get(),
                ModBlocks.GLOWROOT_LADDER.get(),
                ModBlocks.GLOWROOT_POST.get());
    }

    private void woodVariants(
            RecipeOutput out,
            ItemLike planks,
            ItemLike strippedLog,
            ItemLike bookshelf,
            ItemLike ladder,
            ItemLike post) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, bookshelf)
                .pattern("PPP")
                .pattern("BBB")
                .pattern("PPP")
                .define('P', planks)
                .define('B', Items.BOOK)
                .unlockedBy("has_" + path(planks), has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ladder, 3)
                .pattern("P P")
                .pattern("PPP")
                .pattern("P P")
                .define('P', planks)
                .unlockedBy("has_" + path(planks), has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, post, 6)
                .pattern("L")
                .pattern("L")
                .pattern("L")
                .define('L', strippedLog)
                .unlockedBy("has_" + path(strippedLog), has(strippedLog))
                .save(out);
    }

    /** v1.4 Farming dishes — lumen-only (Glowloaf, Moonbeet Soup) + a lumen×overworld hybrid (Gilded Glimmerroot). */
    private void buildFarmingRecipes(RecipeOutput out) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.GLOWLOAF.get())
                .pattern("###")
                .define('#', ModItems.LUMENGRAIN.get())
                .unlockedBy("has_lumengrain", has(ModItems.LUMENGRAIN.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MOONBEET_SOUP.get())
                .requires(Items.BOWL)
                .requires(ModItems.MOONBEET.get(), 6)
                .unlockedBy("has_moonbeet", has(ModItems.MOONBEET.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.GILDED_GLIMMERROOT.get())
                .pattern("ggg")
                .pattern("grg")
                .pattern("ggg")
                .define('g', Items.GOLD_NUGGET)
                .define('r', ModItems.GLIMMERROOT.get())
                .unlockedBy("has_glimmerroot", has(ModItems.GLIMMERROOT.get()))
                .save(out);
        // Gourd seeds (F3): Moonmelon slice → 1 seed (like melon); Glowgourd → 4 seeds (like pumpkin).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MOONMELON_SEEDS.get())
                .requires(ModItems.MOONMELON_SLICE.get())
                .unlockedBy("has_moonmelon_slice", has(ModItems.MOONMELON_SLICE.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GLOWGOURD_SEEDS.get(), 4)
                .requires(ModBlocks.GLOWGOURD.get())
                .unlockedBy("has_glowgourd", has(ModBlocks.GLOWGOURD.get()))
                .save(out);
        // Alien crops (F4): Lumen Sugar from Glimmerreed (cane→sugar); Roasted Duskbean (smelt/smoke).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LUMEN_SUGAR.get())
                .requires(ModItems.GLIMMERREED.get())
                .unlockedBy("has_glimmerreed", has(ModItems.GLIMMERREED.get()))
                .save(out);
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.DUSKBEAN.get()),
                        RecipeCategory.FOOD,
                        ModItems.ROASTED_DUSKBEAN.get(),
                        0.2F,
                        200)
                .unlockedBy("has_duskbean", has(ModItems.DUSKBEAN.get()))
                .save(out);
        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(ModItems.DUSKBEAN.get()),
                        RecipeCategory.FOOD,
                        ModItems.ROASTED_DUSKBEAN.get(),
                        0.2F,
                        100)
                .unlockedBy("has_duskbean", has(ModItems.DUSKBEAN.get()))
                .save(out, id("roasted_duskbean_from_smoking"));

        // Dishes (F5): lumen-only + lumen×overworld hybrids.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.WILDS_PIE.get())
                .requires(ModBlocks.GLOWGOURD.get())
                .requires(ModItems.LUMEN_SUGAR.get())
                .requires(Items.EGG)
                .unlockedBy("has_glowgourd", has(ModBlocks.GLOWGOURD.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.LUMEN_COOKIE.get(), 8)
                .pattern("GCG")
                .define('G', ModItems.LUMENGRAIN.get())
                .define('C', Items.COCOA_BEANS)
                .unlockedBy("has_lumengrain", has(ModItems.LUMENGRAIN.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.DUSK_STEW.get())
                .requires(Items.BOWL)
                .requires(ModItems.ROASTED_DUSKBEAN.get())
                .requires(ModItems.CAVECAP.get())
                .requires(ModItems.GLIMMERROOT.get())
                .unlockedBy("has_roasted_duskbean", has(ModItems.ROASTED_DUSKBEAN.get()))
                .save(out);
    }

    /** v1.3 Phase D2: the Resonite armor set, standard vanilla armor crafting patterns. */
    private void buildArmorRecipes(RecipeOutput out) {
        armorSet(
                out,
                ModItems.LUMINITE_INGOT.get(),
                "luminite_ingot",
                ModItems.LUMINITE_HELMET.get(),
                ModItems.LUMINITE_CHESTPLATE.get(),
                ModItems.LUMINITE_LEGGINGS.get(),
                ModItems.LUMINITE_BOOTS.get());
        armorSet(
                out,
                ModItems.RESONITE_INGOT.get(),
                "resonite_ingot",
                ModItems.RESONITE_HELMET.get(),
                ModItems.RESONITE_CHESTPLATE.get(),
                ModItems.RESONITE_LEGGINGS.get(),
                ModItems.RESONITE_BOOTS.get());
    }

    /** The four vanilla armor crafting patterns for a material's ingot. */
    private void armorSet(
            RecipeOutput out,
            ItemLike ingot,
            String key,
            ItemLike helmet,
            ItemLike chest,
            ItemLike legs,
            ItemLike boots) {
        String crit = "has_" + key;
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .pattern("MMM")
                .pattern("M M")
                .define('M', ingot)
                .unlockedBy(crit, has(ingot))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chest)
                .pattern("M M")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ingot)
                .unlockedBy(crit, has(ingot))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, legs)
                .pattern("MMM")
                .pattern("M M")
                .pattern("M M")
                .define('M', ingot)
                .unlockedBy(crit, has(ingot))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
                .pattern("M M")
                .pattern("M M")
                .define('M', ingot)
                .unlockedBy(crit, has(ingot))
                .save(out);
    }

    /** v1.3 Phase C ores: Emberglow + Pale Opal block↔9, Resonite ore/raw → ingot (smelt+blast) → block. */
    private void buildMiningOreRecipes(RecipeOutput out) {
        nineBlock(out, ModItems.EMBERGLOW.get(), ModBlocks.EMBERGLOW_BLOCK.get(), "emberglow");
        nineBlock(out, ModItems.PALE_OPAL.get(), ModBlocks.PALE_OPAL_BLOCK.get(), "pale_opal");

        // Emberglow Torch (v1.4.7) — like the vanilla coal torch: Emberglow over a stick → 4 torches.
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.EMBERGLOW_TORCH.get(), 4)
                .pattern("E")
                .pattern("S")
                .define('E', ModItems.EMBERGLOW.get())
                .define('S', Items.STICK)
                .unlockedBy("has_emberglow", has(ModItems.EMBERGLOW.get()))
                .save(out);

        Ingredient resoniteOres = Ingredient.of(ModBlocks.RESONITE_ORE.get(), ModBlocks.DEEP_RESONITE_ORE.get());
        SimpleCookingRecipeBuilder.smelting(resoniteOres, RecipeCategory.MISC, ModItems.RESONITE_INGOT.get(), 1.0F, 200)
                .unlockedBy("has_resonite_ore", has(ModBlocks.RESONITE_ORE.get()))
                .save(out, id("resonite_ingot_from_smelting_ore"));
        SimpleCookingRecipeBuilder.blasting(resoniteOres, RecipeCategory.MISC, ModItems.RESONITE_INGOT.get(), 1.0F, 100)
                .unlockedBy("has_resonite_ore", has(ModBlocks.RESONITE_ORE.get()))
                .save(out, id("resonite_ingot_from_blasting_ore"));
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_RESONITE.get()),
                        RecipeCategory.MISC,
                        ModItems.RESONITE_INGOT.get(),
                        1.0F,
                        200)
                .unlockedBy("has_raw_resonite", has(ModItems.RAW_RESONITE.get()))
                .save(out, id("resonite_ingot_from_smelting_raw"));
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.RAW_RESONITE.get()),
                        RecipeCategory.MISC,
                        ModItems.RESONITE_INGOT.get(),
                        1.0F,
                        100)
                .unlockedBy("has_raw_resonite", has(ModItems.RAW_RESONITE.get()))
                .save(out, id("resonite_ingot_from_blasting_raw"));
        nineBlock(out, ModItems.RESONITE_INGOT.get(), ModBlocks.RESONITE_BLOCK.get(), "resonite");
    }

    /** item ↔ 3×3 storage block (mirrors the vanilla ingot/block pattern). */
    private void nineBlock(RecipeOutput out, ItemLike item, ItemLike block, String key) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', item)
                .unlockedBy("has_" + key, has(item))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 9)
                .requires(block)
                .unlockedBy("has_" + key + "_block", has(block))
                .save(out, id(key + "_from_block"));
    }

    /** Tool sets (v1.2): Moonstone (from Cobbled Moonstone) + Luminite (from Luminite Ingots), vanilla shapes. */
    private void buildToolRecipes(RecipeOutput out) {
        toolSet(
                out,
                ModItems.MOONSTONE_PICKAXE.get(),
                ModItems.MOONSTONE_AXE.get(),
                ModItems.MOONSTONE_SHOVEL.get(),
                ModItems.MOONSTONE_HOE.get(),
                ModItems.MOONSTONE_SWORD.get(),
                ModBlocks.COBBLED_MOONSTONE.get(),
                "cobbled_moonstone");
        toolSet(
                out,
                ModItems.LUMINITE_PICKAXE.get(),
                ModItems.LUMINITE_AXE.get(),
                ModItems.LUMINITE_SHOVEL.get(),
                ModItems.LUMINITE_HOE.get(),
                ModItems.LUMINITE_SWORD.get(),
                ModItems.LUMINITE_INGOT.get(),
                "luminite_ingot");
        toolSet(
                out,
                ModItems.RESONITE_PICKAXE.get(),
                ModItems.RESONITE_AXE.get(),
                ModItems.RESONITE_SHOVEL.get(),
                ModItems.RESONITE_HOE.get(),
                ModItems.RESONITE_SWORD.get(),
                ModItems.RESONITE_INGOT.get(),
                "resonite_ingot");
    }

    private void toolSet(
            RecipeOutput out,
            ItemLike pickaxe,
            ItemLike axe,
            ItemLike shovel,
            ItemLike hoe,
            ItemLike sword,
            ItemLike mat,
            String matName) {
        String crit = "has_" + matName;
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .define('M', mat)
                .define('S', Items.STICK)
                .unlockedBy(crit, has(mat))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
                .pattern("MM")
                .pattern("MS")
                .pattern(" S")
                .define('M', mat)
                .define('S', Items.STICK)
                .unlockedBy(crit, has(mat))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
                .pattern("M")
                .pattern("S")
                .pattern("S")
                .define('M', mat)
                .define('S', Items.STICK)
                .unlockedBy(crit, has(mat))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
                .pattern("MM")
                .pattern(" S")
                .pattern(" S")
                .define('M', mat)
                .define('S', Items.STICK)
                .unlockedBy(crit, has(mat))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword)
                .pattern("M")
                .pattern("M")
                .pattern("S")
                .define('M', mat)
                .define('S', Items.STICK)
                .unlockedBy(crit, has(mat))
                .save(out);
    }

    /**
     * Gives every otherwise-useless mob drop a real purpose (v1.1d) — mostly conversions into universally
     * useful vanilla/Lumenwilds items, so no new blocks/items are needed. (Several also double as Lumenwater
     * fishing bait, wired in v1.1f; and Glowcap Spores brew Sporeblind in {@code event.ModBrewing}.)
     */
    private void buildOrphanDropRecipes(RecipeOutput out) {
        // Hides → leather (universal: armor, books, item frames, …). Both the Grazer's and the Shade Stalker's.
        salvage(out, ModItems.GRAZER_HIDE.get(), Items.LEATHER, 1, "leather_from_grazer_hide");
        salvage(out, ModItems.DARK_HIDE.get(), Items.LEATHER, 1, "leather_from_dark_hide");
        // Glow Sinew → string (binding fibre): leads, bows, wool, …
        salvage(out, ModItems.GLOW_SINEW.get(), Items.STRING, 1, "string_from_glow_sinew");
        // Lumen Algae → green dye.
        salvage(out, ModItems.LUMEN_ALGAE.get(), Items.GREEN_DYE, 1, "green_dye_from_lumen_algae");
        // Wraith Membrane → phantom membrane (Slow Falling brewing + elytra repair) — both are flying-mob membranes.
        salvage(
                out,
                ModItems.WRAITH_MEMBRANE.get(),
                Items.PHANTOM_MEMBRANE,
                1,
                "phantom_membrane_from_wraith_membrane");
        // Mire Tooth → bone meal (ground calcium).
        salvage(out, ModItems.MIRE_TOOTH.get(), Items.BONE_MEAL, 2, "bone_meal_from_mire_tooth");
        // Rootback Plate → iron nuggets (salvaged plating).
        salvage(out, ModItems.ROOTBACK_PLATE.get(), Items.IRON_NUGGET, 2, "iron_nugget_from_rootback_plate");
        // Glow Scales (2) → Glow Pollen (feeds the big glow-pollen economy: lumenbulb, glowbrick, sporeglass, …).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GLOW_POLLEN.get(), 1)
                .requires(ModItems.GLOW_SCALES.get(), 2)
                .unlockedBy("has_glow_scales", has(ModItems.GLOW_SCALES.get()))
                .save(out, id("glow_pollen_from_glow_scales"));
        // Shade Claw (2) → Echo Dust (the rare, useful Shade Stalker drop used in the Lumen Anchor).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ECHO_DUST.get(), 1)
                .requires(ModItems.SHADE_CLAW.get(), 2)
                .unlockedBy("has_shade_claw", has(ModItems.SHADE_CLAW.get()))
                .save(out, id("echo_dust_from_shade_claw"));
        // Crystal Dust (4) → Glasspetal Block (the Crags crystal — the Crag Wraith's own biome material).
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLASSPETAL_BLOCK.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.CRYSTAL_DUST.get())
                .unlockedBy("has_crystal_dust", has(ModItems.CRYSTAL_DUST.get()))
                .save(out, id("glasspetal_block_from_crystal_dust"));
        // Moonloam Clumps (4) → Moonloam block.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOONLOAM.get(), 1)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.MOONLOAM_CLUMPS.get())
                .unlockedBy("has_moonloam_clumps", has(ModItems.MOONLOAM_CLUMPS.get()))
                .save(out, id("moonloam_from_clumps"));
    }

    /** A single-ingredient shapeless conversion ({@code count} of {@code to} from one {@code from}). */
    private void salvage(RecipeOutput out, ItemLike from, ItemLike to, int count, String recipeId) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, to, count)
                .requires(from)
                .unlockedBy(
                        "has_" + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath(), has(from))
                .save(out, id(recipeId));
    }

    /**
     * "Rebuild the Lumenwrights' kit" (Phase 10h.1) — once the player has looted ancient fragments + mined the
     * resources, the buildable tech becomes craftable: Lumen Conduit, Lumenbulb, Memory Crystal (repaired from
     * shards), the Active Light Engine, and the aged/decay building variants. The fragments themselves stay
     * loot-only (the ruin gate).
     */
    private void buildRebuildRecipes(RecipeOutput out) {
        // Lumen Conduit — glowbrick + a crystal core.
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.LUMEN_CONDUIT.get(), 3)
                .pattern("GCG")
                .define('G', ModBlocks.GLOWBRICK.get())
                .define('C', ModItems.LUMEN_CRYSTAL_SHARD.get())
                .unlockedBy("has_glowbrick", has(ModBlocks.GLOWBRICK.get()))
                .save(out);

        // Lumenbulb — a glowstone-like living light.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LUMENBULB.get(), 1)
                .pattern(" P ")
                .pattern("PCP")
                .pattern(" P ")
                .define('P', ModItems.GLOW_POLLEN.get())
                .define('C', ModItems.LUMEN_CRYSTAL_SHARD.get())
                .unlockedBy("has_glow_pollen", has(ModItems.GLOW_POLLEN.get()))
                .save(out);

        // Memory Crystal — reassembled from four shards.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MEMORY_CRYSTAL.get(), 1)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModItems.MEMORY_CRYSTAL_SHARD.get())
                .unlockedBy("has_memory_crystal_shard", has(ModItems.MEMORY_CRYSTAL_SHARD.get()))
                .save(out);

        // Active Light Engine — a full-scale core (built around a Resonance Core); the city centrepiece.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ACTIVE_LIGHT_ENGINE.get(), 1)
                .pattern("GCG")
                .pattern("CRC")
                .pattern("GCG")
                .define('G', ModBlocks.GLOWBRICK.get())
                .define('C', ModBlocks.LUMEN_CRYSTAL_BLOCK.get())
                .define('R', ModBlocks.RESONANCE_CORE.get())
                .unlockedBy("has_resonance_core", has(ModBlocks.RESONANCE_CORE.get()))
                .save(out);

        // Aged building variants — stonecutter cuts from Glowbrick / Sporeglass (for the ruined look).
        Block g = ModBlocks.GLOWBRICK.get();
        cut(out, g, ModBlocks.CRACKED_GLOWBRICK.get(), 1);
        cut(out, g, ModBlocks.ANCIENT_GLOWBRICK.get(), 1);
        cut(out, g, ModBlocks.OVERGROWN_GLOWBRICK.get(), 1);
        cut(out, ModBlocks.SPOREGLASS.get(), ModBlocks.BROKEN_SPOREGLASS.get(), 1);

        // Mossy / rooted moonstone (like vanilla mossy stone bricks).
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_MOONSTONE_BRICKS.get(), 1)
                .requires(ModBlocks.MOONSTONE_BRICKS.get())
                .requires(Items.MOSS_BLOCK)
                .unlockedBy("has_moonstone_bricks", has(ModBlocks.MOONSTONE_BRICKS.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ROOTED_MOONSTONE.get(), 1)
                .requires(ModBlocks.MOONSTONE.get())
                .requires(Items.HANGING_ROOTS)
                .unlockedBy("has_moonstone", has(ModBlocks.MOONSTONE.get()))
                .save(out);
    }

    /** Resonance tech (Phase 10e): the functional Resonance Core (from a fragment) + the Ancient Door. */
    private void buildResonanceRecipes(RecipeOutput out) {
        // Resonance Core — rebuild a working core around a salvaged fragment.
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RESONANCE_CORE.get(), 1)
                .pattern("LSL")
                .pattern("CFC")
                .pattern("LSL")
                .define('L', ModItems.LUMINITE_INGOT.get())
                .define('S', ModBlocks.SHIMMERSTONE.get())
                .define('C', ModItems.LUMEN_CRYSTAL_SHARD.get())
                .define('F', ModItems.RESONANCE_CORE_FRAGMENT.get())
                .unlockedBy("has_resonance_core_fragment", has(ModItems.RESONANCE_CORE_FRAGMENT.get()))
                .save(out);

        // Ancient Door — 3 from glowbrick, like a vanilla door.
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.ANCIENT_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', ModBlocks.GLOWBRICK.get())
                .unlockedBy("has_glowbrick", has(ModBlocks.GLOWBRICK.get()))
                .save(out);

        // Gravity Lens — a Shimmerstone frame around a lens rebuilt from fragments + a crystal core.
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GRAVITY_LENS.get(), 1)
                .pattern("SSS")
                .pattern("FCF")
                .pattern("SSS")
                .define('S', ModBlocks.SHIMMERSTONE.get())
                .define('F', ModItems.GRAVITY_LENS_FRAGMENT.get())
                .define('C', ModBlocks.LUMEN_CRYSTAL_BLOCK.get())
                .unlockedBy("has_gravity_lens_fragment", has(ModItems.GRAVITY_LENS_FRAGMENT.get()))
                .save(out);

        // Lumen Relay — a small repeater device.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.LUMEN_RELAY.get(), 2)
                .requires(ModItems.LUMINITE_INGOT.get())
                .requires(ModItems.LUMEN_CRYSTAL_SHARD.get())
                .requires(ModItems.LUMEN_CRYSTAL_SHARD.get())
                .unlockedBy("has_luminite_ingot", has(ModItems.LUMINITE_INGOT.get()))
                .save(out);
    }

    /**
     * Lumenwright Liftshafts (Phase 11b) — the player-craftable elevator kit. The Lumen Field Projector is the
     * bible's advanced, ruin-gated reward (recipe {@code G L G / R C R / I E I}); the Gravity Repeater is cheap
     * so a player can chain arbitrarily tall shafts affordably.
     */
    private void buildLiftshaftRecipes(RecipeOutput out) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.LUMEN_FIELD_PROJECTOR.get(), 1)
                .pattern("GLG")
                .pattern("RCR")
                .pattern("IEI")
                .define('G', ModItems.GRAVITY_LENS_FRAGMENT.get())
                .define('L', ModItems.LUMEN_CRYSTAL_SHARD.get())
                .define('R', ModBlocks.LUMEN_RELAY.get())
                .define('C', ModItems.RESONANCE_CORE_FRAGMENT.get())
                .define('I', ModItems.LUMINITE_INGOT.get())
                .define('E', ModItems.MEMORY_CRYSTAL_SHARD.get())
                .unlockedBy("has_gravity_lens_fragment", has(ModItems.GRAVITY_LENS_FRAGMENT.get()))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GRAVITY_REPEATER.get(), 2)
                .pattern("SFS")
                .pattern("SRS")
                .define('S', ModBlocks.SHIMMERSTONE.get())
                .define('F', ModItems.GRAVITY_LENS_FRAGMENT.get())
                .define('R', ModBlocks.LUMEN_RELAY.get())
                .unlockedBy("has_gravity_lens_fragment", has(ModItems.GRAVITY_LENS_FRAGMENT.get()))
                .save(out);
    }

    /**
     * Luminite + Glowbrick (Phase 10a, the Lumenwright materials). Ore/raw → ingot (smelt + blast),
     * ingot ↔ storage block, the bible's Glowbrick craft ({@code L I L / I C I / L I L}), and stonecutter
     * cuts from Glowbrick to the whole architectural family.
     */
    private void buildLuminiteRecipes(RecipeOutput out) {
        // Ore + raw → ingot (smelting + blasting).
        Ingredient ores = Ingredient.of(ModBlocks.LUMINITE_ORE.get(), ModBlocks.DEEP_LUMINITE_ORE.get());
        SimpleCookingRecipeBuilder.smelting(ores, RecipeCategory.MISC, ModItems.LUMINITE_INGOT.get(), 0.7F, 200)
                .unlockedBy("has_luminite_ore", has(ModBlocks.LUMINITE_ORE.get()))
                .save(out, id("luminite_ingot_from_smelting_ore"));
        SimpleCookingRecipeBuilder.blasting(ores, RecipeCategory.MISC, ModItems.LUMINITE_INGOT.get(), 0.7F, 100)
                .unlockedBy("has_luminite_ore", has(ModBlocks.LUMINITE_ORE.get()))
                .save(out, id("luminite_ingot_from_blasting_ore"));
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_LUMINITE.get()),
                        RecipeCategory.MISC,
                        ModItems.LUMINITE_INGOT.get(),
                        0.7F,
                        200)
                .unlockedBy("has_raw_luminite", has(ModItems.RAW_LUMINITE.get()))
                .save(out, id("luminite_ingot_from_smelting_raw"));
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.RAW_LUMINITE.get()),
                        RecipeCategory.MISC,
                        ModItems.LUMINITE_INGOT.get(),
                        0.7F,
                        100)
                .unlockedBy("has_raw_luminite", has(ModItems.RAW_LUMINITE.get()))
                .save(out, id("luminite_ingot_from_blasting_raw"));

        // Ingot ↔ storage block.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LUMINITE_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.LUMINITE_INGOT.get())
                .unlockedBy("has_luminite_ingot", has(ModItems.LUMINITE_INGOT.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LUMINITE_INGOT.get(), 9)
                .requires(ModBlocks.LUMINITE_BLOCK.get())
                .unlockedBy("has_luminite_block", has(ModBlocks.LUMINITE_BLOCK.get()))
                .save(out, id("luminite_ingot_from_block"));

        // Glowbrick — the signature craft (I = luminite ingot, C = lumen crystal shard, L = glow pollen).
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWBRICK.get(), 4)
                .pattern("LIL")
                .pattern("ICI")
                .pattern("LIL")
                .define('I', ModItems.LUMINITE_INGOT.get())
                .define('C', ModItems.LUMEN_CRYSTAL_SHARD.get())
                .define('L', ModItems.GLOW_POLLEN.get())
                .unlockedBy("has_luminite_ingot", has(ModItems.LUMINITE_INGOT.get()))
                .save(out);

        // Glowbrick architectural family: 2×2 tiles + stonecutter cuts from the base block.
        square2x2(out, ModBlocks.GLOWBRICK.get(), ModBlocks.GLOWBRICK_TILES.get());

        Block g = ModBlocks.GLOWBRICK.get();
        cut(out, g, ModBlocks.GLOWBRICK_TILES.get(), 1);
        cut(out, g, ModBlocks.CHISELED_GLOWBRICK.get(), 1);
        cut(out, g, ModBlocks.GLOWBRICK_PILLAR.get(), 1);
        cut(out, g, ModBlocks.GLOWBRICK_STAIRS.get(), 1);
        cut(out, g, ModBlocks.GLOWBRICK_WALL.get(), 1);
        cut(out, g, ModBlocks.GLOWBRICK_SLAB.get(), 2);

        // Crafted-shape recipes too (stairs/slab/wall the normal way).
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWBRICK_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', ModBlocks.GLOWBRICK.get())
                .unlockedBy("has_glowbrick", has(ModBlocks.GLOWBRICK.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWBRICK_SLAB.get(), 6)
                .pattern("###")
                .define('#', ModBlocks.GLOWBRICK.get())
                .unlockedBy("has_glowbrick", has(ModBlocks.GLOWBRICK.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWBRICK_WALL.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', ModBlocks.GLOWBRICK.get())
                .unlockedBy("has_glowbrick", has(ModBlocks.GLOWBRICK.get()))
                .save(out);
    }

    /** Lumen Crystal: block ↔ 9 shards, and ore → shard (smelting + blasting). */
    private void buildLumenCrystalRecipes(RecipeOutput out) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LUMEN_CRYSTAL_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.LUMEN_CRYSTAL_SHARD.get())
                .unlockedBy("has_lumen_crystal_shard", has(ModItems.LUMEN_CRYSTAL_SHARD.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LUMEN_CRYSTAL_SHARD.get(), 9)
                .requires(ModBlocks.LUMEN_CRYSTAL_BLOCK.get())
                .unlockedBy("has_lumen_crystal_block", has(ModBlocks.LUMEN_CRYSTAL_BLOCK.get()))
                .save(out, id("lumen_crystal_shard_from_block"));

        Ingredient ores = Ingredient.of(ModBlocks.LUMEN_CRYSTAL_ORE.get(), ModBlocks.DEEP_LUMEN_CRYSTAL_ORE.get());
        SimpleCookingRecipeBuilder.smelting(ores, RecipeCategory.MISC, ModItems.LUMEN_CRYSTAL_SHARD.get(), 0.7F, 200)
                .unlockedBy("has_lumen_crystal_ore", has(ModBlocks.LUMEN_CRYSTAL_ORE.get()))
                .save(out, id("lumen_crystal_shard_from_smelting"));
        SimpleCookingRecipeBuilder.blasting(ores, RecipeCategory.MISC, ModItems.LUMEN_CRYSTAL_SHARD.get(), 0.7F, 100)
                .unlockedBy("has_lumen_crystal_ore", has(ModBlocks.LUMEN_CRYSTAL_ORE.get()))
                .save(out, id("lumen_crystal_shard_from_blasting"));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Lumenwilds.MOD_ID, path);
    }

    /** Shimmerstone variants + the two Sporeglass crafts (Phase 4). */
    private void buildShimmerstoneRecipes(RecipeOutput out) {
        // Base Shimmerstone craft (v1.2.1) — crystal-infused Moonstone, so the set + the Gravity Repeater /
        // Field Projector are reachable without first finding a Glasspetal Spire. 4 Moonstone + 1 shard → 4.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SHIMMERSTONE.get(), 4)
                .requires(ModBlocks.MOONSTONE.get(), 4)
                .requires(ModItems.LUMEN_CRYSTAL_SHARD.get())
                .unlockedBy("has_lumen_crystal_shard", has(ModItems.LUMEN_CRYSTAL_SHARD.get()))
                .save(out);

        square2x2(out, ModBlocks.SHIMMERSTONE.get(), ModBlocks.POLISHED_SHIMMERSTONE.get());
        square2x2(out, ModBlocks.POLISHED_SHIMMERSTONE.get(), ModBlocks.SHIMMERSTONE_BRICKS.get());
        square2x2(out, ModBlocks.SHIMMERSTONE_BRICKS.get(), ModBlocks.SHIMMERSTONE_TILES.get());

        Block s = ModBlocks.SHIMMERSTONE.get();
        cut(out, s, ModBlocks.POLISHED_SHIMMERSTONE.get(), 1);
        cut(out, s, ModBlocks.SHIMMERSTONE_BRICKS.get(), 1);
        cut(out, s, ModBlocks.SHIMMERSTONE_TILES.get(), 1);
        cut(out, s, ModBlocks.SHIMMERSTONE_PILLAR.get(), 1);
        for (Block v : List.of(
                ModBlocks.POLISHED_SHIMMERSTONE_STAIRS.get(),
                ModBlocks.POLISHED_SHIMMERSTONE_WALL.get(),
                ModBlocks.SHIMMERSTONE_BRICK_STAIRS.get(),
                ModBlocks.SHIMMERSTONE_BRICK_WALL.get(),
                ModBlocks.SHIMMERSTONE_TILE_STAIRS.get(),
                ModBlocks.SHIMMERSTONE_TILE_WALL.get())) {
            cut(out, s, v, 1);
        }
        for (Block v : List.of(
                ModBlocks.POLISHED_SHIMMERSTONE_SLAB.get(),
                ModBlocks.SHIMMERSTONE_BRICK_SLAB.get(),
                ModBlocks.SHIMMERSTONE_TILE_SLAB.get())) {
            cut(out, s, v, 2);
        }

        // Shimmerstone glass: glass + polished shimmerstone.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SHIMMERSTONE_GLASS.get(), 1)
                .requires(Items.GLASS)
                .requires(ModBlocks.POLISHED_SHIMMERSTONE.get())
                .unlockedBy("has_shimmerstone", has(ModBlocks.SHIMMERSTONE.get()))
                .save(out);

        // Sporeglass: glass + glow pollen + lumen crystal shard.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SPOREGLASS.get(), 1)
                .requires(Items.GLASS)
                .requires(ModItems.GLOW_POLLEN.get())
                .requires(ModItems.LUMEN_CRYSTAL_SHARD.get())
                .unlockedBy("has_glow_pollen", has(ModItems.GLOW_POLLEN.get()))
                .save(out);

        // Sporeglass panes: 6 sporeglass → 16 (like vanilla glass panes).
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SPOREGLASS_PANE.get(), 16)
                .pattern("###")
                .pattern("###")
                .define('#', ModBlocks.SPOREGLASS.get())
                .unlockedBy("has_sporeglass", has(ModBlocks.SPOREGLASS.get()))
                .save(out);
    }

    /**
     * Moonstone + Deep Moonstone recipes: smelting (cobbled→base, base→smooth), 2×2 crafting for the
     * base bricks/polished, and stonecutter recipes from the family base to every variant (the cheap
     * path to all shapes). Each result has a single source, so stonecutter ids never collide.
     */
    private void buildMoonstoneRecipes(RecipeOutput out) {
        // Smelting.
        smelt(out, ModBlocks.COBBLED_MOONSTONE.get(), ModBlocks.MOONSTONE.get());
        smelt(out, ModBlocks.MOONSTONE.get(), ModBlocks.SMOOTH_MOONSTONE.get());
        smelt(out, ModBlocks.COBBLED_DEEP_MOONSTONE.get(), ModBlocks.DEEP_MOONSTONE.get());

        // 2×2 crafting for the base processed blocks.
        square2x2(out, ModBlocks.MOONSTONE.get(), ModBlocks.MOONSTONE_BRICKS.get());
        square2x2(out, ModBlocks.MOONSTONE_BRICKS.get(), ModBlocks.MOONSTONE_TILES.get());
        square2x2(out, ModBlocks.DEEP_MOONSTONE.get(), ModBlocks.POLISHED_DEEP_MOONSTONE.get());
        square2x2(out, ModBlocks.POLISHED_DEEP_MOONSTONE.get(), ModBlocks.DEEP_MOONSTONE_BRICKS.get());
        square2x2(out, ModBlocks.DEEP_MOONSTONE_BRICKS.get(), ModBlocks.DEEP_MOONSTONE_TILES.get());

        // Stonecutter: moonstone → every moonstone variant.
        Block m = ModBlocks.MOONSTONE.get();
        cut(out, m, ModBlocks.SMOOTH_MOONSTONE.get(), 1);
        cut(out, m, ModBlocks.MOONSTONE_BRICKS.get(), 1);
        cut(out, m, ModBlocks.CHISELED_MOONSTONE.get(), 1);
        cut(out, m, ModBlocks.MOONSTONE_TILES.get(), 1);
        for (Block v : List.of(
                ModBlocks.MOONSTONE_STAIRS.get(),
                ModBlocks.MOONSTONE_WALL.get(),
                ModBlocks.COBBLED_MOONSTONE_STAIRS.get(),
                ModBlocks.COBBLED_MOONSTONE_WALL.get(),
                ModBlocks.MOONSTONE_BRICK_STAIRS.get(),
                ModBlocks.MOONSTONE_BRICK_WALL.get(),
                ModBlocks.MOONSTONE_TILE_STAIRS.get(),
                ModBlocks.MOONSTONE_TILE_WALL.get(),
                ModBlocks.SMOOTH_MOONSTONE_STAIRS.get())) {
            cut(out, m, v, 1);
        }
        for (Block v : List.of(
                ModBlocks.MOONSTONE_SLAB.get(),
                ModBlocks.COBBLED_MOONSTONE_SLAB.get(),
                ModBlocks.MOONSTONE_BRICK_SLAB.get(),
                ModBlocks.MOONSTONE_TILE_SLAB.get(),
                ModBlocks.SMOOTH_MOONSTONE_SLAB.get())) {
            cut(out, m, v, 2);
        }

        // Stonecutter: deep_moonstone → every deep variant.
        Block d = ModBlocks.DEEP_MOONSTONE.get();
        cut(out, d, ModBlocks.COBBLED_DEEP_MOONSTONE.get(), 1);
        cut(out, d, ModBlocks.POLISHED_DEEP_MOONSTONE.get(), 1);
        cut(out, d, ModBlocks.DEEP_MOONSTONE_BRICKS.get(), 1);
        cut(out, d, ModBlocks.DEEP_MOONSTONE_TILES.get(), 1);
        for (Block v : List.of(
                ModBlocks.COBBLED_DEEP_MOONSTONE_STAIRS.get(),
                ModBlocks.COBBLED_DEEP_MOONSTONE_WALL.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_STAIRS.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_WALL.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_STAIRS.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_WALL.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_STAIRS.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_WALL.get())) {
            cut(out, d, v, 1);
        }
        for (Block v : List.of(
                ModBlocks.COBBLED_DEEP_MOONSTONE_SLAB.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_SLAB.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_SLAB.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_SLAB.get())) {
            cut(out, d, v, 2);
        }

        // Stonecutter: the MINED cobbled forms are ALSO full inputs (like vanilla cobbled_deepslate/cobblestone),
        // so a player can stonecut freshly-mined stone without smelting it to the smooth form first — the smooth
        // moonstone finish stays smelt-only, as in vanilla. (Was the "cobbled deep moonstone won't cut" bug.)
        Block cm = ModBlocks.COBBLED_MOONSTONE.get();
        cut(out, cm, ModBlocks.MOONSTONE_BRICKS.get(), 1);
        cut(out, cm, ModBlocks.CHISELED_MOONSTONE.get(), 1);
        cut(out, cm, ModBlocks.MOONSTONE_TILES.get(), 1);
        for (Block v : List.of(
                ModBlocks.MOONSTONE_STAIRS.get(),
                ModBlocks.MOONSTONE_WALL.get(),
                ModBlocks.COBBLED_MOONSTONE_STAIRS.get(),
                ModBlocks.COBBLED_MOONSTONE_WALL.get(),
                ModBlocks.MOONSTONE_BRICK_STAIRS.get(),
                ModBlocks.MOONSTONE_BRICK_WALL.get(),
                ModBlocks.MOONSTONE_TILE_STAIRS.get(),
                ModBlocks.MOONSTONE_TILE_WALL.get())) {
            cut(out, cm, v, 1);
        }
        for (Block v : List.of(
                ModBlocks.MOONSTONE_SLAB.get(),
                ModBlocks.COBBLED_MOONSTONE_SLAB.get(),
                ModBlocks.MOONSTONE_BRICK_SLAB.get(),
                ModBlocks.MOONSTONE_TILE_SLAB.get())) {
            cut(out, cm, v, 2);
        }
        Block cd = ModBlocks.COBBLED_DEEP_MOONSTONE.get();
        cut(out, cd, ModBlocks.POLISHED_DEEP_MOONSTONE.get(), 1);
        cut(out, cd, ModBlocks.DEEP_MOONSTONE_BRICKS.get(), 1);
        cut(out, cd, ModBlocks.DEEP_MOONSTONE_TILES.get(), 1);
        for (Block v : List.of(
                ModBlocks.COBBLED_DEEP_MOONSTONE_STAIRS.get(),
                ModBlocks.COBBLED_DEEP_MOONSTONE_WALL.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_STAIRS.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_WALL.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_STAIRS.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_WALL.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_STAIRS.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_WALL.get())) {
            cut(out, cd, v, 1);
        }
        for (Block v : List.of(
                ModBlocks.COBBLED_DEEP_MOONSTONE_SLAB.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_SLAB.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_SLAB.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_SLAB.get())) {
            cut(out, cd, v, 2);
        }

        // Crafting-table stairs/slab/wall for every building variant (a non-stonecutter path, like vanilla
        // stone). Each variant's shapes come from THAT variant (cobbled → cobbled stairs, etc.), not the base.
        stoneShapes(
                out,
                ModBlocks.COBBLED_MOONSTONE.get(),
                ModBlocks.COBBLED_MOONSTONE_STAIRS.get(),
                ModBlocks.COBBLED_MOONSTONE_SLAB.get(),
                ModBlocks.COBBLED_MOONSTONE_WALL.get());
        stoneShapes(
                out,
                ModBlocks.MOONSTONE.get(),
                ModBlocks.MOONSTONE_STAIRS.get(),
                ModBlocks.MOONSTONE_SLAB.get(),
                ModBlocks.MOONSTONE_WALL.get());
        stoneShapes(
                out,
                ModBlocks.SMOOTH_MOONSTONE.get(),
                ModBlocks.SMOOTH_MOONSTONE_STAIRS.get(),
                ModBlocks.SMOOTH_MOONSTONE_SLAB.get(),
                null); // smooth has no wall
        stoneShapes(
                out,
                ModBlocks.MOONSTONE_BRICKS.get(),
                ModBlocks.MOONSTONE_BRICK_STAIRS.get(),
                ModBlocks.MOONSTONE_BRICK_SLAB.get(),
                ModBlocks.MOONSTONE_BRICK_WALL.get());
        stoneShapes(
                out,
                ModBlocks.MOONSTONE_TILES.get(),
                ModBlocks.MOONSTONE_TILE_STAIRS.get(),
                ModBlocks.MOONSTONE_TILE_SLAB.get(),
                ModBlocks.MOONSTONE_TILE_WALL.get());
        stoneShapes(
                out,
                ModBlocks.COBBLED_DEEP_MOONSTONE.get(),
                ModBlocks.COBBLED_DEEP_MOONSTONE_STAIRS.get(),
                ModBlocks.COBBLED_DEEP_MOONSTONE_SLAB.get(),
                ModBlocks.COBBLED_DEEP_MOONSTONE_WALL.get());
        stoneShapes(
                out,
                ModBlocks.POLISHED_DEEP_MOONSTONE.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_STAIRS.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_SLAB.get(),
                ModBlocks.POLISHED_DEEP_MOONSTONE_WALL.get());
        stoneShapes(
                out,
                ModBlocks.DEEP_MOONSTONE_BRICKS.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_STAIRS.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_SLAB.get(),
                ModBlocks.DEEP_MOONSTONE_BRICK_WALL.get());
        stoneShapes(
                out,
                ModBlocks.DEEP_MOONSTONE_TILES.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_STAIRS.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_SLAB.get(),
                ModBlocks.DEEP_MOONSTONE_TILE_WALL.get());
    }

    /** Crafting-table stairs (4)/slab (6)/wall (6) for a stone variant; {@code wall} may be null. */
    /** Veinstone build set (v1.4.7) — polished + bricks + shapes, crafted + stonecut like Moonstone. */
    private void buildVeinstoneRecipes(RecipeOutput out) {
        Block v = ModBlocks.VEINSTONE.get();
        Block pv = ModBlocks.POLISHED_VEINSTONE.get();
        Block vb = ModBlocks.VEINSTONE_BRICKS.get();

        // 2×2 crafting: veinstone → polished → bricks.
        square2x2(out, v, pv);
        square2x2(out, pv, vb);

        // Crafting-table stairs/slab/wall for each base.
        stoneShapes(
                out,
                v,
                ModBlocks.VEINSTONE_STAIRS.get(),
                ModBlocks.VEINSTONE_SLAB.get(),
                ModBlocks.VEINSTONE_WALL.get());
        stoneShapes(
                out,
                pv,
                ModBlocks.POLISHED_VEINSTONE_STAIRS.get(),
                ModBlocks.POLISHED_VEINSTONE_SLAB.get(),
                ModBlocks.POLISHED_VEINSTONE_WALL.get());
        stoneShapes(
                out,
                vb,
                ModBlocks.VEINSTONE_BRICK_STAIRS.get(),
                ModBlocks.VEINSTONE_BRICK_SLAB.get(),
                ModBlocks.VEINSTONE_BRICK_WALL.get());

        // Stonecutter: veinstone → every veinstone variant.
        cut(out, v, pv, 1);
        cut(out, v, vb, 1);
        for (Block s : List.of(
                ModBlocks.VEINSTONE_STAIRS.get(),
                ModBlocks.VEINSTONE_WALL.get(),
                ModBlocks.POLISHED_VEINSTONE_STAIRS.get(),
                ModBlocks.POLISHED_VEINSTONE_WALL.get(),
                ModBlocks.VEINSTONE_BRICK_STAIRS.get(),
                ModBlocks.VEINSTONE_BRICK_WALL.get())) {
            cut(out, v, s, 1);
        }
        for (Block s : List.of(
                ModBlocks.VEINSTONE_SLAB.get(),
                ModBlocks.POLISHED_VEINSTONE_SLAB.get(),
                ModBlocks.VEINSTONE_BRICK_SLAB.get())) {
            cut(out, v, s, 2);
        }
    }

    private void stoneShapes(RecipeOutput out, ItemLike base, ItemLike stairs, ItemLike slab, ItemLike wall) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', base)
                .unlockedBy("has_" + path(base), has(base))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .define('#', base)
                .unlockedBy("has_" + path(base), has(base))
                .save(out);
        if (wall != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6)
                    .pattern("###")
                    .pattern("###")
                    .define('#', base)
                    .unlockedBy("has_" + path(base), has(base))
                    .save(out);
        }
    }

    private void smelt(RecipeOutput out, ItemLike from, ItemLike to) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(from), RecipeCategory.BUILDING_BLOCKS, to, 0.1F, 200)
                .unlockedBy("has_" + path(from), has(from))
                .save(out);
    }

    private void square2x2(RecipeOutput out, ItemLike from, ItemLike to) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, to, 4)
                .pattern("##")
                .pattern("##")
                .define('#', from)
                .unlockedBy("has_" + path(from), has(from))
                .save(out);
    }

    private void cut(RecipeOutput out, ItemLike from, ItemLike to, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(from), RecipeCategory.BUILDING_BLOCKS, to, count)
                .unlockedBy("has_" + path(from), has(from))
                .save(
                        out,
                        ResourceLocation.fromNamespaceAndPath(
                                Lumenwilds.MOD_ID, path(to) + "_from_" + path(from) + "_stonecutting"));
    }

    private static String path(ItemLike item) {
        return BuiltInRegistries.BLOCK.getKey((Block) item).getPath();
    }

    /** Standard wood-set recipes for the Glowwood building blocks (Phase 4). */
    private void buildGlowwoodRecipes(RecipeOutput out) {
        buildWoodSetRecipes(
                out,
                ModBlocks.GLOWWOOD_LOG.get(),
                ModBlocks.GLOWWOOD_WOOD.get(),
                ModBlocks.STRIPPED_GLOWWOOD_LOG.get(),
                ModBlocks.STRIPPED_GLOWWOOD_WOOD.get(),
                ModBlocks.GLOWWOOD_PLANKS.get(),
                ModBlocks.GLOWWOOD_STAIRS.get(),
                ModBlocks.GLOWWOOD_SLAB.get(),
                ModBlocks.GLOWWOOD_FENCE.get(),
                ModBlocks.GLOWWOOD_FENCE_GATE.get(),
                ModBlocks.GLOWWOOD_DOOR.get(),
                ModBlocks.GLOWWOOD_TRAPDOOR.get(),
                ModBlocks.GLOWWOOD_BUTTON.get(),
                ModBlocks.GLOWWOOD_PRESSURE_PLATE.get(),
                ModItems.GLOWWOOD_SIGN.get(),
                ModItems.GLOWWOOD_HANGING_SIGN.get(),
                ModItems.GLOWWOOD_BOAT.get(),
                ModItems.GLOWWOOD_CHEST_BOAT.get());
    }

    /** Standard wood-set recipes for the Glowroot building blocks (v1.1a). */
    private void buildGlowrootRecipes(RecipeOutput out) {
        buildWoodSetRecipes(
                out,
                ModBlocks.GLOWROOT_LOG.get(),
                ModBlocks.GLOWROOT_WOOD.get(),
                ModBlocks.STRIPPED_GLOWROOT_LOG.get(),
                ModBlocks.STRIPPED_GLOWROOT_WOOD.get(),
                ModBlocks.GLOWROOT_PLANKS.get(),
                ModBlocks.GLOWROOT_STAIRS.get(),
                ModBlocks.GLOWROOT_SLAB.get(),
                ModBlocks.GLOWROOT_FENCE.get(),
                ModBlocks.GLOWROOT_FENCE_GATE.get(),
                ModBlocks.GLOWROOT_DOOR.get(),
                ModBlocks.GLOWROOT_TRAPDOOR.get(),
                ModBlocks.GLOWROOT_BUTTON.get(),
                ModBlocks.GLOWROOT_PRESSURE_PLATE.get(),
                ModItems.GLOWROOT_SIGN.get(),
                ModItems.GLOWROOT_HANGING_SIGN.get(),
                ModItems.GLOWROOT_BOAT.get(),
                ModItems.GLOWROOT_CHEST_BOAT.get());
    }

    /**
     * The full vanilla wood-set recipe shapes for one species (log→planks/wood, all plank-derived blocks,
     * signs, hanging signs, boat + chest boat). Recipe ids derive from the result item, so two species never
     * collide; criterion names are per-recipe so generic labels are fine.
     */
    private void buildWoodSetRecipes(
            RecipeOutput out,
            ItemLike log,
            ItemLike wood,
            ItemLike strippedLog,
            ItemLike strippedWood,
            ItemLike planks,
            ItemLike stairs,
            ItemLike slab,
            ItemLike fence,
            ItemLike fenceGate,
            ItemLike door,
            ItemLike trapdoor,
            ItemLike button,
            ItemLike plate,
            ItemLike sign,
            ItemLike hangingSign,
            ItemLike boat,
            ItemLike chestBoat) {
        // Planks from logs; "wood" (all-bark) from 4 logs.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .requires(log)
                .unlockedBy("has_log", has(log))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
                .pattern("##")
                .pattern("##")
                .define('#', log)
                .unlockedBy("has_log", has(log))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, strippedWood, 3)
                .pattern("##")
                .pattern("##")
                .define('#', strippedLog)
                .unlockedBy("has_stripped_log", has(strippedLog))
                .save(out);

        // Plank-derived building blocks.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .define('#', planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 3)
                .pattern("#/#")
                .pattern("#/#")
                .define('#', planks)
                .define('/', Items.STICK)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate, 1)
                .pattern("/#/")
                .pattern("/#/")
                .define('#', planks)
                .define('/', Items.STICK)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapdoor, 2)
                .pattern("###")
                .pattern("###")
                .define('#', planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button, 1)
                .requires(planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, plate, 1)
                .pattern("##")
                .define('#', planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);

        // Signs (the recipe result is the SignItem).
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3)
                .pattern("###")
                .pattern("###")
                .pattern(" / ")
                .define('#', planks)
                .define('/', Items.STICK)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSign, 6)
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .define('C', Items.CHAIN)
                .define('#', strippedLog)
                .unlockedBy("has_stripped_log", has(strippedLog))
                .save(out);

        // Boats.
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boat, 1)
                .pattern("# #")
                .pattern("###")
                .define('#', planks)
                .unlockedBy("has_planks", has(planks))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat, 1)
                .requires(Items.CHEST)
                .requires(boat)
                .unlockedBy("has_boat", has(boat))
                .save(out);
    }
}
