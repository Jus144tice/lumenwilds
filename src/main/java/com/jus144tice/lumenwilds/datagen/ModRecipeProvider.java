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
