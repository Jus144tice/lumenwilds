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
 * The Lumenbound Ruins piece (Phase 8e). {@link #postProcess} builds a <b>broken portal frame</b> from a
 * position-seeded RNG (writing only inside {@code writeBox}): a 4×5 Lumenbound Stone frame around a 2×3 hole
 * with ~30% of the frame missing or weathered into mossy/cracked stone, a ragged base platform, scattered
 * rubble, and a half-buried loot chest of striker/frame ingredients ({@code chests/lumenbound_ruins}).
 */
public class LumenboundRuinsPiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/lumenbound_ruins"));

    private static final int FRAME_WIDTH = 4;
    private static final int FRAME_HEIGHT = 5;
    private static final int REACH = 5;

    private final BlockPos origin;

    public LumenboundRuinsPiece(BlockPos origin) {
        super(ModStructures.LUMENBOUND_RUINS_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public LumenboundRuinsPiece(CompoundTag tag) {
        super(ModStructures.LUMENBOUND_RUINS_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - REACH,
                o.getY() - 3,
                o.getZ() - REACH,
                o.getX() + REACH,
                o.getY() + FRAME_HEIGHT + 1,
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
        boolean alongX = rand.nextBoolean();

        BlockState lumenbound = ModBlocks.LUMENBOUND_STONE.get().defaultBlockState();
        BlockState cobbled = ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState();
        BlockState mossy = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        BlockState cracked = ModBlocks.CRACKED_MOONSTONE_BRICKS.get().defaultBlockState(); // themed (Phase 9d)
        BlockState mossyCobble = Blocks.MOSSY_COBBLESTONE.defaultBlockState();

        // 1) Base platform — a ragged disc one below the frame, half the blocks weathered.
        for (int dx = -2; dx <= FRAME_WIDTH + 1; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (rand.nextInt(4) != 0) {
                    BlockPos base = alongX ? origin.offset(dx, -1, dz) : origin.offset(dz, -1, dx);
                    set(level, writeBox, base, weathered(rand, lumenbound, cobbled, mossyCobble));
                }
            }
        }

        // 2) The broken portal frame (border of a 4×5 rectangle around a 2×3 hole).
        for (int i = 0; i < FRAME_WIDTH; i++) {
            for (int j = 0; j < FRAME_HEIGHT; j++) {
                boolean isFrame = i == 0 || i == FRAME_WIDTH - 1 || j == 0 || j == FRAME_HEIGHT - 1;
                if (!isFrame) {
                    continue;
                }
                int roll = rand.nextInt(100);
                if (roll < 30) {
                    continue; // missing / broken away
                }
                BlockState block = roll < 62 ? lumenbound : (roll < 82 ? mossy : cracked);
                set(level, writeBox, framePos(i, j, alongX), block);
            }
        }

        // 3) Scattered rubble around the base.
        for (int n = 0; n < 8; n++) {
            int dx = rand.nextInt(9) - 3;
            int dz = rand.nextInt(7) - 3;
            int dy = rand.nextInt(2) - 1;
            BlockPos rubble = alongX ? origin.offset(dx, dy, dz) : origin.offset(dz, dy, dx);
            if (rand.nextBoolean()) {
                set(level, writeBox, rubble, weathered(rand, lumenbound, cobbled, cracked));
            }
        }

        // 4) Half-buried loot chest just in front of the frame.
        BlockPos chestPos = alongX ? origin.offset(1, 0, 2) : origin.offset(2, 0, 1);
        BlockState chest =
                Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, alongX ? Direction.SOUTH : Direction.EAST);
        if (writeBox.isInside(chestPos)) {
            level.setBlock(chestPos, chest, 2);
            if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity be) {
                be.setLootTable(LOOT, rand.nextLong());
            }
        }
    }

    /** Frame block world position for local (along, up), oriented along X or Z. */
    private BlockPos framePos(int along, int up, boolean alongX) {
        return alongX ? origin.offset(along, up, 0) : origin.offset(0, up, along);
    }

    private static BlockState weathered(RandomSource rand, BlockState a, BlockState b, BlockState c) {
        int r = rand.nextInt(3);
        return r == 0 ? a : (r == 1 ? b : c);
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
