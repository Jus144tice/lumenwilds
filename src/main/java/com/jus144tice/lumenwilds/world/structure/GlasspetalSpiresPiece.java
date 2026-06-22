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
import net.minecraft.world.level.block.AmethystClusterBlock;
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
 * The Glasspetal Spires piece (Phase 8f; reworked Phase 9). {@link #postProcess} grows a cluster of tapering
 * crystal towers from a position-seeded RNG (writing only inside {@code writeBox}): a main spire + satellites of
 * solid blue-violet <b>Glasspetal Block</b> bristling with Glasspetal Clusters (the rare town-sized version of the
 * natural crystal growths — not a ruin), with a loot chest at the main spire's foot
 * ({@code chests/glasspetal_spires}). Each instance rolls a <b>size tier</b>
 * — regular / large / rare MASSIVE (like the Glowroot/Glowwood trees) — so the Crags vary. Every spire roots into
 * the terrain with a foundation that fills DOWN through water/air to solid ground, so nothing floats on the sea.
 */
public class GlasspetalSpiresPiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/glasspetal_spires"));

    // The box is sized for the MASSIVE case (the position-seeded RNG picks the actual size in postProcess).
    private static final int MAX_REACH = 22;
    private static final int MAX_HEIGHT = 66;
    private static final int FOUNDATION = 22;

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

        // v1.4.2: wipe the footprint first so this remnant overrides whatever generated here before it
        // (trees, a vanilla/modded structure, an overlapping ruin); natural terrain + caves below kept.
        VestigeDecay.clearArea(level, writeBox, this.getBoundingBox(), origin.getY() - 1);

        // Size tier: regular (70%) / large (25%) / MASSIVE (5%).
        int tier = rand.nextInt(100);
        int mainHeight;
        int baseR;
        int satellites;
        int spread;
        if (tier < 70) {
            mainHeight = 13 + rand.nextInt(6); // 13–18
            baseR = 3;
            satellites = 2 + rand.nextInt(2);
            spread = 5;
        } else if (tier < 95) {
            mainHeight = 24 + rand.nextInt(9); // 24–32
            baseR = 4 + rand.nextInt(2);
            satellites = 3 + rand.nextInt(2);
            spread = 7;
        } else {
            mainHeight = 45 + rand.nextInt(18); // 45–62, the rare giant
            baseR = 7 + rand.nextInt(3);
            satellites = 5 + rand.nextInt(3);
            spread = 11;
        }

        spire(level, writeBox, rand, origin, mainHeight, baseR);
        for (int i = 0; i < satellites; i++) {
            double ang = rand.nextDouble() * Math.PI * 2.0;
            int dx = (int) Math.round(Math.cos(ang) * (spread * (0.5 + rand.nextDouble() * 0.5)));
            int dz = (int) Math.round(Math.sin(ang) * (spread * (0.5 + rand.nextDouble() * 0.5)));
            int h = (int) (mainHeight * (0.4 + rand.nextDouble() * 0.4));
            int r = Math.max(2, baseR - 1 - rand.nextInt(2));
            spire(level, writeBox, rand, origin.offset(dx, 0, dz), h, r);
        }

        // Loot chest tucked at the foot of the main spire (carve a one-block alcove first).
        BlockPos chestPos = origin.offset(baseR, 1, 0);
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
        BlockState core = ModBlocks.GLASSPETAL_BLOCK.get().defaultBlockState();
        BlockState cluster = ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState();
        BlockState shimmer = ModBlocks.SHIMMERSTONE.get().defaultBlockState();

        // A solid blue-violet crystal core, tapering to a point.
        for (int h = 0; h <= height; h++) {
            float t = (float) h / height;
            int r = Math.round(baseR * (1.0F - t) + 0.3F);
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (dx * dx + dz * dz <= r * r + 1) {
                        set(level, box, base.offset(dx, h, dz), core);
                    }
                }
            }
        }
        // A cluster crown, and clusters bristling out of the sides (crystals growing off the spire).
        set(level, box, base.above(height + 1), cluster);
        int bristles = height + baseR * 2;
        for (int i = 0; i < bristles; i++) {
            int h = 1 + rand.nextInt(height);
            float t = (float) h / height;
            int r = Math.round(baseR * (1.0F - t) + 0.3F);
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
            BlockPos bp = new BlockPos(
                    base.getX() + dir.getStepX() * (r + 1), base.getY() + h, base.getZ() + dir.getStepZ() * (r + 1));
            set(level, box, bp, cluster.setValue(AmethystClusterBlock.FACING, dir));
        }

        // Foundation: root the base disc DOWN through water/air to solid ground (no floating on the sea).
        for (int dx = -baseR; dx <= baseR; dx++) {
            for (int dz = -baseR; dz <= baseR; dz++) {
                if (dx * dx + dz * dz <= baseR * baseR + 1) {
                    fillFoundation(level, box, base.getX() + dx, base.getY(), base.getZ() + dz, shimmer);
                }
            }
        }
    }

    /** Fills shimmerstone from just below {@code fromY} down through replaceable blocks until it hits solid ground. */
    private static void fillFoundation(WorldGenLevel level, BoundingBox box, int x, int fromY, int z, BlockState fill) {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dy = 1; dy <= FOUNDATION; dy++) {
            p.set(x, fromY - dy, z);
            if (!box.isInside(p)) {
                break;
            }
            if (!level.getBlockState(p).canBeReplaced()) {
                break; // reached solid ground
            }
            level.setBlock(p, fill, 2);
        }
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
