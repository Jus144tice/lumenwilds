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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * The Vestige Vault piece (Phase 10f.2) — the buried technical layer beneath a Medium Vestige City, added as a
 * second piece by {@link VestigeCityStructure}. A Deep-Moonstone + glowbrick chamber holding a <b>resonance
 * puzzle</b>: a dead central {@link com.jus144tice.lumenwilds.block.DormantLightEngineBlock} wired by dead
 * Lumen Conduits to two sealed {@code ancient_door}s that guard the loot (a Vault chest + an Engineer's Cache).
 * The player restores the engine (a {@code resonance_core_fragment}) → the conduits power → the doors open —
 * the 10e tech as a lock-and-key. An <b>Echo Sentinel</b> spawner keeps watch, and a corner spiral stair of
 * glowbrick steps climbs back up to a hole in the plaza above.
 */
public class VestigeVaultPiece extends StructurePiece {

    private static final ResourceKey<LootTable> VAULT =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/vault"));
    private static final ResourceKey<LootTable> ENGINEERS =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationHelper.modLoc("chests/engineers_cache"));

    private static final int HALF_W = 6;
    private static final int HALF_L = 5;
    private static final int HEIGHT = 5;
    private static final int REACH = HALF_W + 3;

    // Orthogonally-adjacent ring walk (radius 1) for the spiral stair — each consecutive cell is a 1-block step.
    private static final int[][] RING = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    private final BlockPos origin;
    private final int surfaceY;

    public VestigeVaultPiece(BlockPos origin, int surfaceY) {
        super(ModStructures.VESTIGE_VAULT_PIECE.get(), 0, boxAround(origin, surfaceY));
        this.origin = origin;
        this.surfaceY = surfaceY;
    }

    public VestigeVaultPiece(CompoundTag tag) {
        super(ModStructures.VESTIGE_VAULT_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
        this.surfaceY = tag.getInt("sy");
    }

    private static BoundingBox boxAround(BlockPos o, int surfaceY) {
        int top = Math.max(surfaceY, o.getY() + HEIGHT) + 2;
        return new BoundingBox(
                o.getX() - REACH, o.getY() - 2, o.getZ() - REACH, o.getX() + REACH, top, o.getZ() + REACH);
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
        resonancePuzzle(level, writeBox, rand);
        decor(level, writeBox, rand);
        shaft(level, writeBox);
    }

    /** Deep-Moonstone + glowbrick shell around an air chamber, glowbrick-tile floor. */
    private void chamber(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState tiles = ModBlocks.GLOWBRICK_TILES.get().defaultBlockState();
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
                        set(level, box, p, air);
                    } else if (dy == -1) {
                        set(level, box, p, tiles);
                    } else {
                        set(level, box, p, wall(rand));
                    }
                }
            }
        }
    }

    private static BlockState wall(RandomSource rand) {
        return switch (rand.nextInt(8)) {
            case 0, 1, 2 -> ModBlocks.DEEP_MOONSTONE_BRICKS.get().defaultBlockState();
            case 3, 4 -> ModBlocks.COBBLED_DEEP_MOONSTONE.get().defaultBlockState();
            case 5, 6 -> ModBlocks.GLOWBRICK.get().defaultBlockState();
            default -> ModBlocks.CRACKED_GLOWBRICK.get().defaultBlockState();
        };
    }

    /** The lock-and-key: a dormant engine wired by dead conduits to two sealed ancient doors guarding loot. */
    private void resonancePuzzle(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        // Central Dormant Light Engine on a polished pedestal.
        set(level, box, origin.below(), ModBlocks.POLISHED_DEEP_MOONSTONE.get().defaultBlockState());
        set(level, box, origin, ModBlocks.DORMANT_LIGHT_ENGINE.get().defaultBlockState());

        BlockState conduit = ModBlocks.LUMEN_CONDUIT.get().defaultBlockState(); // default = dead

        // Two dead conduit runs along the X axis to doors in the east + west walls.
        for (int dir = -1; dir <= 1; dir += 2) {
            for (int x = 1; x <= HALF_W; x++) {
                set(level, box, origin.offset(dir * x, 0, 0), conduit);
            }
            // Ancient door set into the wall opening (carve a 1×2 gap, place both halves).
            BlockPos doorLower = origin.offset(dir * (HALF_W + 1), 0, 0);
            Direction facing = dir > 0 ? Direction.WEST : Direction.EAST; // open inward
            placeDoor(level, box, doorLower, facing);

            // Loot alcove behind the door.
            BlockPos alcove = origin.offset(dir * (HALF_W + 2), 0, 0);
            carveAlcove(level, box, alcove);
            placeChest(level, box, rand, alcove, dir > 0 ? VAULT : ENGINEERS, facing);
        }

        // Echo Sentinel spawner in a back corner (its eternal guard).
        BlockPos spawnerPos = origin.offset(-(HALF_W - 2), 0, -(HALF_L - 1));
        if (box.isInside(spawnerPos)) {
            level.setBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState(), 2);
            if (level.getBlockEntity(spawnerPos) instanceof SpawnerBlockEntity spawner) {
                spawner.setEntityId(ModEntities.ECHO_SENTINEL.get(), rand);
            }
        }
    }

    private void placeDoor(WorldGenLevel level, BoundingBox box, BlockPos lower, Direction facing) {
        BlockState door = ModBlocks.ANCIENT_DOOR.get().defaultBlockState().setValue(DoorBlock.FACING, facing);
        set(level, box, lower, door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        set(level, box, lower.above(), door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
    }

    private void carveAlcove(WorldGenLevel level, BoundingBox box, BlockPos center) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState glow = ModBlocks.GLOWBRICK.get().defaultBlockState();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 2; dy++) {
                    BlockPos p = center.offset(dx, dy, dz);
                    boolean shell = dy == -1 || dy == 2 || Math.abs(dx) == 1 || Math.abs(dz) == 1;
                    set(level, box, p, dy >= 0 && dx == 0 && dz == 0 ? air : (shell ? glow : air));
                }
            }
        }
    }

    /** A scatter of lore + dead-tech atmosphere: Memory Crystals, dim conduit accents on the floor. */
    private void decor(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        set(
                level,
                box,
                origin.offset(-(HALF_W - 1), 0, HALF_L),
                ModBlocks.MEMORY_CRYSTAL.get().defaultBlockState());
        set(
                level,
                box,
                origin.offset(HALF_W - 1, 0, HALF_L),
                ModBlocks.MEMORY_CRYSTAL.get().defaultBlockState());
        // A dim conduit border along part of the north wall foot (decorative dead tech).
        BlockState dim = ModBlocks.LUMEN_CONDUIT
                .get()
                .defaultBlockState()
                .setValue(LumenConduitBlock.CONDUIT_STATE, LumenConduitBlock.State.DIM);
        for (int dx = -3; dx <= 3; dx++) {
            if (rand.nextInt(3) != 0) {
                set(level, box, origin.offset(dx, 0, -HALF_L), dim);
            }
        }
    }

    /** A corner spiral of glowbrick steps climbing from the chamber floor up to a hole in the plaza. */
    private void shaft(WorldGenLevel level, BoundingBox box) {
        int scx = HALF_W - 2;
        int scz = HALF_L - 2;
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState glow = ModBlocks.GLOWBRICK.get().defaultBlockState();
        int top = surfaceY - origin.getY();
        if (top < HEIGHT) {
            return; // too shallow for a shaft (shouldn't happen)
        }
        for (int dy = 0; dy <= top; dy++) {
            // Carve a 3×3 interior; line the 5×5 ring with glowbrick where it's above the chamber.
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos p = origin.offset(scx + dx, dy, scz + dz);
                    boolean ring = Math.abs(dx) == 2 || Math.abs(dz) == 2;
                    if (ring) {
                        if (dy > HEIGHT) {
                            set(level, box, p, glow); // shaft wall above the chamber ceiling
                        }
                    } else {
                        set(level, box, p, air); // interior (incl. punching the ceiling + plaza floor)
                    }
                }
            }
            // The spiral step for this level.
            int[] off = RING[dy % RING.length];
            set(level, box, origin.offset(scx + off[0], dy, scz + off[1]), glow);
        }
        // Glowbrick rim around the hole at the surface.
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (Math.abs(dx) == 2 || Math.abs(dz) == 2) {
                    set(level, box, origin.offset(scx + dx, top, scz + dz), glow);
                }
            }
        }
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
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }
}
