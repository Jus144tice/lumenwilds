/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

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
