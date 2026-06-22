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
            if (block instanceof com.jus144tice.lumenwilds.block.AbstractFieldBlock) {
                continue; // liftshaft field — hand-authored translucent column blockstate/model
            }
            if (block instanceof net.minecraft.world.level.block.BarrelBlock) {
                continue; // hand-authored facing/open blockstate + emissive models (v1.1.3)
            }
            if (block instanceof net.minecraft.world.level.block.ChestBlock) {
                continue; // chest renders via the block-entity renderer — hand-authored particle blockstate
            }
            if (block instanceof net.minecraft.world.level.block.FarmBlock
                    || block instanceof net.minecraft.world.level.block.DirtPathBlock) {
                continue; // farmland (moisture-keyed top) + path — hand-authored 15px-top models (F1)
            }
            if (name.endsWith("_bookshelf") || name.endsWith("_ladder") || name.endsWith("_post")) {
                continue; // wood variants (v1.4.2) — hand-authored emissive bookshelf / cutout ladder / post column
            }
            if (name.equals("lumen_grass_block")) {
                // Grass: a real grass block — green top, moonloam + grass-fringe sides, moonloam bottom
                // (3-face, painted in the art pass), not the flat cube_all every other simple block gets.
                simpleBlock(
                        block,
                        models().cubeBottomTop(
                                        name,
                                        blockTex("lumen_grass_block_side"),
                                        blockTex("lumen_grass_block_bottom"),
                                        blockTex("lumen_grass_block_top")));
            } else if (block instanceof RotatedPillarBlock pillar) {
                if (name.endsWith("_wood")) {
                    // "Wood" (all-bark) reuses its log's side texture on every face (e.g. glowwood_wood →
                    // glowwood_log, stripped_glowroot_wood → stripped_glowroot_log).
                    ResourceLocation side = blockTex(name.substring(0, name.length() - "_wood".length()) + "_log");
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
            } else if (block instanceof net.minecraft.world.level.block.CropBlock crop) {
                // Crops: a per-age cutout "crop" model (block/<name>_stage<age>), keyed to the AGE property.
                int max = crop.getMaxAge();
                var ageProp = max <= 3
                        ? net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_3
                        : net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7;
                ModelFile[] stages = new ModelFile[max + 1];
                for (int a = 0; a <= max; a++) {
                    stages[a] = models().withExistingParent(name + "_stage" + a, "minecraft:block/crop")
                            .texture("crop", blockTex(name + "_stage" + a))
                            .renderType("minecraft:cutout");
                }
                getVariantBuilder(block)
                        .forAllStates(state -> net.neoforged.neoforge.client.model.generators.ConfiguredModel.builder()
                                .modelFile(stages[state.getValue(ageProp)])
                                .build());
            } else if (block instanceof net.minecraft.world.level.block.StemBlock) {
                // Gourd stem: 8 vanilla stem_growth parents keyed to AGE, textured with our stem sprite.
                ModelFile[] stages = new ModelFile[8];
                for (int a = 0; a <= 7; a++) {
                    stages[a] = models().withExistingParent(name + "_stage" + a, "minecraft:block/stem_growth" + a)
                            .texture("stem", blockTex(name))
                            .renderType("minecraft:cutout");
                }
                getVariantBuilder(block)
                        .forAllStates(s -> net.neoforged.neoforge.client.model.generators.ConfiguredModel.builder()
                                .modelFile(stages[
                                        s.getValue(
                                                net.minecraft.world.level.block.state.properties.BlockStateProperties
                                                        .AGE_7)])
                                .build());
            } else if (block instanceof net.minecraft.world.level.block.AttachedStemBlock) {
                ModelFile m = models().withExistingParent(name, "minecraft:block/stem_fruit")
                        .texture("stem", blockTex(name))
                        .renderType("minecraft:cutout");
                getVariantBuilder(block).forAllStates(s -> {
                    int rot =
                            switch (s.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING)) {
                                case WEST -> 90;
                                case NORTH -> 180;
                                case EAST -> 270;
                                default -> 0;
                            };
                    return net.neoforged.neoforge.client.model.generators.ConfiguredModel.builder()
                            .modelFile(m)
                            .rotationY(rot)
                            .build();
                });
            } else if (block instanceof net.minecraft.world.level.block.CarvedPumpkinBlock) {
                // Carved Glowgourd — a glowing face block, oriented to FACING (side/top reuse the gourd).
                ModelFile m = models().orientable(
                                name, blockTex("glowgourd_side"), blockTex(name + "_front"), blockTex("glowgourd_top"));
                horizontalBlock(block, m);
            } else if (block instanceof net.minecraft.world.level.block.SugarCaneBlock) {
                // Glimmerreed: a cutout cross (sugar-cane style), one model for all AGE states.
                simpleBlock(block, models().cross(name, blockTex(name)).renderType("minecraft:cutout"));
            } else if (name.equals("moonmelon") || name.equals("glowgourd")) {
                // Gourd blocks: a cube with distinct side + top (melon/pumpkin style).
                simpleBlock(block, models().cubeColumn(name, blockTex(name + "_side"), blockTex(name + "_top")));
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
        ResourceLocation glowwood = blockTex("glowwood_planks");
        signBlock(ModBlocks.GLOWWOOD_SIGN.get(), ModBlocks.GLOWWOOD_WALL_SIGN.get(), glowwood);
        ModelFile glowwoodHanging = models().sign("glowwood_hanging_sign", glowwood);
        simpleBlock(ModBlocks.GLOWWOOD_HANGING_SIGN.get(), glowwoodHanging);
        simpleBlock(ModBlocks.GLOWWOOD_WALL_HANGING_SIGN.get(), glowwoodHanging);

        ResourceLocation glowroot = blockTex("glowroot_planks");
        signBlock(ModBlocks.GLOWROOT_SIGN.get(), ModBlocks.GLOWROOT_WALL_SIGN.get(), glowroot);
        ModelFile glowrootHanging = models().sign("glowroot_hanging_sign", glowroot);
        simpleBlock(ModBlocks.GLOWROOT_HANGING_SIGN.get(), glowrootHanging);
        simpleBlock(ModBlocks.GLOWROOT_WALL_HANGING_SIGN.get(), glowrootHanging);
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
        if (name.startsWith("glowroot_")) {
            return blockTex("glowroot_planks");
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
