/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.block.LumenConduitBlock;
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
 * The Medium Vestige City piece (Phase 10d). {@link #postProcess} builds a radial dead alien city from a
 * position-seeded RNG (writing only inside {@code writeBox}, so the city spans chunks safely): a circular
 * chiseled-glowbrick {@link #plaza} with a dry {@link #fountain} and {@link #lightPylons}, four broken
 * {@link #road}s spoking out with embedded Lumen Conduit lines, and an outer ring of building "stamps"
 * (crescent house / hollow pod / archway / root chamber / plinth). Everything is run through the shared
 * {@link VestigeDecay} processors (fade, missing chunks, overgrowth) and rooted to the ground.
 *
 * <p>Curves-from-cubes language: concentric stepped rings (plaza/dome), 45°-spoked roads, and crescent arches
 * built from offset rows — never square medieval houses. Chests: one Scholar's Reliquary in the civic hall by
 * the plaza, plus Ruined Caches scattered through the ring.</p>
 */
public class VestigeCityPiece extends StructurePiece {

    private static final ResourceKey<LootTable> CACHE =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/ruined_cache"));
    private static final ResourceKey<LootTable> RELIQUARY =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/scholars_reliquary"));

    private static final int PLAZA_R = 7;
    private static final int CITY_R = 26;
    private static final int MAX_H = 16;
    private static final int FOUNDATION = 20;

    private final BlockPos origin;

    public VestigeCityPiece(BlockPos origin) {
        super(ModStructures.VESTIGE_CITY_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public VestigeCityPiece(CompoundTag tag) {
        super(ModStructures.VESTIGE_CITY_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - CITY_R - 5,
                o.getY() - FOUNDATION,
                o.getZ() - CITY_R - 5,
                o.getX() + CITY_R + 5,
                o.getY() + MAX_H + 2,
                o.getZ() + CITY_R + 5);
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

        plaza(level, writeBox, rand);
        fountain(level, writeBox, rand);
        lightPylons(level, writeBox, rand);

        // Four broken roads spoking out along the cardinals.
        road(level, writeBox, rand, 1, 0);
        road(level, writeBox, rand, -1, 0);
        road(level, writeBox, rand, 0, 1);
        road(level, writeBox, rand, 0, -1);

        // Outer ring of buildings, placed on the diagonals between the roads.
        int buildings = 8 + rand.nextInt(3);
        for (int i = 0; i < buildings; i++) {
            double ang = (i + 0.5) / buildings * Math.PI * 2.0;
            int radius = 15 + rand.nextInt(8);
            int dx = (int) Math.round(Math.cos(ang) * radius);
            int dz = (int) Math.round(Math.sin(ang) * radius);
            BlockPos c = origin.offset(dx, 0, dz);
            ResourceKey<LootTable> loot = i == 0 ? RELIQUARY : (rand.nextInt(3) == 0 ? CACHE : null);
            switch (rand.nextInt(5)) {
                case 0 -> crescentHouse(level, writeBox, rand, c, loot);
                case 1 -> hollowPod(level, writeBox, rand, c, loot);
                case 2 -> archway(level, writeBox, rand, c);
                case 3 -> rootChamber(level, writeBox, rand, c, loot);
                default -> plinth(level, writeBox, rand, c);
            }
        }
    }

    // --- Plaza --------------------------------------------------------------------------------------

    private void plaza(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        BlockState chiseled = ModBlocks.CHISELED_GLOWBRICK.get().defaultBlockState();
        BlockState tiles = ModBlocks.GLOWBRICK_TILES.get().defaultBlockState();
        for (int dx = -PLAZA_R; dx <= PLAZA_R; dx++) {
            for (int dz = -PLAZA_R; dz <= PLAZA_R; dz++) {
                int d2 = dx * dx + dz * dz;
                if (d2 > PLAZA_R * PLAZA_R) {
                    continue;
                }
                BlockPos p = origin.offset(dx, 0, dz);
                if (rand.nextInt(100) < 14) {
                    continue; // missing flagstone
                }
                // Concentric pattern: chiseled rings, tiles between.
                boolean ring = (int) Math.round(Math.sqrt(d2)) % 2 == 0;
                BlockState floor = ring ? chiseled : tiles;
                if (rand.nextInt(100) < 22) {
                    floor = VestigeDecay.glowbrick(rand); // weathered patches
                }
                VestigeDecay.set(level, box, p, floor);
                VestigeDecay.fillFoundation(
                        level,
                        box,
                        p.getX(),
                        p.getY(),
                        p.getZ(),
                        ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                        FOUNDATION);
                VestigeDecay.overgrow(level, box, rand, p.above(), 14);
            }
        }
    }

    private void fountain(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        // A dry crystal fountain: a low ring wall around a sunken basin, a broken central pylon.
        BlockState glow = ModBlocks.GLOWBRICK.get().defaultBlockState();
        int r = 2;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                int d2 = dx * dx + dz * dz;
                if (d2 == r * r || d2 == r * r - 1) {
                    VestigeDecay.set(level, box, origin.offset(dx, 0, dz), VestigeDecay.glowbrick(rand)); // basin rim
                    VestigeDecay.set(level, box, origin.offset(dx, 1, dz), VestigeDecay.glowbrick(rand));
                } else if (d2 < r * r) {
                    // Dry basin floor (the Lumenwater that once filled it is long gone).
                    VestigeDecay.set(
                            level,
                            box,
                            origin.offset(dx, -1, dz),
                            ModBlocks.DEEP_MOONSTONE.get().defaultBlockState());
                }
            }
        }
        // Broken central pylon topped with a dead Lumen Crystal Block ("dead crystal fountain").
        int ph = 2 + rand.nextInt(2);
        for (int h = 0; h <= ph; h++) {
            VestigeDecay.set(level, box, origin.offset(0, h, 0), glow);
        }
        VestigeDecay.set(
                level,
                box,
                origin.offset(0, ph + 1, 0),
                ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState());
    }

    private void lightPylons(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        int n = 6;
        for (int i = 0; i < n; i++) {
            double ang = i / (double) n * Math.PI * 2.0;
            int dx = (int) Math.round(Math.cos(ang) * (PLAZA_R - 1));
            int dz = (int) Math.round(Math.sin(ang) * (PLAZA_R - 1));
            BlockPos base = origin.offset(dx, 0, dz);
            boolean broken = rand.nextInt(3) == 0;
            int h = broken ? 1 + rand.nextInt(2) : 4 + rand.nextInt(2);
            for (int y = 1; y <= h; y++) {
                VestigeDecay.set(
                        level,
                        box,
                        base.offset(0, y, 0),
                        ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState());
            }
            if (!broken) {
                VestigeDecay.set(
                        level,
                        box,
                        base.offset(0, h + 1, 0),
                        ModBlocks.LUMENBULB.get().defaultBlockState());
            }
        }
    }

    // --- Roads --------------------------------------------------------------------------------------

    private void road(WorldGenLevel level, BoundingBox box, RandomSource rand, int sx, int sz) {
        BlockState dim = ModBlocks.LUMEN_CONDUIT
                .get()
                .defaultBlockState()
                .setValue(LumenConduitBlock.CONDUIT_STATE, LumenConduitBlock.State.DIM);
        BlockState dead = ModBlocks.LUMEN_CONDUIT
                .get()
                .defaultBlockState()
                .setValue(LumenConduitBlock.CONDUIT_STATE, LumenConduitBlock.State.DEAD);
        for (int t = PLAZA_R - 1; t <= CITY_R; t++) {
            int cx = sx * t;
            int cz = sz * t;
            for (int w = -1; w <= 1; w++) {
                int wx = sz != 0 ? w : 0; // perpendicular width
                int wz = sx != 0 ? w : 0;
                BlockPos p = origin.offset(cx + wx, 0, cz + wz);
                if (w == 0) {
                    // Centre line: an embedded conduit (dim near the plaza, dead farther out, with gaps).
                    if (rand.nextInt(100) < 18) {
                        continue;
                    }
                    VestigeDecay.set(level, box, p, t < CITY_R / 2 && rand.nextInt(3) != 0 ? dim : dead);
                } else {
                    VestigeDecay.decayedGlowbrick(level, box, rand, p, 26);
                }
                VestigeDecay.fillFoundation(
                        level,
                        box,
                        p.getX(),
                        p.getY(),
                        p.getZ(),
                        ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                        FOUNDATION);
                VestigeDecay.overgrow(level, box, rand, p.above(), 22);
            }
        }
    }

    // --- Building stamps ----------------------------------------------------------------------------

    private void crescentHouse(
            WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos c, ResourceKey<LootTable> loot) {
        int r = 3;
        floorAndFoundation(level, box, rand, c, r);
        // A C-shaped wall: an arc of the ring with a wide opening.
        double openA = rand.nextDouble() * Math.PI * 2.0;
        for (int a = 0; a < 360; a += 12) {
            double rad = Math.toRadians(a);
            // Skip a ~110° opening.
            double diff = Math.abs(Math.atan2(Math.sin(rad - openA), Math.cos(rad - openA)));
            if (diff < Math.toRadians(55)) {
                continue;
            }
            int dx = (int) Math.round(Math.cos(rad) * r);
            int dz = (int) Math.round(Math.sin(rad) * r);
            int wallH = 2 + rand.nextInt(2);
            for (int h = 1; h <= wallH; h++) {
                VestigeDecay.decayedGlowbrick(level, box, rand, c.offset(dx, h, dz), 28);
            }
            VestigeDecay.overgrow(level, box, rand, c.offset(dx, 1, dz), 16);
        }
        placeChest(level, box, rand, c.above(1), loot);
    }

    private void hollowPod(
            WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos c, ResourceKey<LootTable> loot) {
        floorAndFoundation(level, box, rand, c, 3);
        // A stepped dome: rings of decreasing radius (a pod that curves inward), with a broken top + doorway.
        int height = 4;
        int door = rand.nextInt(4);
        for (int h = 1; h <= height; h++) {
            int rr = Math.max(0, 3 - (h - 1));
            for (int a = 0; a < 360; a += 15) {
                double rad = Math.toRadians(a);
                int dx = (int) Math.round(Math.cos(rad) * rr);
                int dz = (int) Math.round(Math.sin(rad) * rr);
                if (h <= 2 && isDoorway(dx, dz, rr, door)) {
                    continue; // doorway gap
                }
                if (rand.nextInt(100) < (h == height ? 55 : 22)) {
                    continue; // broken (more at the top)
                }
                VestigeDecay.decayedGlowbrick(level, box, rand, c.offset(dx, h, dz), 0);
            }
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                VestigeDecay.overgrow(level, box, rand, c.offset(dx, 1, dz), 16);
            }
        }
        placeChest(level, box, rand, c.above(1), loot);
    }

    private void archway(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos c) {
        boolean alongX = rand.nextBoolean();
        int span = 2;
        BlockState pillar = ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState();
        BlockPos a = alongX ? c.offset(-span, 0, 0) : c.offset(0, 0, -span);
        BlockPos b = alongX ? c.offset(span, 0, 0) : c.offset(0, 0, span);
        int h = 4;
        for (BlockPos foot : new BlockPos[] {a, b}) {
            VestigeDecay.fillFoundation(
                    level,
                    box,
                    foot.getX(),
                    foot.getY(),
                    foot.getZ(),
                    ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                    FOUNDATION);
            for (int y = 0; y <= h; y++) {
                VestigeDecay.set(level, box, foot.offset(0, y, 0), pillar);
            }
        }
        // Crescent arch: a stepped peak spanning the two pillars.
        for (int s = -span; s <= span; s++) {
            int rise = h + (span - Math.abs(s)); // peaks in the middle
            BlockPos top = alongX ? c.offset(s, rise, 0) : c.offset(0, rise, s);
            if (rand.nextInt(100) < 22) {
                continue; // broken span
            }
            VestigeDecay.set(level, box, top, VestigeDecay.glowbrick(rand));
        }
    }

    private void rootChamber(
            WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos c, ResourceKey<LootTable> loot) {
        int r = 2;
        // Sunken floor.
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                VestigeDecay.set(
                        level,
                        box,
                        c.offset(dx, -1, dz),
                        ModBlocks.ROOTED_MOONSTONE.get().defaultBlockState());
                VestigeDecay.fillFoundation(
                        level,
                        box,
                        c.getX() + dx,
                        c.getY() - 1,
                        c.getZ() + dz,
                        ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                        FOUNDATION);
            }
        }
        // Walls of mixed rooted moonstone + glowbrick, full of glowvine.
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (Math.abs(dx) != r && Math.abs(dz) != r) {
                    continue;
                }
                for (int h = 0; h <= 2; h++) {
                    if (rand.nextInt(100) < 30) {
                        continue;
                    }
                    BlockState w = rand.nextBoolean()
                            ? ModBlocks.ROOTED_MOONSTONE.get().defaultBlockState()
                            : VestigeDecay.glowbrick(rand);
                    VestigeDecay.set(level, box, c.offset(dx, h, dz), w);
                }
                VestigeDecay.overgrow(level, box, rand, c.offset(dx, 1, dz), 40);
            }
        }
        VestigeDecay.overgrow(level, box, rand, c.offset(0, 0, 0), 60);
        placeChest(level, box, rand, c, loot);
    }

    private void plinth(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos c) {
        VestigeDecay.fillFoundation(
                level,
                box,
                c.getX(),
                c.getY(),
                c.getZ(),
                ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                FOUNDATION);
        VestigeDecay.set(level, box, c, ModBlocks.GLOWBRICK.get().defaultBlockState());
        VestigeDecay.set(level, box, c.above(), ModBlocks.GLOWBRICK_PILLAR.get().defaultBlockState());
        VestigeDecay.set(level, box, c.above(2), ModBlocks.GLOWBRICK_SLAB.get().defaultBlockState());
        // Sometimes a Memory Crystal still rests on the plinth.
        if (rand.nextInt(3) == 0) {
            VestigeDecay.set(
                    level, box, c.above(3), ModBlocks.MEMORY_CRYSTAL.get().defaultBlockState());
        }
    }

    // --- Shared stamp helpers -----------------------------------------------------------------------

    private void floorAndFoundation(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos c, int r) {
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz > r * r + 1) {
                    continue;
                }
                VestigeDecay.decayedGlowbrick(level, box, rand, c.offset(dx, 0, dz), 14);
                VestigeDecay.fillFoundation(
                        level,
                        box,
                        c.getX() + dx,
                        c.getY(),
                        c.getZ() + dz,
                        ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState(),
                        FOUNDATION);
            }
        }
    }

    private void placeChest(
            WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos at, ResourceKey<LootTable> loot) {
        if (loot == null || !box.isInside(at)) {
            return;
        }
        VestigeDecay.set(level, box, at.below(), ModBlocks.GLOWBRICK.get().defaultBlockState());
        BlockState chest = Blocks.CHEST
                .defaultBlockState()
                .setValue(ChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(rand));
        level.setBlock(at, chest, 2);
        if (level.getBlockEntity(at) instanceof ChestBlockEntity be) {
            be.setLootTable(loot, rand.nextLong());
        }
    }

    private static boolean isDoorway(int dx, int dz, int r, int doorSide) {
        if (r <= 0) {
            return false;
        }
        return switch (doorSide) {
            case 0 -> dz == -r && dx == 0;
            case 1 -> dz == r && dx == 0;
            case 2 -> dx == -r && dz == 0;
            default -> dx == r && dz == 0;
        };
    }
}
