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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * The Small Vestige Outpost piece (Phase 10b). {@link #postProcess} builds the "arrival edge" of a vanished
 * Lumenwright city from a position-seeded RNG (writing only inside {@code writeBox}), then runs it through the
 * shared {@link VestigeDecay} processors so the world has reclaimed it: a broken glowbrick road, a few toppled
 * glowbrick pillars, one roofless collapsed building shell (with a Ruined Cache chest and a still-flickering
 * Lumenbulb inside), an empty plinth, and scattered debris — all fading intact → cracked → ancient and creeping
 * with glowvine/lumen grass. Every standing part roots to the ground so nothing floats on slopes or seas.
 */
public class VestigeOutpostPiece extends StructurePiece {

    private static final ResourceKey<LootTable> LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/ruined_cache"));

    private static final int REACH = 11;
    private static final int FOUNDATION = 14;

    private final BlockPos origin;

    public VestigeOutpostPiece(BlockPos origin) {
        super(ModStructures.VESTIGE_OUTPOST_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public VestigeOutpostPiece(CompoundTag tag) {
        super(ModStructures.VESTIGE_OUTPOST_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - REACH,
                o.getY() - FOUNDATION,
                o.getZ() - REACH,
                o.getX() + REACH,
                o.getY() + 6,
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

        road(level, writeBox, rand, alongX);
        toppledPillars(level, writeBox, rand, alongX);

        // Building shell to one side of the road; the chest lives inside it.
        int side = rand.nextBoolean() ? 1 : -1;
        int jitter = rand.nextInt(5) - 2;
        BlockPos bc = alongX ? origin.offset(jitter, 0, 6 * side) : origin.offset(6 * side, 0, jitter);
        buildingShell(level, writeBox, rand, bc);

        plinth(level, writeBox, rand, alongX ? origin.offset(-4, 0, side * 2) : origin.offset(side * 2, 0, -4));
        debris(level, writeBox, rand);
    }

    /** A broken glowbrick road running through the origin, with gaps and creeping overgrowth. */
    private void road(WorldGenLevel level, BoundingBox box, RandomSource rand, boolean alongX) {
        for (int t = -7; t <= 7; t++) {
            BlockPos p = alongX ? origin.offset(t, 0, 0) : origin.offset(0, 0, t);
            VestigeDecay.decayedGlowbrick(level, box, rand, p, 22);
            // Occasionally a second lane.
            if (rand.nextInt(3) == 0) {
                BlockPos side = alongX
                        ? p.offset(0, 0, rand.nextBoolean() ? 1 : -1)
                        : p.offset(rand.nextBoolean() ? 1 : -1, 0, 0);
                VestigeDecay.decayedGlowbrick(level, box, rand, side, 30);
                VestigeDecay.overgrow(level, box, rand, side.above(), 28);
            }
            VestigeDecay.overgrow(level, box, rand, p.above(), 25);
            VestigeDecay.weatheredFoundation(level, box, p.getX(), p.getY(), p.getZ(), rand, FOUNDATION);
        }
    }

    /** A few short pillars, some standing, some toppled over (a horizontal axis). */
    private void toppledPillars(WorldGenLevel level, BoundingBox box, RandomSource rand, boolean alongX) {
        BlockState pillarY = ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState();
        int n = 3 + rand.nextInt(2);
        for (int i = 0; i < n; i++) {
            int dx = rand.nextInt(13) - 6;
            int dz = rand.nextInt(13) - 6;
            BlockPos base = origin.offset(dx, 0, dz);
            if (rand.nextBoolean()) {
                // Standing stub, 1–3 tall, with a broken (missing) top.
                int h = 1 + rand.nextInt(3);
                for (int j = 0; j < h; j++) {
                    VestigeDecay.set(level, box, base.above(j), pillarY);
                }
            } else {
                // Toppled: a short horizontal run on the ground.
                Direction.Axis axis = rand.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
                BlockState lying =
                        ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);
                int len = 2 + rand.nextInt(3);
                for (int j = 0; j < len; j++) {
                    BlockPos p = axis == Direction.Axis.X ? base.offset(j, 0, 0) : base.offset(0, 0, j);
                    if (rand.nextInt(5) != 0) {
                        VestigeDecay.set(level, box, p, lying);
                    }
                }
            }
        }
    }

    /** A roofless 5×5 building shell: decayed floor + broken walls + a doorway, a Lumenbulb, and the cache chest. */
    private void buildingShell(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos bc) {
        int r = 2; // 5×5 footprint
        // Floor.
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                VestigeDecay.decayedGlowbrick(level, box, rand, bc.offset(dx, 0, dz), 12);
                VestigeDecay.weatheredFoundation(
                        level, box, bc.getX() + dx, bc.getY(), bc.getZ() + dz, rand, FOUNDATION);
            }
        }
        // Walls (perimeter, up to 3 tall, lots missing). One side gets a 2-tall doorway gap.
        int doorSide = rand.nextInt(4);
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                boolean perimeter = Math.abs(dx) == r || Math.abs(dz) == r;
                if (!perimeter) {
                    continue;
                }
                boolean corner = Math.abs(dx) == r && Math.abs(dz) == r;
                int wallH = corner ? 3 : 2 + rand.nextInt(2);
                for (int h = 1; h <= wallH; h++) {
                    if (isDoorway(dx, dz, r, doorSide) && h <= 2) {
                        continue;
                    }
                    BlockPos wp = bc.offset(dx, h, dz);
                    if (corner) {
                        VestigeDecay.set(
                                level, box, wp, ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState());
                    } else {
                        VestigeDecay.decayedGlowbrick(level, box, rand, wp, 34);
                    }
                    // Glowvine creeping the walls.
                    VestigeDecay.overgrow(
                            level,
                            box,
                            rand,
                            wp.offset(dx == r ? 1 : (dx == -r ? -1 : 0), 0, dz == r ? 1 : (dz == -r ? -1 : 0)),
                            12);
                }
            }
        }
        // A still-flickering light inside, and interior overgrowth.
        VestigeDecay.set(level, box, bc.above(0), ModBlocks.LUMENBULB.get().defaultBlockState());
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                VestigeDecay.overgrow(level, box, rand, bc.offset(dx, 1, dz), 18);
            }
        }

        // Ruined Cache chest in a corner of the shell, on a clean glowbrick base.
        BlockPos chestPos = bc.offset(r - 1, 1, -(r - 1));
        VestigeDecay.set(level, box, chestPos.below(), ModBlocks.GLOWBRICK.get().defaultBlockState());
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST);
        if (box.isInside(chestPos)) {
            level.setBlock(chestPos, chest, 2);
            if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity be) {
                be.setLootTable(LOOT, rand.nextLong());
            }
        }
    }

    private static boolean isDoorway(int dx, int dz, int r, int doorSide) {
        return switch (doorSide) {
            case 0 -> dz == -r && dx == 0;
            case 1 -> dz == r && dx == 0;
            case 2 -> dx == -r && dz == 0;
            default -> dx == r && dz == 0;
        };
    }

    /** An empty plinth: a short pillar capped by a glowbrick slab (a monument with nothing left on it). */
    private void plinth(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos at) {
        VestigeDecay.weatheredFoundation(level, box, at.getX(), at.getY(), at.getZ(), rand, FOUNDATION);
        VestigeDecay.set(level, box, at, ModBlocks.GLOWBRICK.get().defaultBlockState());
        VestigeDecay.set(
                level, box, at.above(), ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState());
        VestigeDecay.set(level, box, at.above(2), ModBlocks.GLOWBRICK_SLAB.get().defaultBlockState());
    }

    /** Scattered ruin rubble around the site. */
    private void debris(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        for (int n = 0; n < 16; n++) {
            int dx = rand.nextInt(2 * REACH - 2) - (REACH - 1);
            int dz = rand.nextInt(2 * REACH - 2) - (REACH - 1);
            int dy = rand.nextInt(2);
            BlockPos p = origin.offset(dx, dy, dz);
            if (rand.nextBoolean() && box.isInside(p) && level.getBlockState(p).canBeReplaced()) {
                VestigeDecay.set(level, box, p, VestigeDecay.rubble(rand));
            }
        }
    }
}
