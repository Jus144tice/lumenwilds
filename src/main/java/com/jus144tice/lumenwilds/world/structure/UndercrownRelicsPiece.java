/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModEntities;
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
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * The Undercrown Relics piece (Phase 8g). {@link #postProcess} carves a buried dungeon hall into the rock
 * (writing only inside {@code writeBox}): a Deep-Moonstone shell around a 9×7×5 air chamber, a tiled floor,
 * four pillars, Lumenbulb lighting, a central <b>mob spawner</b> (Shade Stalker), and two loot chests of rare
 * loot + portal-stabilization (Lumen Anchor) parts ({@code chests/undercrown_relics}).
 */
public class UndercrownRelicsPiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/undercrown_relics"));

    private static final int HALF_W = 4; // interior half-width (x)
    private static final int HALF_L = 3; // interior half-length (z)
    private static final int HEIGHT = 4; // interior height (y: 0..HEIGHT)
    private static final int REACH = HALF_W + 2;

    private final BlockPos origin;

    public UndercrownRelicsPiece(BlockPos origin) {
        super(ModStructures.UNDERCROWN_RELICS_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public UndercrownRelicsPiece(CompoundTag tag) {
        super(ModStructures.UNDERCROWN_RELICS_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - REACH,
                o.getY() - 2,
                o.getZ() - HALF_L - 2,
                o.getX() + REACH,
                o.getY() + HEIGHT + 2,
                o.getZ() + HALF_L + 2);
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

        BlockState bricks = ModBlocks.DEEP_MOONSTONE_BRICKS.get().defaultBlockState();
        BlockState cobbled = ModBlocks.COBBLED_DEEP_MOONSTONE.get().defaultBlockState();
        BlockState deep = ModBlocks.DEEP_MOONSTONE.get().defaultBlockState();
        BlockState tiles = ModBlocks.DEEP_MOONSTONE_TILES.get().defaultBlockState();
        BlockState polished = ModBlocks.POLISHED_DEEP_MOONSTONE.get().defaultBlockState();
        BlockState bulb = ModBlocks.LUMENBULB.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        // 1) Shell + carved interior. Border = wall, floor = tiles, interior = air.
        for (int dx = -HALF_W - 1; dx <= HALF_W + 1; dx++) {
            for (int dz = -HALF_L - 1; dz <= HALF_L + 1; dz++) {
                for (int dy = -1; dy <= HEIGHT + 1; dy++) {
                    boolean border = dx == -HALF_W - 1
                            || dx == HALF_W + 1
                            || dz == -HALF_L - 1
                            || dz == HALF_L + 1
                            || dy == -1
                            || dy == HEIGHT + 1;
                    BlockPos p = origin.offset(dx, dy, dz);
                    if (!border) {
                        set(level, writeBox, p, air);
                    } else if (dy == -1) {
                        set(level, writeBox, p, tiles);
                    } else {
                        int roll = rand.nextInt(10);
                        set(level, writeBox, p, roll < 6 ? bricks : (roll < 9 ? cobbled : deep));
                    }
                }
            }
        }

        // 2) Four pillars.
        for (int sx = -1; sx <= 1; sx += 2) {
            for (int sz = -1; sz <= 1; sz += 2) {
                for (int dy = 0; dy <= HEIGHT; dy++) {
                    set(level, writeBox, origin.offset(sx * 2, dy, sz * 2), polished);
                }
            }
        }

        // 3) Lumenbulb lighting set into the upper walls.
        for (int sx = -1; sx <= 1; sx += 2) {
            set(level, writeBox, origin.offset(sx * (HALF_W + 1), HEIGHT - 1, 0), bulb);
        }

        // 4) Central mob spawner (Shade Stalker — the dungeon's keeper).
        BlockPos spawnerPos = origin.above();
        if (writeBox.isInside(spawnerPos)) {
            level.setBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState(), 2);
            if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
                spawner.setEntityId(ModEntities.SHADE_STALKER.get(), rand);
            }
        }

        // 5) Two loot chests against opposite walls.
        placeChest(level, writeBox, rand, origin.offset(-HALF_W, 0, HALF_L - 1), Direction.EAST);
        placeChest(level, writeBox, rand, origin.offset(HALF_W, 0, -HALF_L + 1), Direction.WEST);
    }

    private void placeChest(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos at, Direction facing) {
        if (!box.isInside(at)) {
            return;
        }
        level.setBlock(at, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
        if (level.getBlockEntity(at) instanceof ChestBlockEntity be) {
            be.setLootTable(LOOT, rand.nextLong());
        }
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
