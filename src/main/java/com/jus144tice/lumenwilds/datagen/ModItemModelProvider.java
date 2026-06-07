/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModItems;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates item models: standalone items use {@code item/generated} (flat {@code item/<name>}
 * texture); block items inherit their block's {@code cube_all} model. Placeholder only.
 */
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Lumenwilds.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (var entry : ModItems.ITEMS.getEntries()) {
            Item item = entry.get();
            String name = entry.getId().getPath();
            if (item instanceof BlockItem) {
                // Reuse the block's generated cube_all model.
                withExistingParent(name, ResourceLocationHelper.modLoc("block/" + name));
            } else {
                basicItem(item); // item/generated with layer0 = item/<name>
            }
        }
    }
}
