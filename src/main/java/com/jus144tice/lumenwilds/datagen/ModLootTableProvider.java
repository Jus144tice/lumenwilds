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
                String name = block.builtInRegistryHolder().key().location().getPath();
                if (block instanceof SlabBlock) {
                    add(block, this::createSlabItemTable); // double slab drops 2
                } else if (block instanceof DoorBlock) {
                    add(block, this::createDoorTable); // a door is two blocks but drops one item
                } else if (name.endsWith("_wall_hanging_sign")) {
                    dropOther(block, ModItems.GLOWWOOD_HANGING_SIGN.get());
                } else if (name.endsWith("_wall_sign")) {
                    dropOther(block, ModItems.GLOWWOOD_SIGN.get());
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
                    .collect(Collectors.toList());
        }
    }
}
