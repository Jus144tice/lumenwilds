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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
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

/**
 * The Rootshrine piece (Phase 8d). {@link #postProcess} builds a small shrine procedurally from a
 * position-seeded RNG, writing only inside the per-chunk {@code writeBox} (so it's chunk-safe even though it's
 * small): a Moonstone floor disc, four Glowroot-log roots arching up to a meeting peak (the "inside giant
 * roots" cage) with a leaf cap + hanging Glowvine, Lumenbulb lights, and a loot chest on a central pedestal.
 */
public class RootshrinePiece extends StructurePiece {

    private static final ResourceKey<net.minecraft.world.level.storage.loot.LootTable> LOOT = ResourceKey.create(
            net.minecraft.core.registries.Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/rootshrine"));

    private static final int RADIUS = 3; // floor radius
    private static final int PEAK = 7; // root arch height
    private static final int REACH = RADIUS + 2;

    private final BlockPos origin;

    public RootshrinePiece(BlockPos origin) {
        super(ModStructures.ROOTSHRINE_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public RootshrinePiece(CompoundTag tag) {
        super(ModStructures.ROOTSHRINE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - REACH,
                o.getY() - 2,
                o.getZ() - REACH,
                o.getX() + REACH,
                o.getY() + PEAK + 2,
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

        BlockState moonstone = ModBlocks.MOONSTONE.get().defaultBlockState();
        BlockState log = ModBlocks.GLOWROOT_LOG.get().defaultBlockState();
        BlockState leaves = ModBlocks.GLOWROOT_LEAVES.get().defaultBlockState();
        BlockState bulb = ModBlocks.LUMENBULB.get().defaultBlockState();
        BlockState vine = ModBlocks.GLOWVINE.get().defaultBlockState();

        // 1) Floor disc (one below origin) + clear the chamber air above it.
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                if (dx * dx + dz * dz <= RADIUS * RADIUS + 1) {
                    set(level, writeBox, origin.offset(dx, -1, dz), moonstone);
                    for (int dy = 0; dy < PEAK; dy++) {
                        set(level, writeBox, origin.offset(dx, dy, dz), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

        // 2) Four roots arching from the rim up to a shared peak.
        int[][] corners = {{RADIUS, RADIUS}, {RADIUS, -RADIUS}, {-RADIUS, RADIUS}, {-RADIUS, -RADIUS}};
        for (int[] c : corners) {
            for (int step = 0; step <= 14; step++) {
                float t = step / 14.0F;
                int x = Math.round(c[0] * (1.0F - t));
                int z = Math.round(c[1] * (1.0F - t));
                int y = Math.round(PEAK * Mth.sin(t * Mth.HALF_PI));
                set(level, writeBox, origin.offset(x, y, z), log);
                if (rand.nextInt(3) == 0) {
                    set(level, writeBox, origin.offset(x, y - 1, z), vine);
                }
            }
        }

        // 3) Leaf cap + a crowning Lumenbulb at the peak.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                set(level, writeBox, origin.offset(dx, PEAK, dz), leaves);
            }
        }
        set(level, writeBox, origin.offset(0, PEAK - 1, 0), bulb);

        // 4) Lumenbulb lights set into the rim corners.
        for (int[] c : corners) {
            set(level, writeBox, origin.offset(c[0], -1, c[1]), bulb);
        }

        // 5) Central pedestal + loot chest.
        BlockPos pedestal = origin;
        set(level, writeBox, pedestal, moonstone);
        BlockPos chestPos = origin.above();
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
        if (writeBox.isInside(chestPos)) {
            level.setBlock(chestPos, chest, 2);
            if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity be) {
                be.setLootTable(LOOT, rand.nextLong());
            }
        }
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
