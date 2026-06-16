/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Data-generation entry point. Run with {@code ./gradlew runData}; output lands in
 * {@code src/generated/resources} (git-ignored). The hand-authored placeholder assets under
 * {@code src/main/resources} remain authoritative for the dev/runtime client, so the mod loads fine
 * with or without ever running datagen — datagen here is a regeneration/scaffolding aid.
 *
 * <p>Providers are deliberately tolerant: they emit cube_all/generated placeholders, the two required
 * recipes, drop-self loot, lang for every registered object, and basic mining tags.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class DataGenerators {

    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        // Client assets.
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));

        // Server data.
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookup));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output, lookup));
        ModTagProvider blockTags = new ModTagProvider(output, lookup, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(
                event.includeServer(),
                new ModItemTagProvider(output, lookup, blockTags.contentsGetter(), existingFileHelper));
    }
}
