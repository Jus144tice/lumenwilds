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

        // Harvest TIERS (v1.2) — the blocks above all set requiresCorrectToolForDrops(), but without a tier
        // tag the game (and every harvest-HUD/tooltip mod, which reads these same tags) treats EVERY tier as
        // correct → a wooden pickaxe harvests everything. Vanilla's #incorrect_for_*_tool tags reference these
        // #needs_*_tool tags, so populating them gates mining + makes harvest-level mods report correctly.
        // Stone-tier = the dimension's basic stone/build family; iron-tier = its valuable ores + metal/crystal
        // blocks (Lumenwilds: Moonstone tools mine the former, Luminite tools the latter).
        var needsStone = tag(BlockTags.NEEDS_STONE_TOOL);
        var needsIron = tag(BlockTags.NEEDS_IRON_TOOL);

        // Glowvine is climbable like vines/weeping vines (the hanging cave strands + surface vine).
        tag(BlockTags.CLIMBABLE).add(ModBlocks.GLOWVINE.get());

        // Crops (v1.4 Farming): #minecraft:crops (some harvest/farm mods read it) + #maintains_farmland (so
        // farmland under an unattended crop doesn't revert to Moonloam when dry). Populated in the loop below.
        var crops = tag(BlockTags.CROPS);
        var maintainsFarmland = tag(BlockTags.MAINTAINS_FARMLAND);

        // Vanilla wood-set block tags (v1.1.1) — so the Glowwood + Glowroot sets behave as real wood
        // (#minecraft:planks → crafting table/chest/etc., fence/door/sign/button/plate behaviour, burnable
        // logs, saplings). The matching ITEM tags (what recipes read) are mirrored in ModItemTagProvider.
        var planks = tag(BlockTags.PLANKS);
        var woodenSlabs = tag(BlockTags.WOODEN_SLABS);
        var woodenStairs = tag(BlockTags.WOODEN_STAIRS);
        var woodenFences = tag(BlockTags.WOODEN_FENCES);
        var fenceGates = tag(BlockTags.FENCE_GATES);
        var woodenDoors = tag(BlockTags.WOODEN_DOORS);
        var woodenTrapdoors = tag(BlockTags.WOODEN_TRAPDOORS);
        var woodenButtons = tag(BlockTags.WOODEN_BUTTONS);
        var woodenPlates = tag(BlockTags.WOODEN_PRESSURE_PLATES);
        var standingSigns = tag(BlockTags.STANDING_SIGNS);
        var wallSigns = tag(BlockTags.WALL_SIGNS);
        var ceilingHangingSigns = tag(BlockTags.CEILING_HANGING_SIGNS);
        var wallHangingSigns = tag(BlockTags.WALL_HANGING_SIGNS);
        var logsThatBurn = tag(BlockTags.LOGS_THAT_BURN);
        var saplings = tag(BlockTags.SAPLINGS);

        // Classify by name so new stone/wood blocks are covered automatically.
        for (var holder : ModBlocks.BLOCKS.getEntries()) {
            if (holder == ModBlocks.LUMEN_PORTAL) {
                continue;
            }
            net.minecraft.world.level.block.Block block = holder.get();
            String name = holder.getId().getPath();
            // Non-exclusive: crops join #minecraft:crops + #maintains_farmland (auto-covers every CropBlock).
            if (block instanceof net.minecraft.world.level.block.CropBlock) {
                crops.add(block);
                maintainsFarmland.add(block);
            }
            // Non-exclusive: logs/wood are both axe-mineable AND #minecraft:logs (for leaf decay).
            if (name.endsWith("_log") || name.endsWith("_wood")) {
                logs.add(block);
            }

            // Vanilla wood-set membership for the Glowwood + Glowroot species (NOT the stone families or
            // glowcap mushroom blocks, which is why this gates on the species name, not the block type).
            if (name.contains("glowwood") || name.contains("glowroot")) {
                if (name.endsWith("_planks")) {
                    planks.add(block);
                } else if (name.endsWith("_wall_hanging_sign")) {
                    wallHangingSigns.add(block);
                } else if (name.endsWith("_hanging_sign")) {
                    ceilingHangingSigns.add(block);
                } else if (name.endsWith("_wall_sign")) {
                    wallSigns.add(block);
                } else if (name.endsWith("_sign")) {
                    standingSigns.add(block);
                } else if (name.endsWith("_slab")) {
                    woodenSlabs.add(block);
                } else if (name.endsWith("_stairs")) {
                    woodenStairs.add(block);
                } else if (name.endsWith("_fence_gate")) {
                    fenceGates.add(block);
                } else if (name.endsWith("_fence")) {
                    woodenFences.add(block);
                } else if (name.endsWith("_door")) {
                    woodenDoors.add(block);
                } else if (name.endsWith("_trapdoor")) {
                    woodenTrapdoors.add(block);
                } else if (name.endsWith("_button")) {
                    woodenButtons.add(block);
                } else if (name.endsWith("_pressure_plate")) {
                    woodenPlates.add(block);
                } else if (name.endsWith("_sapling")) {
                    saplings.add(block);
                }
                if (name.endsWith("_log") || name.endsWith("_wood")) {
                    logsThatBurn.add(block);
                }
            }
            if (name.endsWith("leaves")) {
                hoe.add(block);
                leaves.add(block);
            } else if (name.contains("glowwood") || name.contains("glowroot") || name.contains("glowcap")) {
                axe.add(block);
            } else if (name.equals("moonloam")
                    || name.equals("lumen_grass_block")
                    || name.equals("lumensand")
                    || name.equals("lumen_farmland")
                    || name.equals("lumen_dirt_path")) {
                shovel.add(block);
            } else if (name.contains("moonstone")
                    || name.contains("glowbrick")
                    || name.contains("luminite")
                    || name.contains("conduit")
                    || name.contains("resonance")
                    || name.contains("gravity_lens")
                    || name.equals("gravity_repeater")
                    || name.equals("lumen_field_projector")
                    || name.contains("light_engine")
                    || name.equals("lumen_relay")
                    || name.equals("memory_crystal")
                    || name.equals("ancient_door")
                    || name.endsWith("_ore")
                    || name.endsWith("_cluster")
                    || name.equals("lumenbound_stone")
                    || name.equals("lumen_crystal_block")
                    || name.equals("glasspetal_block")
                    || name.equals("veinstone")
                    || name.equals("pale_tuff")
                    || name.contains("emberglow")
                    || name.contains("pale_opal")
                    || name.contains("resonite")
                    || name.contains("lumen_crystal")) { // incl. budding + buds + cluster (E1 geode)
                pickaxe.add(block);
                // Iron-tier = the dimension's valuable resources (its ores + crystal/metal storage blocks);
                // everything else pickaxe-mineable is stone-tier (the basic build/stone families).
                // Exception: Emberglow (the fuel ore) is a cheap, stone-tier mine like coal.
                if (name.contains("emberglow")) {
                    needsStone.add(block);
                } else if (name.endsWith("_ore")
                        || name.equals("lumen_crystal_block")
                        || name.equals("luminite_block")
                        || name.equals("pale_opal_block")
                        || name.equals("resonite_block")) {
                    needsIron.add(block);
                } else {
                    needsStone.add(block);
                }
            }
            // Others (moonblossom, glowvine, lumenbulb, …) break instantly / need no mining tag yet.
        }
    }
}
