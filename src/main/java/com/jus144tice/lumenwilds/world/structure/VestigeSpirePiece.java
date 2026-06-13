/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModStructures;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * A Vestige Spire (Phase 10g) — a broken Lumenwright tower, the "spire district" of a Grand Vestige City. A
 * tapering glowbrick shaft whose upper floors are increasingly shattered, around an exposed Lumen-Crystal core,
 * with stair fragments jutting from the walls, a few floating debris chunks near the top, and a Spire Chest at
 * the base. Added by {@link VestigeCityStructure} (one or two per grand city) and rooted to the ground with a
 * foundation. Decayed via the shared {@link VestigeDecay} processors.
 */
public class VestigeSpirePiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/spire"));

    private static final int MAX_HEIGHT = 32;
    private static final int MAX_REACH = 8;
    private static final int FOUNDATION = 18;

    private final BlockPos origin;

    public VestigeSpirePiece(BlockPos origin) {
        super(ModStructures.VESTIGE_SPIRE_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public VestigeSpirePiece(CompoundTag tag) {
        super(ModStructures.VESTIGE_SPIRE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - MAX_REACH,
                o.getY() - FOUNDATION,
                o.getZ() - MAX_REACH,
                o.getX() + MAX_REACH,
                o.getY() + MAX_HEIGHT,
                o.getZ() + MAX_REACH);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("ox", origin.getX());
        tag.putInt("oy", origin.getY());
        tag.putInt("oz", origin.getZ());
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox writeBox,
            ChunkPos chunkPos,
            BlockPos pos) {
        RandomSource rand = RandomSource.create(
                origin.getX() * 341873128712L ^ origin.getZ() * 132897987541L ^ (long) origin.getY());

        int height = 18 + rand.nextInt(12); // 18–29
        int baseR = 3;
        BlockState core = ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState();
        BlockState floorTile = ModBlocks.GLOWBRICK_TILES.get().defaultBlockState();

        // Foundation under the base ring so the tower roots into the ground.
        for (int dx = -baseR; dx <= baseR; dx++) {
            for (int dz = -baseR; dz <= baseR; dz++) {
                if (dx * dx + dz * dz <= baseR * baseR + 1) {
                    VestigeDecay.fillFoundation(
                            level,
                            writeBox,
                            origin.getX() + dx,
                            origin.getY(),
                            origin.getZ() + dz,
                            ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                            FOUNDATION);
                }
            }
        }

        // The shaft: hollow rings, more broken with height; an exposed crystal core column.
        for (int h = 0; h <= height; h++) {
            float t = (float) h / height;
            int r = Math.max(1, Math.round(baseR * (1.0F - t * 0.5F)));
            int missing = (int) (10 + t * 60); // 10%..70% missing toward the top
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    int d2 = dx * dx + dz * dz;
                    boolean rim = d2 > (r - 1) * (r - 1) && d2 <= r * r + 1;
                    if (rim) {
                        VestigeDecay.decayedGlowbrick(level, writeBox, rand, origin.offset(dx, h, dz), missing);
                    }
                }
            }
            // Exposed crystal core (every other level), and a floor disc every ~6 levels.
            if (h % 2 == 0) {
                VestigeDecay.set(level, writeBox, origin.offset(0, h, 0), core);
            }
            if (h > 0 && h % 6 == 0) {
                for (int dx = -r + 1; dx <= r - 1; dx++) {
                    for (int dz = -r + 1; dz <= r - 1; dz++) {
                        if (rand.nextInt(100) >= missing) {
                            VestigeDecay.set(level, writeBox, origin.offset(dx, h, dz), floorTile);
                        }
                    }
                }
            }
        }

        // Stair fragments jutting from the shaft at a few heights.
        BlockState stair = ModBlocks.GLOWBRICK_STAIRS.get().defaultBlockState();
        int frags = 4 + rand.nextInt(4);
        for (int i = 0; i < frags; i++) {
            int h = 2 + rand.nextInt(Math.max(1, height - 3));
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
            int r = Math.max(1, Math.round(baseR * (1.0F - (float) h / height * 0.5F)));
            BlockPos at = origin.offset(dir.getStepX() * (r + 1), h, dir.getStepZ() * (r + 1));
            VestigeDecay.set(level, writeBox, at, stair.setValue(StairBlock.FACING, dir));
        }

        // Floating debris chunks near the top (broken fragments hanging in the air).
        int debris = 5 + rand.nextInt(5);
        for (int i = 0; i < debris; i++) {
            int h = height - rand.nextInt(Math.max(1, height / 2));
            int dx = rand.nextInt(2 * MAX_REACH - 2) - (MAX_REACH - 1);
            int dz = rand.nextInt(2 * MAX_REACH - 2) - (MAX_REACH - 1);
            if (Math.abs(dx) > baseR + 1 || Math.abs(dz) > baseR + 1) {
                VestigeDecay.set(level, writeBox, origin.offset(dx, h, dz), VestigeDecay.glowbrick(rand));
            }
        }

        // Spire Chest at the base interior, on a clean glowbrick plinth.
        BlockPos chestPos = origin.offset(0, 1, 0);
        VestigeDecay.set(
                level, writeBox, chestPos.below(), ModBlocks.GLOWBRICK.get().defaultBlockState());
        if (writeBox.isInside(chestPos)) {
            level.setBlock(chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH), 2);
            if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity be) {
                be.setLootTable(LOOT, rand.nextLong());
            }
        }
    }
}
