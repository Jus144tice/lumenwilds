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

        buildGlowwoodRecipes(recipeOutput);
        buildMoonstoneRecipes(recipeOutput);
        buildShimmerstoneRecipes(recipeOutput);
        buildLumenCrystalRecipes(recipeOutput);
        buildLuminiteRecipes(recipeOutput);
        buildResonanceRecipes(recipeOutput);
        buildRebuildRecipes(recipeOutput);
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
        // Planks from logs; "wood" (all-bark) from 4 logs.
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWWOOD_PLANKS.get(), 4)
                .requires(ModBlocks.GLOWWOOD_LOG.get())
                .unlockedBy("has_glowwood_log", has(ModBlocks.GLOWWOOD_LOG.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWWOOD_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', ModBlocks.GLOWWOOD_LOG.get())
                .unlockedBy("has_glowwood_log", has(ModBlocks.GLOWWOOD_LOG.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_GLOWWOOD_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', ModBlocks.STRIPPED_GLOWWOOD_LOG.get())
                .unlockedBy("has_stripped_glowwood_log", has(ModBlocks.STRIPPED_GLOWWOOD_LOG.get()))
                .save(out);

        // Plank-derived building blocks.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWWOOD_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GLOWWOOD_SLAB.get(), 6)
                .pattern("###")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.GLOWWOOD_FENCE.get(), 3)
                .pattern("#/#")
                .pattern("#/#")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .define('/', Items.STICK)
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GLOWWOOD_FENCE_GATE.get(), 1)
                .pattern("/#/")
                .pattern("/#/")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .define('/', Items.STICK)
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GLOWWOOD_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GLOWWOOD_TRAPDOOR.get(), 2)
                .pattern("###")
                .pattern("###")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.GLOWWOOD_BUTTON.get(), 1)
                .requires(ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GLOWWOOD_PRESSURE_PLATE.get(), 1)
                .pattern("##")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);

        // Signs (the recipe result is the SignItem, registered under glowwood_sign).
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.GLOWWOOD_SIGN.get(), 3)
                .pattern("###")
                .pattern("###")
                .pattern(" / ")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .define('/', Items.STICK)
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.GLOWWOOD_HANGING_SIGN.get(), 6)
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .define('C', Items.CHAIN)
                .define('#', ModBlocks.STRIPPED_GLOWWOOD_LOG.get())
                .unlockedBy("has_stripped_glowwood_log", has(ModBlocks.STRIPPED_GLOWWOOD_LOG.get()))
                .save(out);

        // Boats.
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModItems.GLOWWOOD_BOAT.get(), 1)
                .pattern("# #")
                .pattern("###")
                .define('#', ModBlocks.GLOWWOOD_PLANKS.get())
                .unlockedBy("has_glowwood_planks", has(ModBlocks.GLOWWOOD_PLANKS.get()))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModItems.GLOWWOOD_CHEST_BOAT.get(), 1)
                .requires(Items.CHEST)
                .requires(ModItems.GLOWWOOD_BOAT.get())
                .unlockedBy("has_glowwood_boat", has(ModItems.GLOWWOOD_BOAT.get()))
                .save(out);
    }
}
