/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates the vanilla mining tags so the placeholder blocks are harvestable with the right tool
 * (and, where {@code requiresCorrectToolForDrops()} is set, actually drop). Block tags only for now;
 * add an item-tag provider when item tags (e.g. {@code c:} common tags) are needed.
 */
public class ModTagProvider extends BlockTagsProvider {

    public ModTagProvider(
            PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        super(output, lookupProvider, Lumenwilds.MOD_ID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        ModBlocks.LUMENBOUND_STONE.get(),
                        ModBlocks.MOONSTONE.get(),
                        ModBlocks.COBBLED_MOONSTONE.get(),
                        ModBlocks.LUMEN_CRYSTAL_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
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
                        ModBlocks.GLOWROOT_LOG.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.MOONLOAM.get(), ModBlocks.LUMEN_GRASS_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.GLOWWOOD_LEAVES.get());
        tag(BlockTags.LEAVES).add(ModBlocks.GLOWWOOD_LEAVES.get());

        // TODO (Phase 4+): needs_*_tool tiers, c: common tags via an item-tag provider, plant/vine tags.
    }
}
