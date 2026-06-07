/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates simple {@code cube_all} blockstates + block models for every Lumenwilds block. Placeholder
 * only — each block points at a {@code block/<name>} texture (flat-colour placeholders ship in
 * {@code assets/lumenwilds/textures/block/}). Replace with bespoke models/textures later.
 */
public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Lumenwilds.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Every block (including the portal interior) gets a placeholder cube_all model so nothing
        // renders as a missing model. TODO: portal/plant/log blocks deserve real, non-cube models.
        for (var block : ModBlocks.BLOCKS.getEntries()) {
            simpleBlockWithItem(block.get());
        }
    }

    private void simpleBlockWithItem(Block block) {
        simpleBlock(block); // cube_all blockstate + model using texture block/<name>
    }
}
