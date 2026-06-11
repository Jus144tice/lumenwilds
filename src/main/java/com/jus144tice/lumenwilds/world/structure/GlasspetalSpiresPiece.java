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
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * The Glasspetal Spires piece (Phase 8f). {@link #postProcess} grows a cluster of tapering crystal towers
 * from a position-seeded RNG (writing only inside {@code writeBox}): a tall main spire + two satellites of
 * mixed Shimmerstone / Shimmerstone Bricks / Lumen Crystal Block, crowned with a Glasspetal Cluster, with a
 * loot chest tucked into the main spire's base ({@code chests/glasspetal_spires}).
 */
public class GlasspetalSpiresPiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/glasspetal_spires"));

    private static final int REACH = 8;
    private static final int MAIN_HEIGHT = 16;

    private final BlockPos origin;

    public GlasspetalSpiresPiece(BlockPos origin) {
        super(ModStructures.GLASSPETAL_SPIRES_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public GlasspetalSpiresPiece(CompoundTag tag) {
        super(ModStructures.GLASSPETAL_SPIRES_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - REACH,
                o.getY() - 3,
                o.getZ() - REACH,
                o.getX() + REACH,
                o.getY() + MAIN_HEIGHT + 3,
                o.getZ() + REACH);
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

        spire(level, writeBox, rand, origin, MAIN_HEIGHT, 3);
        spire(level, writeBox, rand, origin.offset(5, 0, 2), 9, 2);
        spire(level, writeBox, rand, origin.offset(-4, 0, -3), 11, 2);

        // Loot chest tucked at the foot of the main spire (carve a one-block alcove first).
        BlockPos chestPos = origin.offset(3, 0, 0);
        set(level, writeBox, chestPos, Blocks.AIR.defaultBlockState());
        set(level, writeBox, chestPos.below(), ModBlocks.SHIMMERSTONE.get().defaultBlockState());
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST);
        if (writeBox.isInside(chestPos)) {
            level.setBlock(chestPos, chest, 2);
            if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity be) {
                be.setLootTable(LOOT, rand.nextLong());
            }
        }
    }

    private void spire(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos base, int height, int baseR) {
        BlockState shimmer = ModBlocks.SHIMMERSTONE.get().defaultBlockState();
        BlockState bricks = ModBlocks.SHIMMERSTONE_BRICKS.get().defaultBlockState();
        BlockState crystal = ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState();

        for (int h = 0; h <= height; h++) {
            float t = (float) h / height;
            int r = Math.round(baseR * (1.0F - t) + 0.3F);
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx * dx + dz * dz <= r * r + 1) {
                        int roll = rand.nextInt(10);
                        BlockState block = roll < 5 ? shimmer : (roll < 8 ? bricks : crystal);
                        set(level, box, base.offset(dx, h, dz), block);
                    }
                }
            }
        }
        // Glasspetal crown (the cluster's default state faces up).
        set(
                level,
                box,
                base.above(height + 1),
                ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState());
        set(level, box, base.above(height), ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState());
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
