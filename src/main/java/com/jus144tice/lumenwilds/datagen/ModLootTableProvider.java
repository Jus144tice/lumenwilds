/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

/**
 * Generates simple "drop yourself" loot tables for every mineable Lumenwilds block. The portal
 * interior ({@link ModBlocks#LUMEN_PORTAL}) is excluded — it has {@code noLootTable()} and is never
 * obtained as an item.
 *
 * <p>TODO: silk-touch / fortune behaviour, ore-style drops (e.g. crystal block → shards), leaf/plant
 * drops once those blocks become non-cube.</p>
 */
public final class ModLootTableProvider {

    private ModLootTableProvider() {}

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)),
                registries);
    }

    private static final class ModBlockLoot extends BlockLootSubProvider {

        private ModBlockLoot(HolderLookup.Provider registries) {
            super(Set.<Item>of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            for (Block block : lootableBlocks()) {
                String name = net.minecraft.core.registries.BuiltInRegistries.BLOCK
                        .getKey(block)
                        .getPath();
                if (block instanceof SlabBlock) {
                    add(block, this::createSlabItemTable); // double slab drops 2
                } else if (block instanceof DoorBlock) {
                    add(block, this::createDoorTable); // a door is two blocks but drops one item
                } else if (name.endsWith("_wall_hanging_sign")) {
                    dropOther(
                            block,
                            name.startsWith("glowroot")
                                    ? ModItems.GLOWROOT_HANGING_SIGN.get()
                                    : ModItems.GLOWWOOD_HANGING_SIGN.get());
                } else if (name.endsWith("_wall_sign")) {
                    dropOther(
                            block,
                            name.startsWith("glowroot") ? ModItems.GLOWROOT_SIGN.get() : ModItems.GLOWWOOD_SIGN.get());
                } else if (name.equals("lumen_grass_block")) {
                    // Grass mechanic: silk → Lumen Grass, otherwise → Moonloam (like vanilla grass → dirt).
                    add(block, b -> createSingleItemTableWithSilkTouch(b, ModBlocks.MOONLOAM.get()));
                } else if (name.equals("lumen_farmland") || name.equals("lumen_dirt_path")) {
                    // Farmland/path drop Moonloam when broken (like vanilla farmland/path → dirt).
                    add(block, b -> createSingleItemTable(ModBlocks.MOONLOAM.get()));
                } else if (name.equals("moonstone")) {
                    // Stone analog: silk → Moonstone, else → Cobbled Moonstone (smelt back to Moonstone).
                    add(block, b -> createSingleItemTableWithSilkTouch(b, ModBlocks.COBBLED_MOONSTONE.get()));
                } else if (name.equals("deep_moonstone")) {
                    // Deepslate analog: silk → Deep Moonstone, else → Cobbled Deep Moonstone.
                    add(block, b -> createSingleItemTableWithSilkTouch(b, ModBlocks.COBBLED_DEEP_MOONSTONE.get()));
                } else if (name.equals("memory_crystal")) {
                    add(block, b -> createOreDrop(b, ModItems.MEMORY_CRYSTAL_SHARD.get())); // silk → block, else shard
                } else if (name.equals("cracked_gravity_lens")) {
                    add(
                            block,
                            b -> createOreDrop(b, ModItems.GRAVITY_LENS_FRAGMENT.get())); // silk → block, else fragment
                } else if (name.equals("lumen_crystal_cluster")) {
                    // Geode cluster (E1): silk → the cluster, else Lumen Crystal Shards (+fortune) — renewable.
                    add(block, b -> createOreDrop(b, ModItems.LUMEN_CRYSTAL_SHARD.get()));
                } else if (name.endsWith("_lumen_crystal_bud")) {
                    add(block, this::createSilkTouchOnlyTable); // buds only drop with Silk Touch (like amethyst)
                } else if (block instanceof DropExperienceBlock) {
                    // Route each ore to its drop by name (all +silk/fortune via createOreDrop).
                    Item drop;
                    if (name.contains("emberglow")) {
                        drop = ModItems.EMBERGLOW.get();
                    } else if (name.contains("pale_opal")) {
                        drop = ModItems.PALE_OPAL.get();
                    } else if (name.contains("resonite")) {
                        drop = ModItems.RAW_RESONITE.get();
                    } else if (name.contains("luminite")) {
                        drop = ModItems.RAW_LUMINITE.get();
                    } else {
                        drop = ModItems.LUMEN_CRYSTAL_SHARD.get();
                    }
                    add(block, b -> createOreDrop(b, drop));
                } else if (block instanceof net.minecraft.world.level.block.LeavesBlock) {
                    // Real leaves loot (NOT drop-self): shears/silk → block, else sapling/stick/mostly nothing.
                    // Drop-self on decaying leaves floods the world with leaf-block items (see CLAUDE.md).
                    Block sapling = name.startsWith("glowwood")
                            ? ModBlocks.GLOWWOOD_SAPLING.get()
                            : ModBlocks.GLOWROOT_SAPLING.get();
                    add(block, b -> createLeavesDrops(b, sapling, NORMAL_LEAVES_SAPLING_CHANCES));
                } else {
                    dropSelf(block); // standing/ceiling signs drop their own (Sign/HangingSign)Item
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return lootableBlocks();
        }

        private static List<Block> lootableBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream()
                    .map(holder -> (Block) holder.get())
                    .filter(block -> block != ModBlocks.LUMEN_PORTAL.get())
                    .filter(block -> block != ModBlocks.LUMENWATER_BLOCK.get()) // noLootTable fluid block
                    .filter(block -> block != ModBlocks.ASCENSION_FIELD.get())
                    .filter(block -> block != ModBlocks.DESCENT_FIELD.get()) // noLootTable liftshaft fields
                    .filter(block -> block != ModBlocks.BUDDING_LUMEN_CRYSTAL.get()) // noLootTable (un-harvestable)
                    // Glowberry Bush has a hand-authored, age-conditioned berry loot table (v1.1c).
                    .filter(block -> block != ModBlocks.GLOWBERRY_BUSH.get())
                    .collect(Collectors.toList());
        }
    }
}
