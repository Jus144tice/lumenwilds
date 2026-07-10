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
                } else if (name.equals("lumengrain_crop")) {
                    add(
                            block,
                            lumenCropDrops(
                                    block,
                                    ModItems.LUMENGRAIN.get(),
                                    ModItems.LUMENGRAIN_SEEDS.get(),
                                    matureCond(block, net.minecraft.world.level.block.CropBlock.AGE, 7)));
                } else if (name.equals("glimmerroot_crop")) {
                    // Glimmerroot: the root is both produce and seed (like carrots).
                    add(
                            block,
                            lumenCropDrops(
                                    block,
                                    ModItems.GLIMMERROOT.get(),
                                    ModItems.GLIMMERROOT.get(),
                                    matureCond(block, net.minecraft.world.level.block.CropBlock.AGE, 7)));
                } else if (name.equals("moonbeet_crop")) {
                    add(
                            block,
                            lumenCropDrops(
                                    block,
                                    ModItems.MOONBEET.get(),
                                    ModItems.MOONBEET_SEEDS.get(),
                                    matureCond(block, com.jus144tice.lumenwilds.block.MoonbeetCropBlock.AGE, 3)));
                } else if (name.equals("glimmerreed")) {
                    dropOther(block, ModItems.GLIMMERREED.get()); // cane drops the reed item
                } else if (name.equals("duskbean_crop")) {
                    add(
                            block,
                            lumenCropDrops(
                                    block,
                                    ModItems.DUSKBEAN.get(),
                                    ModItems.DUSKBEAN.get(),
                                    matureCond(block, com.jus144tice.lumenwilds.block.DuskbeanCropBlock.AGE, 3)));
                } else if (name.equals("cavecap_crop")) {
                    add(
                            block,
                            lumenCropDrops(
                                    block,
                                    ModItems.CAVECAP.get(),
                                    ModItems.CAVECAP.get(),
                                    matureCond(block, com.jus144tice.lumenwilds.block.CavecapCropBlock.AGE, 3)));
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
                } else if (block instanceof net.minecraft.world.level.block.StemBlock) {
                    Item seed = name.startsWith("moonmelon")
                            ? ModItems.MOONMELON_SEEDS.get()
                            : ModItems.GLOWGOURD_SEEDS.get();
                    add(block, createStemDrops(block, seed));
                } else if (block instanceof net.minecraft.world.level.block.AttachedStemBlock) {
                    Item seed = name.startsWith("moonmelon")
                            ? ModItems.MOONMELON_SEEDS.get()
                            : ModItems.GLOWGOURD_SEEDS.get();
                    add(block, createAttachedStemDrops(block, seed));
                } else if (name.equals("moonmelon")) {
                    add(
                            block,
                            createSingleItemTable(
                                    ModItems.MOONMELON_SLICE.get(),
                                    net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between(
                                            3.0F, 7.0F)));
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
                    // Glowroot leaves also drop the occasional Lumen Fruit (apple-analog → renewable food).
                    if (name.startsWith("glowroot")) {
                        add(
                                block,
                                b -> lumenLeavesWithFruit(
                                        b, ModBlocks.GLOWROOT_SAPLING.get(), ModItems.LUMEN_FRUIT.get()));
                    } else {
                        add(
                                block,
                                b -> createLeavesDrops(
                                        b, ModBlocks.GLOWWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
                    }
                } else {
                    dropSelf(block); // standing/ceiling signs drop their own (Sign/HangingSign)Item
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return lootableBlocks();
        }

        /**
         * A gentler crop drop than vanilla's {@code createCropDrops}: a mature harvest yields 1 produce + exactly
         * ONE seed (guaranteed — a farm is always replantable) plus a Fortune bonus, i.e. a clean ~1:1
         * seed:produce, instead of vanilla's binomial ~1.7 seeds that felt too prolific (v1.5.0). For self-seeding
         * crops (produce == seed) it yields ~2 of the item, so you replant one and keep one.
         */
        private net.minecraft.world.level.storage.loot.LootTable.Builder lumenCropDrops(
                Block crop,
                net.minecraft.world.level.ItemLike produce,
                net.minecraft.world.level.ItemLike seed,
                net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder mature) {
            var fortune = this.registries
                    .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                    .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.FORTUNE);
            return this.applyExplosionDecay(
                    crop,
                    net.minecraft.world.level.storage.loot.LootTable.lootTable()
                            .withPool(net.minecraft.world.level.storage.loot.LootPool.lootPool()
                                    .add(net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(produce)
                                            .when(mature)
                                            .otherwise(
                                                    net.minecraft.world.level.storage.loot.entries.LootItem
                                                            .lootTableItem(seed))))
                            .withPool(net.minecraft.world.level.storage.loot.LootPool.lootPool()
                                    .when(mature)
                                    .add(net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(seed)
                                            .apply(net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
                                                    .addUniformBonusCount(fortune)))));
        }

        /**
         * Glowroot leaves also drop the occasional <b>Lumen Fruit</b> — the apple-analog, so the night-vision food
         * is renewable by growing Glowroot trees rather than chest-loot only (v1.5.0). Same rarity curve as oak
         * apples; only when NOT shearing/silk-touching.
         */
        private net.minecraft.world.level.storage.loot.LootTable.Builder lumenLeavesWithFruit(
                Block leaves, Block sapling, net.minecraft.world.level.ItemLike fruit) {
            var fortune = this.registries
                    .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                    .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.FORTUNE);
            return this.createLeavesDrops(leaves, sapling, NORMAL_LEAVES_SAPLING_CHANCES)
                    .withPool(net.minecraft.world.level.storage.loot.LootPool.lootPool()
                            .setRolls(
                                    net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly(1.0F))
                            .when(HAS_SHEARS.or(this.hasSilkTouch()).invert())
                            .add(this.applyExplosionCondition(
                                            leaves,
                                            net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem(
                                                    fruit))
                                    .when(net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
                                            .bonusLevelFlatChance(
                                                    fortune, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
        }

        /** "Crop is at its max age" loot condition (for lumenCropDrops). */
        private static net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder matureCond(
                Block crop, net.minecraft.world.level.block.state.properties.IntegerProperty age, int max) {
            return net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
                    .hasBlockStateProperties(crop)
                    .setProperties(net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties()
                            .hasProperty(age, max));
        }

        private static List<Block> lootableBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream()
                    .map(holder -> (Block) holder.get())
                    .filter(block -> block != ModBlocks.LUMEN_PORTAL.get())
                    .filter(block -> block != ModBlocks.LUMENWATER_BLOCK.get()) // noLootTable fluid block
                    .filter(block -> block != ModBlocks.ASCENSION_FIELD.get())
                    .filter(block -> block != ModBlocks.DESCENT_FIELD.get()) // noLootTable liftshaft fields
                    .filter(block -> block != ModBlocks.BUDDING_LUMEN_CRYSTAL.get()) // noLootTable (un-harvestable)
                    // Lumenberry Bush has a hand-authored, age-conditioned berry loot table (v1.1c).
                    .filter(block -> block != ModBlocks.LUMENBERRY_BUSH.get())
                    // Wall torch drops the standing torch via lootFrom (no own table), like vanilla (v1.4.7).
                    .filter(block -> block != ModBlocks.EMBERGLOW_WALL_TORCH.get())
                    .collect(Collectors.toList());
        }
    }
}
