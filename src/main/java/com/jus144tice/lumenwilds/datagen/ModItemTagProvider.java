/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Item tags for the Glowwood + Glowroot wood sets (v1.1.1) — the half that recipes actually read. Most are
 * mirrored from the block tags added in {@link ModTagProvider} via {@link #copy}; the shared sign items and
 * the boats are item-only, so they're added directly. Without {@code #minecraft:planks} here, the planks
 * can't be used to craft a crafting table / chest / and the dozens of other vanilla planks-tag recipes.
 */
public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Lumenwilds.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Mirror the block-tag membership onto the matching item tags (copies only our additions).
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.LOGS, ItemTags.LOGS);
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);

        // Item-only: the sign items (one item per pair) and the boats.
        tag(ItemTags.SIGNS).add(ModItems.GLOWWOOD_SIGN.get(), ModItems.GLOWROOT_SIGN.get());
        tag(ItemTags.HANGING_SIGNS).add(ModItems.GLOWWOOD_HANGING_SIGN.get(), ModItems.GLOWROOT_HANGING_SIGN.get());
        tag(ItemTags.BOATS).add(ModItems.GLOWWOOD_BOAT.get(), ModItems.GLOWROOT_BOAT.get());
        tag(ItemTags.CHEST_BOATS).add(ModItems.GLOWWOOD_CHEST_BOAT.get(), ModItems.GLOWROOT_CHEST_BOAT.get());
    }
}
