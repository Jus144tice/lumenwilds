/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.loot.LumenwaterFishingModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Global Loot Modifier serializers (v1.1.3). Registers the {@link LumenwaterFishingModifier} codec so the
 * {@code data/lumenwilds/loot_modifiers/lumenwater_fishing.json} modifier (indexed in
 * {@code data/neoforge/loot_modifiers/global_loot_modifiers.json}) can resolve its type.
 */
public final class ModLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Lumenwilds.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<LumenwaterFishingModifier>>
            LUMENWATER_FISHING =
                    LOOT_MODIFIER_SERIALIZERS.register("lumenwater_fishing", () -> LumenwaterFishingModifier.CODEC);

    private ModLootModifiers() {}
}
