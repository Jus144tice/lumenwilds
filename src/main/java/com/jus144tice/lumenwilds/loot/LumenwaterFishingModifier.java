/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/**
 * Lumenwater fishing (v1.1.3). When the {@code conditions} pass (the bobber is in Lumenwater in the
 * Lumenwilds — a {@code location_check}), this <b>replaces</b> the vanilla fishing catch entirely with a
 * roll of our own {@code table}, so you never reel in earth fish (cod/salmon/pufferfish). The replacement
 * table still includes treasure (enchanted rods/bows/books) and the fished spell-books.
 *
 * <p>Mirrors {@code neoforge:add_table} but clears the generated loot first (add → replace).</p>
 */
public class LumenwaterFishingModifier extends LootModifier {

    public static final MapCodec<LumenwaterFishingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(m -> m.table))
            .apply(inst, LumenwaterFishingModifier::new));

    private final ResourceKey<LootTable> table;

    public LumenwaterFishingModifier(LootItemCondition[] conditions, ResourceKey<LootTable> table) {
        super(conditions);
        this.table = table;
    }

    @SuppressWarnings("deprecation") // getRandomItemsRaw is the internal API NeoForge's own AddTableLootModifier uses
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.clear(); // discard the vanilla catch — replace with the native Lumenwater table
        context.getResolver().get(Registries.LOOT_TABLE, this.table).ifPresent(t -> t.value()
                .getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add)));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
