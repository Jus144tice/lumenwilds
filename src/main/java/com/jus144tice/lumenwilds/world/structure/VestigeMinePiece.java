/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.block.LumenConduitBlock;
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
 * The Lumenwright Liftshaft + Abandoned Luminite Mine (Phase 11c) — added as a sub-piece of a Vestige City by
 * {@link VestigeCityStructure} ({@code docs/lumenwright_liftshafts.txt}). A single tall piece (like
 * {@link VestigeVaultPiece}) that spans from a buried mine chamber up to a surface <b>mine access dais</b>:
 *
 * <ul>
 *   <li>a deep carved-Moonstone chamber with arched Glowbrick ribs, exposed Luminite + Lumen-Crystal ore,
 *       dead/dim conduits, broken Gravity Lenses, an old Shimmerstone lift platform, an Echo Sentinel spawner,
 *       Memory-Crystal lore, and two loot caches (Miner's + Engineer's Mine);</li>
 *   <li>two side-by-side vertical shafts up to the dais — a <b>descent</b> column (pre-filled with
 *       {@code descent_field}) and an <b>ascension</b> column ({@code ascension_field}) — so the shafts work the
 *       moment they're discovered: step in and be lowered/raised. (The recoverable Lumen Field Projector +
 *       Gravity Repeaters are the Engineer's Mine Cache reward, so the player rebuilds the tech.)</li>
 *   <li>a surface octagonal dais of Glowbrick Tiles with the two shaft mouths, broken Gravity Lenses, dim
 *       conduits, Lumen-Crystal accents, and a Miner's Cache chest — foundation-rooted so it never floats.</li>
 * </ul>
 *
 * <p>Cave-aware connection (scan for a real cavern below) is deferred to a later cut; this uses the bible's
 * recommended "Simple Version" — a fixed-depth artificial chamber — so the content always exists.</p>
 */
public class VestigeMinePiece extends StructurePiece {

    private static final ResourceKey<LootTable> MINERS =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/miners_cache"));
    private static final ResourceKey<LootTable> ENGINEERS =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/engineers_mine_cache"));

    /** Chamber half-extents (origin = chamber floor centre). */
    private static final int HALF_W = 5;

    private static final int HALF_L = 5;
    private static final int CH_HEIGHT = 6;

    /** Local X of the descent / ascension shaft columns (z = 0), with a glowbrick divider between them. */
    private static final int DESCENT_DX = -1;

    private static final int ASCEND_DX = 1;

    private static final int DAIS_R = 4;
    private static final int REACH = HALF_W + DAIS_R + 2;

    private final BlockPos origin; // chamber floor centre
    private final int surfaceY; // dais (plaza) level

    public VestigeMinePiece(BlockPos origin, int surfaceY) {
        super(ModStructures.VESTIGE_MINE_PIECE.get(), 0, boxAround(origin, surfaceY));
        this.origin = origin;
        this.surfaceY = surfaceY;
    }

    public VestigeMinePiece(CompoundTag tag) {
        super(ModStructures.VESTIGE_MINE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
        this.surfaceY = tag.getInt("sy");
    }

    private static BoundingBox boxAround(BlockPos o, int surfaceY) {
        int top = Math.max(surfaceY, o.getY() + CH_HEIGHT) + 4;
        return new BoundingBox(
                o.getX() - REACH, o.getY() - 6, o.getZ() - REACH, o.getX() + REACH, top, o.getZ() + REACH);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("ox", origin.getX());
        tag.putInt("oy", origin.getY());
        tag.putInt("oz", origin.getZ());
        tag.putInt("sy", surfaceY);
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

        chamber(level, writeBox, rand);
        ribs(level, writeBox);
        machinery(level, writeBox, rand);
        shafts(level, writeBox);
        dais(level, writeBox, rand);
    }

    /** Carved Moonstone chamber: floor + shell, interior hollowed, ore veins exposed in the walls. */
    private void chamber(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int dx = -HALF_W; dx <= HALF_W; dx++) {
            for (int dz = -HALF_L; dz <= HALF_L; dz++) {
                for (int dy = 0; dy <= CH_HEIGHT; dy++) {
                    boolean border = dx == -HALF_W
                            || dx == HALF_W
                            || dz == -HALF_L
                            || dz == HALF_L
                            || dy == 0
                            || dy == CH_HEIGHT;
                    BlockPos p = origin.offset(dx, dy, dz);
                    if (!border) {
                        set(level, box, p, air);
                    } else if (dy == 0) {
                        set(level, box, p, floor(rand));
                    } else {
                        set(level, box, p, wall(rand));
                    }
                }
            }
        }
    }

    private static BlockState floor(RandomSource rand) {
        return switch (rand.nextInt(6)) {
            case 0, 1, 2 -> ModBlocks.MOONSTONE.get().defaultBlockState();
            case 3, 4 -> ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState();
            default -> ModBlocks.MOONSTONE_BRICKS.get().defaultBlockState();
        };
    }

    /** Wall mix — carved moonstone with exposed Luminite + Lumen-Crystal ore (the extraction seam). */
    private static BlockState wall(RandomSource rand) {
        int r = rand.nextInt(20);
        if (r < 2) {
            return ModBlocks.LUMINITE_ORE.get().defaultBlockState();
        }
        if (r < 3) {
            return ModBlocks.LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        }
        return switch (rand.nextInt(6)) {
            case 0, 1, 2 -> ModBlocks.MOONSTONE.get().defaultBlockState();
            case 3 -> ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState();
            case 4 -> ModBlocks.MOONSTONE_BRICKS.get().defaultBlockState();
            default -> ModBlocks.CRACKED_MOONSTONE_BRICKS.get().defaultBlockState();
        };
    }

    /** Arched Glowbrick support ribs across the chamber every few blocks (the Lumenwrights' "mining beams"). */
    private void ribs(WorldGenLevel level, BoundingBox box) {
        BlockState glow = ModBlocks.GLOWBRICK.get().defaultBlockState();
        for (int dz = -HALF_L + 2; dz <= HALF_L - 2; dz += 3) {
            // Two uprights against the walls + a flat arch across the ceiling.
            for (int dy = 1; dy <= CH_HEIGHT - 1; dy++) {
                set(level, box, origin.offset(-HALF_W + 1, dy, dz), glow);
                set(level, box, origin.offset(HALF_W - 1, dy, dz), glow);
            }
            for (int dx = -HALF_W + 1; dx <= HALF_W - 1; dx++) {
                set(level, box, origin.offset(dx, CH_HEIGHT - 1, dz), glow);
            }
        }
    }

    /** Broken machinery, an old lift platform, dead/dim conduits, ore-cluster lighting, spawner, lore + chests. */
    private void machinery(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        BlockState dim = ModBlocks.LUMEN_CONDUIT
                .get()
                .defaultBlockState()
                .setValue(LumenConduitBlock.CONDUIT_STATE, LumenConduitBlock.State.DIM);
        BlockState cracked = ModBlocks.CRACKED_GRAVITY_LENS.get().defaultBlockState();
        BlockState relay = ModBlocks.LUMEN_RELAY.get().defaultBlockState();

        // Dim conduit runs along the foot of the long walls (dead power lines).
        for (int dz = -HALF_L + 2; dz <= HALF_L - 2; dz++) {
            if (rand.nextInt(3) != 0) {
                set(level, box, origin.offset(-HALF_W + 1, 1, dz), dim);
            }
            if (rand.nextInt(3) != 0) {
                set(level, box, origin.offset(HALF_W - 1, 1, dz), dim);
            }
        }

        // Broken Gravity Lenses + relays scattered as dead extraction machinery.
        set(level, box, origin.offset(-HALF_W + 1, 1, HALF_L - 1), cracked);
        set(level, box, origin.offset(HALF_W - 1, 1, -HALF_L + 1), cracked);
        set(level, box, origin.offset(-HALF_W + 1, 1, -HALF_L + 1), relay);

        // An old Shimmerstone lift platform in a corner (where a gravity sled once rose).
        for (int dx = HALF_W - 2; dx <= HALF_W - 1; dx++) {
            for (int dz = HALF_L - 2; dz <= HALF_L - 1; dz++) {
                set(
                        level,
                        box,
                        origin.offset(dx, 1, dz),
                        ModBlocks.SHIMMERSTONE.get().defaultBlockState());
            }
        }

        // Lore — two Memory Crystals near the shaft mouths.
        set(level, box, origin.offset(-2, 1, 2), ModBlocks.MEMORY_CRYSTAL.get().defaultBlockState());
        set(level, box, origin.offset(2, 1, -2), ModBlocks.MEMORY_CRYSTAL.get().defaultBlockState());

        // Echo Sentinel spawner in a back corner (the mine's eternal guard).
        BlockPos spawnerPos = origin.offset(-HALF_W + 2, 1, -HALF_L + 2);
        if (box.isInside(spawnerPos)) {
            level.setBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState(), 2);
            if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
                spawner.setEntityId(ModEntities.ECHO_SENTINEL.get(), rand);
            }
        }

        // Two caches: a Miner's Cache (raw materials) and an Engineer's Mine Cache (the liftshaft tech to rebuild).
        placeChest(level, box, rand, origin.offset(HALF_W - 2, 1, 0), MINERS, Direction.WEST);
        placeChest(level, box, rand, origin.offset(-HALF_W + 2, 1, 0), ENGINEERS, Direction.EAST);
    }

    /**
     * The two gravity shafts up to the dais. The descent column is pre-filled with {@code descent_field}, the
     * ascension column with {@code ascension_field}; a glowbrick divider sits between them and a glowbrick casing
     * lines the slot. The columns punch through the chamber ceiling so they open into the room below.
     */
    private void shafts(WorldGenLevel level, BoundingBox box) {
        BlockState descent = ModBlocks.DESCENT_FIELD.get().defaultBlockState();
        BlockState ascend = ModBlocks.ASCENSION_FIELD.get().defaultBlockState();
        BlockState glow = ModBlocks.GLOWBRICK.get().defaultBlockState();
        int bottomY = origin.getY() + CH_HEIGHT - 1; // one below the ceiling, reaching into the chamber
        int topY = surfaceY - 1; // top field cell, just under the dais floor

        for (int y = bottomY; y <= topY; y++) {
            // Casing around the 3-wide (x:-2..2) × 1-deep (z:-1..1) slot, above the chamber ceiling.
            if (y >= origin.getY() + CH_HEIGHT) {
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        boolean rim = dx == -2 || dx == 2 || dz == -1 || dz == 1;
                        if (rim) {
                            setAbsolute(level, box, origin.getX() + dx, y, origin.getZ() + dz, glow);
                        }
                    }
                }
            }
            // The divider and the two field columns.
            setAbsolute(level, box, origin.getX(), y, origin.getZ(), glow);
            setAbsolute(level, box, origin.getX() + DESCENT_DX, y, origin.getZ(), descent);
            setAbsolute(level, box, origin.getX() + ASCEND_DX, y, origin.getZ(), ascend);
        }
    }

    /** Surface octagonal dais of Glowbrick Tiles with the two shaft mouths, broken tech, accents, and a chest. */
    private void dais(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState tiles = ModBlocks.GLOWBRICK_TILES.get().defaultBlockState();
        BlockState chiseled = ModBlocks.CHISELED_GLOWBRICK.get().defaultBlockState();
        int fy = surfaceY; // dais floor level

        for (int dx = -DAIS_R; dx <= DAIS_R; dx++) {
            for (int dz = -DAIS_R; dz <= DAIS_R; dz++) {
                if (Math.abs(dx) + Math.abs(dz) > DAIS_R + 2) {
                    continue; // octagon corners cut
                }
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                boolean shaftMouth = dz == 0 && (dx == DESCENT_DX || dx == ASCEND_DX || dx == 0);
                // Clear headroom above the dais (so it isn't buried on a slope).
                for (int dy = 1; dy <= 3; dy++) {
                    setAbsolute(level, box, x, fy + dy, z, air);
                }
                if (shaftMouth) {
                    if (dx == 0) {
                        setAbsolute(
                                level, box, x, fy, z, ModBlocks.GLOWBRICK.get().defaultBlockState()); // divider cap
                    } else {
                        setAbsolute(level, box, x, fy, z, air); // open mouth into the shaft
                    }
                } else {
                    boolean edge = Math.abs(dx) == DAIS_R
                            || Math.abs(dz) == DAIS_R
                            || Math.abs(dx) + Math.abs(dz) == DAIS_R + 2;
                    setAbsolute(
                            level,
                            box,
                            x,
                            fy,
                            z,
                            edge ? VestigeDecay.glowbrick(rand) : ((dx == 0 || dz == 0) ? chiseled : tiles));
                }
                // Root the dais to the ground so nothing floats on uneven terrain.
                VestigeDecay.weatheredFoundation(level, box, x, fy, z, rand, 12);
            }
        }

        // Broken Gravity Lenses flanking the shaft mouths (the dais "shaft heads"), + dim conduit accents.
        BlockState cracked = ModBlocks.CRACKED_GRAVITY_LENS.get().defaultBlockState();
        BlockState dim = ModBlocks.LUMEN_CONDUIT
                .get()
                .defaultBlockState()
                .setValue(LumenConduitBlock.CONDUIT_STATE, LumenConduitBlock.State.DIM);
        setAbsolute(level, box, origin.getX() + DESCENT_DX, fy, origin.getZ() - 1, cracked);
        setAbsolute(level, box, origin.getX() + ASCEND_DX, fy, origin.getZ() + 1, cracked);
        setAbsolute(level, box, origin.getX() - 3, fy, origin.getZ(), dim);
        setAbsolute(level, box, origin.getX() + 3, fy, origin.getZ(), dim);
        setAbsolute(
                level,
                box,
                origin.getX(),
                fy,
                origin.getZ() - 3,
                ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState());
        setAbsolute(
                level,
                box,
                origin.getX(),
                fy,
                origin.getZ() + 3,
                ModBlocks.LUMENBULB.get().defaultBlockState());

        // A Miner's Cache on the dais edge.
        placeChest(
                level, box, rand, new BlockPos(origin.getX() + 2, fy + 1, origin.getZ() + 2), MINERS, Direction.WEST);
    }

    private void placeChest(
            WorldGenLevel level,
            BoundingBox box,
            RandomSource rand,
            BlockPos at,
            ResourceKey<LootTable> loot,
            Direction facing) {
        if (!box.isInside(at)) {
            return;
        }
        level.setBlock(at, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
        if (level.getBlockEntity(at) instanceof ChestBlockEntity be) {
            be.setLootTable(loot, rand.nextLong());
        }
    }

    private static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        VestigeDecay.set(level, box, p, state);
    }

    private static void setAbsolute(WorldGenLevel level, BoundingBox box, int x, int y, int z, BlockState state) {
        BlockPos p = new BlockPos(x, y, z);
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
