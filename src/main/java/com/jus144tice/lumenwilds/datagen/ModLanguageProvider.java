/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * Generates {@code en_us} display names for every registered block and item, plus the creative tab
 * title. Names are derived from the registry path (snake_case → Title Case), so new content is covered
 * automatically.
 */
public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output) {
        super(output, Lumenwilds.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.lumenwilds", Lumenwilds.MOD_NAME);

        // Block display names (BlockItems use the block translation key).
        ModBlocks.BLOCKS
                .getEntries()
                .forEach(block -> add(block.get(), titleCase(block.getId().getPath())));

        // Standalone (non-block) item names.
        ModItems.ITEMS.getEntries().stream()
                .filter(item -> !(item.get() instanceof BlockItem))
                .forEach(item -> add(item.get(), titleCase(item.getId().getPath())));
    }

    private static String titleCase(String path) {
        return Arrays.stream(path.split("_"))
                .map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
