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
    }
}
