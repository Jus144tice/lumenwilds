/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

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
    }
}
