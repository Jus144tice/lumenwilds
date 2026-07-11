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
 * The Duskglass Ruins piece (v1.7.0) — a broken Dusk-portal site: a 4×5 Duskglass frame (~30% missing, the rest
 * weathered into dark stone) around a 2×3 hole, a ragged dark base, scattered rubble, <b>a couple of small
 * contained lava pools</b> (Duskglass is quenched lava), and a half-buried chest of Lumenwater buckets + treasure
 * ({@code chests/duskglass_ruins}). The frame volume is cleared to air first, so it works in the open Lumenwilds
 * <em>and</em> in the enclosed Nether (where {@code DuskglassRuinsStructure} anchors it on a cave floor).
 */
public class DuskglassRuinsPiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/duskglass_ruins"));

    private static final int FRAME_WIDTH = 4;
    private static final int FRAME_HEIGHT = 5;
    private static final int REACH = 7;

    private final BlockPos origin;

    public DuskglassRuinsPiece(BlockPos origin) {
        super(ModStructures.DUSKGLASS_RUINS_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public DuskglassRuinsPiece(CompoundTag tag) {
        super(ModStructures.DUSKGLASS_RUINS_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - REACH,
                o.getY() - 3,
                o.getZ() - REACH,
                o.getX() + REACH,
                o.getY() + FRAME_HEIGHT + 2,
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

        BlockState duskglass = ModBlocks.DUSKGLASS.get().defaultBlockState();
        BlockState cobbledDeep = ModBlocks.COBBLED_DEEP_MOONSTONE.get().defaultBlockState();
        BlockState deep = ModBlocks.DEEP_MOONSTONE.get().defaultBlockState();
        BlockState blackstone = Blocks.BLACKSTONE.defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState lava = Blocks.LAVA.defaultBlockState();

        // 0) Clear a pocket over the footprint (declutters the enclosed Nether; a no-op over open sky).
        for (int dx = -3; dx <= FRAME_WIDTH + 2; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = 0; dy <= FRAME_HEIGHT + 1; dy++) {
                    BlockPos p = alongX ? origin.offset(dx, dy, dz) : origin.offset(dz, dy, dx);
                    set(level, writeBox, p, air);
                }
            }
        }

        // 1) Base platform — a ragged dark disc one below the frame, most blocks weathered.
        for (int dx = -3; dx <= FRAME_WIDTH + 2; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                if (rand.nextInt(5) != 0) {
                    BlockPos base = alongX ? origin.offset(dx, -1, dz) : origin.offset(dz, -1, dx);
                    set(level, writeBox, base, weathered(rand, cobbledDeep, deep, blackstone));
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
                BlockState block = roll < 65 ? duskglass : (roll < 84 ? blackstone : cobbledDeep);
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
                set(level, writeBox, rubble, weathered(rand, duskglass, cobbledDeep, blackstone));
            }
        }

        // 4) A couple of small, contained lava pools set into the base, off to the side of the frame.
        int pools = 1 + rand.nextInt(2);
        for (int n = 0; n < pools; n++) {
            int side = rand.nextBoolean() ? -2 : FRAME_WIDTH + 1;
            int off = rand.nextInt(3) - 1;
            BlockPos poolCenter = alongX ? origin.offset(side, 0, off) : origin.offset(off, 0, side);
            lavaPool(level, writeBox, poolCenter, cobbledDeep, deep, lava);
        }

        // 5) Half-buried loot chest just in front of the frame.
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

    /** A contained 2×2 lava pool at ground level: solid floor + rim so the lava can't escape. */
    private void lavaPool(
            WorldGenLevel level, BoundingBox box, BlockPos center, BlockState floor, BlockState rim, BlockState lava) {
        // solid floor + rim (3×3 at y-2 floor, 3×3 rim at y-1) then a flush 2×2 lava pool.
        for (int dx = -1; dx <= 2; dx++) {
            for (int dz = -1; dz <= 2; dz++) {
                set(level, box, center.offset(dx, -2, dz), floor);
                boolean isRim = dx == -1 || dx == 2 || dz == -1 || dz == 2;
                set(level, box, center.offset(dx, -1, dz), isRim ? rim : lava);
                set(level, box, center.offset(dx, 0, dz), Blocks.AIR.defaultBlockState());
            }
        }
    }

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
