/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import java.util.function.Supplier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Tool material tiers for the Lumenwilds gear sets (v1.2). The dimension gets its own stone→iron tool
 * progression so a player can outfit themselves in-world without hauling Overworld tools:
 *
 * <ul>
 *   <li>{@link #MOONSTONE} — <b>stone-tier</b> (reuses {@code INCORRECT_FOR_STONE_TOOL}, so it mines exactly
 *       what a stone pickaxe does). Slightly more durable than stone (165 vs 131) to feel a touch special;
 *       crafted from / repaired with {@code Cobbled Moonstone}, the dimension's cobble analog.</li>
 *   <li>{@link #LUMINITE} — <b>iron-tier</b> (reuses {@code INCORRECT_FOR_IRON_TOOL}; mines the dimension's
 *       ores + crystal/metal blocks, tagged {@code #minecraft:needs_iron_tool} in {@code ModTagProvider}).
 *       Repaired with {@code Luminite Ingot}.</li>
 * </ul>
 *
 * <p>{@link Tier} is a tiny 6-method interface; we implement it with the {@link SimpleTier} record rather
 * than depending on a platform helper.</p>
 */
public final class ModToolTiers {

    public static final Tier MOONSTONE = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            165,
            4.0F,
            1.0F,
            5,
            () -> Ingredient.of(ModBlocks.COBBLED_MOONSTONE.get()));

    public static final Tier LUMINITE = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 300, 6.0F, 2.0F, 14, () -> Ingredient.of(ModItems.LUMINITE_INGOT.get()));

    /**
     * {@link #RESONITE} — the dimension's <b>top tier</b> (v1.3 Phase D), the deep's chase reward. Diamond-
     * mining-capable ({@code INCORRECT_FOR_DIAMOND_TOOL}); durability between diamond (1561) and below netherite,
     * a touch faster than diamond, high enchantability. Mined as the rarest, deepest ore (Resonite) and repaired
     * with {@code Resonite Ingot}.
     */
    public static final Tier RESONITE = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            900,
            8.0F,
            3.0F,
            18,
            () -> Ingredient.of(ModItems.RESONITE_INGOT.get()));

    private ModToolTiers() {}

    /** Minimal {@link Tier} implementation (the repair ingredient is built lazily — items register first). */
    private record SimpleTier(
            TagKey<Block> incorrectBlocks,
            int uses,
            float speed,
            float attackDamageBonus,
            int enchantmentValue,
            Supplier<Ingredient> repairIngredient)
            implements Tier {

        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public float getSpeed() {
            return speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamageBonus;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return incorrectBlocks;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repairIngredient.get();
        }
    }
}
