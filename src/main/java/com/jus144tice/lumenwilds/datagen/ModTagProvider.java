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
        var pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var axe = tag(BlockTags.MINEABLE_WITH_AXE);
        var shovel = tag(BlockTags.MINEABLE_WITH_SHOVEL);
        var hoe = tag(BlockTags.MINEABLE_WITH_HOE);
        var leaves = tag(BlockTags.LEAVES);
        // #minecraft:logs is what LeavesBlock's distance check reads to find a trunk; without our logs in
        // it, every leaf computes DISTANCE 7 and decays — even one touching the trunk. Keep it populated.
        var logs = tag(BlockTags.LOGS);

        // Classify by name so new stone/wood blocks are covered automatically.
        for (var holder : ModBlocks.BLOCKS.getEntries()) {
            if (holder == ModBlocks.LUMEN_PORTAL) {
                continue;
            }
            net.minecraft.world.level.block.Block block = holder.get();
            String name = holder.getId().getPath();
            // Non-exclusive: logs/wood are both axe-mineable AND #minecraft:logs (for leaf decay).
            if (name.endsWith("_log") || name.endsWith("_wood")) {
                logs.add(block);
            }
            if (name.endsWith("leaves")) {
                hoe.add(block);
                leaves.add(block);
            } else if (name.contains("glowwood") || name.contains("glowroot") || name.contains("glowcap")) {
                axe.add(block);
            } else if (name.equals("moonloam") || name.equals("lumen_grass_block") || name.equals("lumensand")) {
                shovel.add(block);
            } else if (name.contains("moonstone")
                    || name.contains("glowbrick")
                    || name.contains("luminite")
                    || name.contains("conduit")
                    || name.equals("memory_crystal")
                    || name.endsWith("_ore")
                    || name.endsWith("_cluster")
                    || name.equals("lumenbound_stone")
                    || name.equals("lumen_crystal_block")
                    || name.equals("glasspetal_block")) {
                pickaxe.add(block);
            }
            // Others (moonblossom, glowvine, lumenbulb, …) break instantly / need no mining tag yet.
        }

        // TODO (Phase 4+): needs_*_tool tiers, c: common tags via an item-tag provider, plant/vine tags.
    }
}
