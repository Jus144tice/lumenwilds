/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.block.LiftShaftNetwork;
import com.jus144tice.lumenwilds.block.LumenConduitBlock;
import com.jus144tice.lumenwilds.block.LumenFieldProjectorBlock;
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
 *   <li>two side-by-side vertical shafts up to the dais, each built from <b>real, lootable components</b> a
 *       player can reverse-engineer (see {@link #gravityColumn}): a {@link LumenFieldProjectorBlock} whose block
 *       entity floods the column with field at runtime, extended by {@code GravityRepeaterBlock}s spaced so the
 *       16-cell budget terminates the field <em>exactly</em> at each ramp (clean step-in/out, no overshoot).
 *       Ascension's projector sits flush in the chamber floor (projecting up); descent's sits on a short
 *       glowbrick head above the dais (projecting down), so the rider walks in beneath it.</li>
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
    private final boolean naturalCave; // chamber landed on a detected cavern → break it open into the cave
    /** Biome flavor (0 std / 1 overgrown / 2 cracked / 3 sunken) — decided at placement, never read in postProcess. */
    private final int flavor;

    public VestigeMinePiece(BlockPos origin, int surfaceY, boolean naturalCave, int flavor) {
        super(ModStructures.VESTIGE_MINE_PIECE.get(), 0, boxAround(origin, surfaceY));
        this.origin = origin;
        this.surfaceY = surfaceY;
        this.naturalCave = naturalCave;
        this.flavor = flavor;
    }

    public VestigeMinePiece(CompoundTag tag) {
        super(ModStructures.VESTIGE_MINE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
        this.surfaceY = tag.getInt("sy");
        this.naturalCave = tag.getBoolean("cave");
        this.flavor = tag.getInt("flavor");
    }

    /** Headroom carved above the dais so covering terrain never buries it, + room for the marker pylons. */
    private static final int DAIS_CLEAR = 14;

    private static BoundingBox boxAround(BlockPos o, int surfaceY) {
        int top = Math.max(surfaceY, o.getY() + CH_HEIGHT) + DAIS_CLEAR + 4;
        return new BoundingBox(
                o.getX() - REACH, o.getY() - 6, o.getZ() - REACH, o.getX() + REACH, top, o.getZ() + REACH);
    }

    /**
     * Probe the dais column for a natural cavern to drop the mine into (the bible's "Recommended Version" —
     * connect to a real cave when one is below, else use the fixed artificial depth). Uses
     * {@link ChunkGenerator#getBaseColumn} — the same read-only noise-terrain probe vanilla structures use, so
     * there is no chunk-load/"chunk unavailable" risk — and this dimension's caverns are noise caves, so they
     * show up in it. Returns the chamber-floor Y of the highest open pocket (≥{@link #CAVE_MIN_OPEN} open cells
     * over a solid floor) below the surface, or {@link Integer#MIN_VALUE} if none.
     */
    public static int findCaveFloor(
            ChunkGenerator generator,
            int x,
            int z,
            net.minecraft.world.level.LevelHeightAccessor heightAccessor,
            net.minecraft.world.level.levelgen.RandomState randomState,
            int surfaceY) {
        net.minecraft.world.level.NoiseColumn col = generator.getBaseColumn(x, z, heightAccessor, randomState);
        int top = surfaceY - 18;
        int bottom = heightAccessor.getMinBuildHeight() + 16;
        int open = 0;
        for (int y = top; y >= bottom; y--) {
            BlockState s = col.getBlock(y);
            boolean isOpen = s.isAir() || !s.getFluidState().isEmpty(); // air OR (noise) fluid pocket = cavern
            if (isOpen) {
                open++;
            } else {
                if (open >= CAVE_MIN_OPEN) {
                    return y + 1; // chamber floor = the first open cell above this solid floor
                }
                open = 0;
            }
        }
        return Integer.MIN_VALUE;
    }

    /** Minimum vertical run of open cells (over a solid floor) to count as a cavern worth connecting to. */
    private static final int CAVE_MIN_OPEN = 5;

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("ox", origin.getX());
        tag.putInt("oy", origin.getY());
        tag.putInt("oz", origin.getZ());
        tag.putInt("sy", surfaceY);
        tag.putBoolean("cave", naturalCave);
        tag.putInt("flavor", flavor);
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
        if (naturalCave) {
            breach(level, writeBox, rand);
        }
        ribs(level, writeBox);
        machinery(level, writeBox, rand);
        dais(level, writeBox, rand);
        shafts(level, writeBox); // after dais() so the descent projector head survives the dais headroom carve
        flavor(level, writeBox, rand);
    }

    /**
     * When the chamber landed on a real cavern, punch ragged openings through its lower walls so it visibly
     * breaks out into the surrounding cave (where there's solid rock behind, this just reads as a broken alcove).
     */
    private void breach(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int dy = 1; dy <= CH_HEIGHT - 1; dy++) {
            for (int dx = -HALF_W; dx <= HALF_W; dx++) {
                for (int dz = -HALF_L; dz <= HALF_L; dz++) {
                    boolean wall = dx == -HALF_W || dx == HALF_W || dz == -HALF_L || dz == HALF_L;
                    if (wall && rand.nextInt(100) < 35) {
                        set(level, box, origin.offset(dx, dy, dz), air);
                    }
                }
            }
        }
    }

    /**
     * Biome-specific accents so a mine reads as part of where it was dug: <b>Crags</b> bristle with Glasspetal
     * crystal + crystal-block seams; <b>Moonmire</b> mines have seeped Lumenwater pools; <b>forest/jungle</b>
     * mines are invaded by Glowroot-log roots breaking through the walls. All pop-safe (no plants on bare stone).
     */
    private void flavor(WorldGenLevel level, BoundingBox box, RandomSource rand) {
        if (flavor == 2) { // cracked-spire (Crags)
            BlockState cluster = ModBlocks.GLASSPETAL_CLUSTER.get().defaultBlockState();
            BlockState crystal = ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState();
            for (int dx = -HALF_W + 1; dx <= HALF_W - 1; dx++) {
                for (int dz = -HALF_L + 1; dz <= HALF_L - 1; dz++) {
                    if (rand.nextInt(10) == 0) {
                        set(level, box, origin.offset(dx, 1, dz), cluster);
                    }
                }
            }
            set(level, box, origin.offset(0, 1, 3), crystal);
            set(level, box, new BlockPos(origin.getX() - 2, surfaceY + 1, origin.getZ()), cluster);
            set(level, box, new BlockPos(origin.getX() + 2, surfaceY + 1, origin.getZ()), cluster);
        } else if (flavor == 3) { // sunken (Moonmire)
            BlockState water = ModBlocks.LUMENWATER_BLOCK.get().defaultBlockState();
            BlockState crystal = ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState();
            // Seeped pools flush with the chamber floor in the corners (never in the shaft columns).
            int[][] corners = {{-HALF_W + 2, -HALF_L + 2}, {HALF_W - 2, HALF_L - 3}, {-HALF_W + 2, HALF_L - 3}};
            for (int[] c : corners) {
                set(level, box, origin.offset(c[0], 0, c[1]), water);
                set(level, box, origin.offset(c[0] + 1, 0, c[1]), water);
            }
            set(level, box, origin.offset(3, 0, -3), crystal);
        } else if (flavor == 1) { // overgrown (forest/jungle)
            BlockState root = ModBlocks.GLOWROOT_LOG.get().defaultBlockState();
            BlockState leaves = ModBlocks.GLOWROOT_LEAVES.get().defaultBlockState();
            // Roots breaking through the walls/ceiling into the mine.
            for (int dy = 1; dy <= CH_HEIGHT - 1; dy++) {
                set(level, box, origin.offset(-HALF_W + 1, dy, 2), root);
            }
            set(level, box, origin.offset(-HALF_W + 1, CH_HEIGHT - 1, 1), leaves);
            set(level, box, origin.offset(-HALF_W + 2, CH_HEIGHT - 1, 2), leaves);
            for (int dy = 1; dy <= 3; dy++) {
                set(level, box, origin.offset(HALF_W - 1, dy, -2), root);
            }
        }
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
        BlockState glow = ModBlocks.GLOWBRICK.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        int ceilY = origin.getY() + CH_HEIGHT;
        int ascCol = origin.getX() + ASCEND_DX;
        int desCol = origin.getX() + DESCENT_DX;
        int z = origin.getZ();
        int colBottom = origin.getY() + 1; // chamber-floor standing level — step in/out right at the floor
        int ascTop = surfaceY + 1; // ascension rider clears the lip and walks out onto the dais
        int desTop = surfaceY + 2; // descent field top (its projector head sits at +3, so you walk in under it)

        // Carve both 1-wide column cells to AIR (floor → dais) so the projectors can fill them with field at
        // runtime; below the ceiling they're already the open chamber.
        for (int y = colBottom; y <= desTop; y++) {
            setAbsolute(level, box, ascCol, y, z, air);
            setAbsolute(level, box, desCol, y, z, air);
        }
        // Glowbrick casing around the 3-wide slot, between the chamber ceiling and the dais floor.
        for (int y = ceilY; y < surfaceY; y++) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == -2 || dx == 2 || dz == -1 || dz == 1) {
                        setAbsolute(level, box, origin.getX() + dx, y, z + dz, glow);
                    }
                }
            }
        }
        // The divider between the two columns, floor → flush with the dais (mouths stay open at the top).
        for (int y = colBottom; y <= surfaceY; y++) {
            setAbsolute(level, box, origin.getX(), y, z, glow);
        }

        // The working, lootable gravity engines — real components a player can reverse-engineer:
        // Ascension — a Lumen Field Projector flush in the chamber floor projecting UP; repeaters carry it to the dais.
        gravityColumn(level, box, ascCol, z, LumenFieldProjectorBlock.Mode.ASCEND, origin.getY(), colBottom, ascTop);
        // Descent — a projector on a short glowbrick head above the dais projecting DOWN; you walk in beneath it.
        descentHead(level, box, desCol, z, glow);
        gravityColumn(level, box, desCol, z, LumenFieldProjectorBlock.Mode.DESCEND, surfaceY + 3, desTop, colBottom);
    }

    /**
     * Places a working, lootable gravity engine in one column: a {@link LumenFieldProjectorBlock} (its block
     * entity fills the field at runtime) plus {@link com.jus144tice.lumenwilds.block.GravityRepeaterBlock}s
     * spaced so the field terminates <b>exactly</b> at {@code farField} — the last repeater sits one
     * {@link LiftShaftNetwork#RANGE} from the far end (so the 16-cell budget runs out right at the ramp, no
     * overshoot), and a near-anchor within the projector's own reach keeps the chain connected. This is the
     * player-replicable build (projector + a repeater every 16) rendered in the ruin.
     */
    private void gravityColumn(
            WorldGenLevel level,
            BoundingBox box,
            int colX,
            int colZ,
            LumenFieldProjectorBlock.Mode mode,
            int projectorY,
            int nearField,
            int farField) {
        int dir = mode == LumenFieldProjectorBlock.Mode.ASCEND ? 1 : -1;
        int range = LiftShaftNetwork.RANGE;
        setAbsolute(
                level,
                box,
                colX,
                projectorY,
                colZ,
                ModBlocks.LUMEN_FIELD_PROJECTOR
                        .get()
                        .defaultBlockState()
                        .setValue(LumenFieldProjectorBlock.MODE, mode));
        // Near-anchor inside the projector's own reach so the chain always connects back to it.
        int nearAnchor = nearField + dir * (range - 1);
        placeRepeater(level, box, colX, nearAnchor, colZ);
        // Last repeater exactly `range` from the far end (clean ramp), then one every `range` toward the projector.
        for (int h = farField - dir * range; (dir > 0 ? h > nearAnchor : h < nearAnchor); h -= dir * range) {
            placeRepeater(level, box, colX, h, colZ);
        }
    }

    /** A Gravity Repeater embedded in the z+1 casing wall, orthogonally adjacent to the column so the field reads it. */
    private void placeRepeater(WorldGenLevel level, BoundingBox box, int colX, int y, int colZ) {
        setAbsolute(
                level, box, colX, y, colZ + 1, ModBlocks.GRAVITY_REPEATER.get().defaultBlockState());
    }

    /** A short glowbrick head over the descent shaft: its projector sits at the top, the rider walks in beneath it. */
    private void descentHead(WorldGenLevel level, BoundingBox box, int colX, int colZ, BlockState glow) {
        for (int y = surfaceY + 1; y <= surfaceY + 3; y++) {
            setAbsolute(level, box, colX, y, colZ - 1, glow);
            setAbsolute(level, box, colX, y, colZ + 1, glow);
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
                // The two shaft mouths carry the field up to fy+1 (set by shafts()) — leave them alone so the
                // rider can step in/out at dais level; only carve headroom ABOVE the field (from fy+2).
                boolean boreColumn = dz == 0 && (dx == DESCENT_DX || dx == ASCEND_DX);
                boolean divider = dz == 0 && dx == 0;
                int clearFrom = boreColumn ? 2 : 1;
                // Carve away any covering terrain above the dais so it's never buried (no-op where already open).
                for (int dy = clearFrom; dy <= DAIS_CLEAR; dy++) {
                    setAbsolute(level, box, x, fy + dy, z, air);
                }
                if (boreColumn) {
                    continue; // leave the field column + skip the foundation (the shaft drops below it)
                }
                if (divider) {
                    setAbsolute(level, box, x, fy, z, ModBlocks.GLOWBRICK.get().defaultBlockState()); // flush cap
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

        // Four tall glowing pylons at the dais edges — a Lumen-Crystal column capped with a Lumenbulb — so the
        // mine access reads as a beacon from a distance and is never lost among the ruins or terrain.
        int[][] pylons = {{DAIS_R - 1, 0}, {-(DAIS_R - 1), 0}, {0, DAIS_R - 1}, {0, -(DAIS_R - 1)}};
        BlockState crystal = ModBlocks.LUMEN_CRYSTAL_BLOCK.get().defaultBlockState();
        BlockState bulb = ModBlocks.LUMENBULB.get().defaultBlockState();
        for (int[] p : pylons) {
            for (int dy = 1; dy <= 4; dy++) {
                setAbsolute(level, box, origin.getX() + p[0], fy + dy, origin.getZ() + p[1], crystal);
            }
            setAbsolute(level, box, origin.getX() + p[0], fy + 5, origin.getZ() + p[1], bulb);
        }

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
