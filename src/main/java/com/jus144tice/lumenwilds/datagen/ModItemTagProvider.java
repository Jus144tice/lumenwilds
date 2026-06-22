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

        // Tools (v1.2) — the vanilla type tags (read by recipes/other mods) + the enchantability tags so the
        // Moonstone/Luminite tools enchant exactly like their vanilla counterparts. Mirrors vanilla membership:
        // all are durability/vanishing-enchantable; mining tools get mining (+ pickaxe/shovel get mining_loot
        // for Fortune/Silk Touch); axes + swords get the weapon enchants; swords get sword/fire_aspect.
        var pickaxes = new net.minecraft.world.item.Item[] {
            ModItems.MOONSTONE_PICKAXE.get(), ModItems.LUMINITE_PICKAXE.get(), ModItems.RESONITE_PICKAXE.get()
        };
        var axes = new net.minecraft.world.item.Item[] {
            ModItems.MOONSTONE_AXE.get(), ModItems.LUMINITE_AXE.get(), ModItems.RESONITE_AXE.get()
        };
        var shovels = new net.minecraft.world.item.Item[] {
            ModItems.MOONSTONE_SHOVEL.get(), ModItems.LUMINITE_SHOVEL.get(), ModItems.RESONITE_SHOVEL.get()
        };
        var hoes = new net.minecraft.world.item.Item[] {
            ModItems.MOONSTONE_HOE.get(), ModItems.LUMINITE_HOE.get(), ModItems.RESONITE_HOE.get()
        };
        var swords = new net.minecraft.world.item.Item[] {
            ModItems.MOONSTONE_SWORD.get(), ModItems.LUMINITE_SWORD.get(), ModItems.RESONITE_SWORD.get()
        };

        tag(ItemTags.PICKAXES).add(pickaxes);
        tag(ItemTags.AXES).add(axes);
        tag(ItemTags.SHOVELS).add(shovels);
        tag(ItemTags.HOES).add(hoes);
        tag(ItemTags.SWORDS).add(swords);

        var miners = concat(pickaxes, axes, shovels, hoes); // mining tools
        var allTools = concat(miners, swords);
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(allTools);
        tag(ItemTags.VANISHING_ENCHANTABLE).add(allTools);

        tag(ItemTags.MINING_ENCHANTABLE).add(miners);
        tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(concat(pickaxes, shovels));
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(concat(swords, axes));
        tag(ItemTags.WEAPON_ENCHANTABLE).add(concat(swords, axes));
        tag(ItemTags.SWORD_ENCHANTABLE).add(swords);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).add(swords);

        // Armor sets (Luminite v1.4.1, Resonite v1.3 D2) — slot + enchantability tags, like vanilla armor.
        armorTags(
                ModItems.LUMINITE_HELMET.get(),
                ModItems.LUMINITE_CHESTPLATE.get(),
                ModItems.LUMINITE_LEGGINGS.get(),
                ModItems.LUMINITE_BOOTS.get());
        armorTags(
                ModItems.RESONITE_HELMET.get(),
                ModItems.RESONITE_CHESTPLATE.get(),
                ModItems.RESONITE_LEGGINGS.get(),
                ModItems.RESONITE_BOOTS.get());
    }

    private void armorTags(
            net.minecraft.world.item.Item helmet,
            net.minecraft.world.item.Item chest,
            net.minecraft.world.item.Item legs,
            net.minecraft.world.item.Item boots) {
        var all = new net.minecraft.world.item.Item[] {helmet, chest, legs, boots};
        tag(ItemTags.HEAD_ARMOR).add(helmet);
        tag(ItemTags.CHEST_ARMOR).add(chest);
        tag(ItemTags.LEG_ARMOR).add(legs);
        tag(ItemTags.FOOT_ARMOR).add(boots);
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(helmet);
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(chest);
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(legs);
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(boots);
        tag(ItemTags.ARMOR_ENCHANTABLE).add(all);
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE).add(all);
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(all);
        tag(ItemTags.VANISHING_ENCHANTABLE).add(all);
    }

    private static net.minecraft.world.item.Item[] concat(net.minecraft.world.item.Item[]... groups) {
        int n = 0;
        for (var g : groups) {
            n += g.length;
        }
        var out = new net.minecraft.world.item.Item[n];
        int i = 0;
        for (var g : groups) {
            System.arraycopy(g, 0, out, i, g.length);
            i += g.length;
        }
        return out;
    }
}
