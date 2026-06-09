/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.datagen;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates blockstates + block models for every Lumenwilds block, dispatching by block type so the
 * structural shapes (logs, stairs, slabs, fences, doors, …) get correct models, not cube_all. Textures
 * are flat-colour placeholders under {@code assets/lumenwilds/textures/block/}. Datagen output is OFF the
 * resource path; keep the committed assets authoritative (copy regenerated files in as needed).
 */
public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Lumenwilds.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerSigns();

        for (var holder : ModBlocks.BLOCKS.getEntries()) {
            Block block = holder.get();
            String name = holder.getId().getPath();

            if (name.endsWith("_sign")) {
                continue; // handled in registerSigns() (needs standing+wall paired)
            }
            if (block instanceof LiquidBlock) {
                continue; // fluid block — hand-authored particle-only blockstate/model
            }
            if (block instanceof RotatedPillarBlock pillar) {
                if (name.endsWith("_wood")) {
                    // "Wood" (all-bark) reuses its log's side texture on every face.
                    ResourceLocation side =
                            blockTex(name.startsWith("stripped") ? "stripped_glowwood_log" : "glowwood_log");
                    axisBlock(pillar, side, side);
                } else {
                    logBlock(pillar); // block/<name> (side) + block/<name>_top
                }
            } else if (block instanceof StairBlock stair) {
                stairsBlock(stair, baseTex(name));
            } else if (block instanceof SlabBlock slab) {
                ResourceLocation tex = baseTex(name);
                slabBlock(slab, tex, tex);
            } else if (block instanceof FenceBlock fence) {
                fenceBlock(fence, baseTex(name));
            } else if (block instanceof FenceGateBlock gate) {
                fenceGateBlock(gate, baseTex(name));
            } else if (block instanceof WallBlock wall) {
                wallBlock(wall, baseTex(name));
            } else if (block instanceof DoorBlock door) {
                doorBlockWithRenderType(door, blockTex(name + "_bottom"), blockTex(name + "_top"), "cutout");
            } else if (block instanceof TrapDoorBlock trapdoor) {
                trapdoorBlockWithRenderType(trapdoor, blockTex(name), true, "cutout");
            } else if (block instanceof ButtonBlock button) {
                buttonBlock(button, baseTex(name));
            } else if (block instanceof PressurePlateBlock plate) {
                pressurePlateBlock(plate, baseTex(name));
            } else if (block instanceof IronBarsBlock bars) {
                ResourceLocation tex = blockTex(name.endsWith("_pane") ? name.substring(0, name.length() - 5) : name);
                paneBlock(bars, tex, tex);
            } else if (block instanceof AmethystClusterBlock) {
                // Crystal clusters (Glasspetal) — a cutout cross rotated to the FACING direction.
                directionalBlock(block, models().cross(name, blockTex(name)).renderType("minecraft:cutout"));
            } else if (block instanceof BushBlock) {
                // Flowers/ferns/etc. render as a cutout cross.
                simpleBlock(block, models().cross(name, blockTex(name)).renderType("minecraft:cutout"));
            } else {
                // Cube blocks (planks, leaves, stone, ores, the portal, …) → cube_all on block/<name>.
                simpleBlock(block);
            }
        }
    }

    /** Sign blocks render via a block-entity renderer; their blockstate just points at a particle model. */
    private void registerSigns() {
        ResourceLocation particle = blockTex("glowwood_planks");
        signBlock(ModBlocks.GLOWWOOD_SIGN.get(), ModBlocks.GLOWWOOD_WALL_SIGN.get(), particle);
        ModelFile hanging = models().sign("glowwood_hanging_sign", particle);
        simpleBlock(ModBlocks.GLOWWOOD_HANGING_SIGN.get(), hanging);
        simpleBlock(ModBlocks.GLOWWOOD_WALL_HANGING_SIGN.get(), hanging);
    }

    /** Texture {@code lumenwilds:block/<id>}. */
    private ResourceLocation blockTex(String id) {
        return ResourceLocation.fromNamespaceAndPath(Lumenwilds.MOD_ID, "block/" + id);
    }

    /**
     * The base material texture a derived shape (stairs/slab/fence/wall/button/plate) is built from:
     * Glowwood shapes use {@code glowwood_planks}; stone shapes use their own base block texture.
     */
    private ResourceLocation baseTex(String name) {
        if (name.startsWith("glowwood_")) {
            return blockTex("glowwood_planks");
        }
        String base = name;
        for (String suffix :
                new String[] {"_stairs", "_slab", "_wall", "_fence_gate", "_fence", "_button", "_pressure_plate"}) {
            if (base.endsWith(suffix)) {
                base = base.substring(0, base.length() - suffix.length());
                break;
            }
        }
        // "<x>_brick"/"<x>_tile" shapes are cut from the plural "<x>_bricks"/"<x>_tiles" base texture.
        if (base.endsWith("_brick") || base.endsWith("_tile")) {
            base = base + "s";
        }
        return blockTex(base);
    }
}
