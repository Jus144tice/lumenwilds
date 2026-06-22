/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.common.Tags;

/**
 * Shared ruin processors for the Vestige City structures (Phase 10b+). The Lumenwright cities are never
 * pristine — every piece runs its clean layout through these helpers so the world looks like it reclaimed
 * the ruins: the glowbrick fades intact → cracked → ancient (so light dies out across a ruin), chunks go
 * missing, and nature creeps back in (glowvine, lumen grass, moonloam, the odd moonblossom/lumenbulb).
 *
 * <p>All writes are {@code writeBox}-clipped (chunk-safe) and all randomness is drawn from the caller's
 * position-seeded RNG, so a ruin is deterministic per world seed. This is the single place to tune the
 * decay/overgrowth feel for the whole city family.</p>
 */
public final class VestigeDecay {

    private VestigeDecay() {}

    /**
     * A weathered glowbrick: mostly intact ({@link ModBlocks#GLOWBRICK} light 6), often cracked (light 3),
     * sometimes ancient (light 1). Use this instead of the plain block so a wall visibly dims as it ages.
     */
    public static BlockState glowbrick(RandomSource rand) {
        int r = rand.nextInt(100);
        if (r < 58) {
            return ModBlocks.GLOWBRICK.get().defaultBlockState();
        }
        if (r < 84) {
            return ModBlocks.CRACKED_GLOWBRICK.get().defaultBlockState();
        }
        return ModBlocks.ANCIENT_GLOWBRICK.get().defaultBlockState();
    }

    /** A darker, almost-dead glowbrick mix for the oldest/sunken parts of a ruin. */
    public static BlockState ancientGlowbrick(RandomSource rand) {
        return rand.nextInt(3) == 0
                ? ModBlocks.CRACKED_GLOWBRICK.get().defaultBlockState()
                : ModBlocks.ANCIENT_GLOWBRICK.get().defaultBlockState();
    }

    /** Loose ruin rubble: cobbled/cracked/mossy/rooted moonstone + broken glowbrick chunks. */
    public static BlockState rubble(RandomSource rand) {
        return switch (rand.nextInt(6)) {
            case 0 -> ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState();
            case 1 -> ModBlocks.CRACKED_MOONSTONE_BRICKS.get().defaultBlockState();
            case 2 -> ModBlocks.MOSSY_MOONSTONE_BRICKS.get().defaultBlockState();
            case 3 -> ModBlocks.ROOTED_MOONSTONE.get().defaultBlockState();
            case 4 -> ModBlocks.CRACKED_GLOWBRICK.get().defaultBlockState();
            default -> ModBlocks.ANCIENT_GLOWBRICK.get().defaultBlockState();
        };
    }

    /**
     * Places a weathered glowbrick at {@code pos} unless this block has crumbled away. {@code missingPct} is
     * the chance (0–100) the block is simply gone (a missing chunk of wall/floor).
     */
    public static void decayedGlowbrick(
            WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos pos, int missingPct) {
        if (rand.nextInt(100) < missingPct) {
            return;
        }
        set(level, box, pos, glowbrick(rand));
    }

    /**
     * A chance to creep overgrowth onto a ruin block. Pass the air position just <em>above</em> a solid floor
     * (drops lumen grass / moonloam / the odd moonblossom) or beside/under a ceiling (hangs a glowvine). Only
     * writes into air, only inside the box.
     */
    public static void overgrow(WorldGenLevel level, BoundingBox box, RandomSource rand, BlockPos airPos, int pct) {
        if (rand.nextInt(100) >= pct || !box.isInside(airPos)) {
            return;
        }
        if (!level.getBlockState(airPos).isAir()) {
            return;
        }
        BlockState below = level.getBlockState(airPos.below());
        if (!below.isAir()) {
            // Ground cover.
            BlockState cover =
                    switch (rand.nextInt(8)) {
                        case 0, 1, 2 -> ModBlocks.LUMEN_GRASS_BLOCK.get().defaultBlockState();
                        case 3, 4 -> ModBlocks.GLOW_FERN.get().defaultBlockState();
                        case 5 -> ModBlocks.MOONBLOSSOM.get().defaultBlockState();
                        default -> ModBlocks.GLOWVINE.get().defaultBlockState();
                    };
            set(level, box, airPos, cover);
        } else if (!level.getBlockState(airPos.above()).isAir()) {
            // Hanging from a ceiling.
            set(level, box, airPos, ModBlocks.GLOWVINE.get().defaultBlockState());
        }
    }

    /** Fills {@code fill} from just below {@code fromY} down through replaceable blocks until solid ground. */
    public static void fillFoundation(
            WorldGenLevel level, BoundingBox box, int x, int fromY, int z, BlockState fill, int maxDepth) {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dy = 1; dy <= maxDepth; dy++) {
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

    /**
     * Like {@link #fillFoundation} but with a weathered, mixed material (crumbling moonstone/moonloam) so a
     * ruin's foundations on a slope read as <b>ancient broken supports half-buried in the hill</b> rather than
     * clean grey pillars — and the deepest reaches go to dirt/cobble so they blend with the ground. Use this
     * for the surface ruins' foundations.
     */
    public static void weatheredFoundation(
            WorldGenLevel level, BoundingBox box, int x, int fromY, int z, RandomSource rand, int maxDepth) {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int dy = 1; dy <= maxDepth; dy++) {
            p.set(x, fromY - dy, z);
            if (!box.isInside(p)) {
                break;
            }
            if (!level.getBlockState(p).canBeReplaced()) {
                break; // reached solid ground
            }
            level.setBlock(p, foundationBlock(rand, dy), 2);
        }
    }

    /** Weathered foundation material — more dirt/cobble the deeper it goes, so columns blend into the ground. */
    private static BlockState foundationBlock(RandomSource rand, int depth) {
        if (depth >= 4 && rand.nextInt(3) == 0) {
            return ModBlocks.MOONLOAM.get().defaultBlockState();
        }
        return switch (rand.nextInt(6)) {
            case 0, 1 -> ModBlocks.COBBLED_MOONSTONE.get().defaultBlockState();
            case 2 -> ModBlocks.CRACKED_MOONSTONE_BRICKS.get().defaultBlockState();
            case 3 -> ModBlocks.MOSSY_MOONSTONE_BRICKS.get().defaultBlockState();
            case 4 -> ModBlocks.ROOTED_MOONSTONE.get().defaultBlockState();
            default -> ModBlocks.MOONLOAM.get().defaultBlockState();
        };
    }

    /**
     * Clears the ruin's footprint <em>before</em> it builds so a Lumenwright remnant <b>takes precedence over
     * whatever generated there first</b> — trees growing through it, a vanilla/modded structure, an overlapping
     * Lumenwilds ruin. Iterates only the current chunk's {@code writeBox} intersected with the piece's
     * {@code pieceBox} (chunk-safe — never touches a neighbour chunk), from {@code minClearY} up to the box top,
     * and removes every <em>non-terrain</em> solid (trees, planks, foreign structure blocks) to air. Natural
     * terrain (stone/dirt/sand/ores + the Lumenwilds ground blocks) and fluids are <b>left intact</b>, so the
     * piece's own foundation refills as normal and caves/ground below {@code minClearY} are never gutted.
     */
    public static void clearArea(WorldGenLevel level, BoundingBox writeBox, BoundingBox pieceBox, int minClearY) {
        int x0 = Math.max(writeBox.minX(), pieceBox.minX());
        int x1 = Math.min(writeBox.maxX(), pieceBox.maxX());
        int z0 = Math.max(writeBox.minZ(), pieceBox.minZ());
        int z1 = Math.min(writeBox.maxZ(), pieceBox.maxZ());
        int y0 = Math.max(Math.max(writeBox.minY(), pieceBox.minY()), minClearY);
        int y1 = Math.min(writeBox.maxY(), pieceBox.maxY());
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
                for (int y = y0; y <= y1; y++) {
                    p.set(x, y, z);
                    if (isClearable(level.getBlockState(p))) {
                        level.setBlock(p, air, 2);
                    }
                }
            }
        }
    }

    /** A block the ruin should wipe from its air column: a non-terrain solid (tree/plant/foreign structure). */
    private static boolean isClearable(BlockState s) {
        if (s.isAir() || !s.getFluidState().isEmpty()) {
            return false;
        }
        if (s.is(BlockTags.LOGS) || s.is(BlockTags.LEAVES) || s.is(BlockTags.SAPLINGS)) {
            return true; // trees growing through the ruin (the common case)
        }
        return !isNaturalTerrain(s);
    }

    /** Natural worldgen ground (vanilla + Lumenwilds) — never cleared, so the ruin sits in real terrain. */
    private static boolean isNaturalTerrain(BlockState s) {
        if (s.is(BlockTags.DIRT)
                || s.is(BlockTags.BASE_STONE_OVERWORLD)
                || s.is(BlockTags.SAND)
                || s.is(Tags.Blocks.ORES)
                || s.is(Tags.Blocks.GRAVELS)
                || s.is(Tags.Blocks.STONES)) {
            return true;
        }
        Block b = s.getBlock();
        return b == ModBlocks.MOONSTONE.get()
                || b == ModBlocks.DEEP_MOONSTONE.get()
                || b == ModBlocks.MOONLOAM.get()
                || b == ModBlocks.LUMEN_GRASS_BLOCK.get()
                || b == ModBlocks.VEINSTONE.get()
                || b == ModBlocks.PALE_TUFF.get()
                || b == ModBlocks.LUMENSAND.get()
                || b == ModBlocks.LUMEN_CRYSTAL_ORE.get()
                || b == ModBlocks.DEEP_LUMEN_CRYSTAL_ORE.get()
                || b == ModBlocks.LUMINITE_ORE.get()
                || b == ModBlocks.DEEP_LUMINITE_ORE.get();
    }

    /** Box-clipped block set (no-op outside the write box, so chunk-gen never reaches a neighbour). */
    public static void set(WorldGenLevel level, BoundingBox box, BlockPos p, BlockState state) {
        if (box.isInside(p)) {
            level.setBlock(p, state, 2);
        }
    }

    public static BlockState air() {
        return Blocks.AIR.defaultBlockState();
    }
}
