/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Procedural geometry for the <em>mega Glowcap</em> — a town-sized variant of the Sporefall Jungle's
 * Giant Glowcap mushroom (the ordinary one is built by the vanilla {@code huge_brown_mushroom} feature;
 * this giant is a {@code world.structure} so it can span chunks). This is a genuine <strong>mushroom</strong>
 * silhouette, NOT the Glowroot tree shape: a single thick, base-flared stem carrying a broad domed cap
 * <em>shell</em> (hollow underneath, like real gills) of glowing cap blocks, with a Lumen-Crystal-Ore
 * cluster seeded in the moonstone directly beneath it (mirroring the mega Glowroot's underground reward).
 *
 * <p>Block writes go through {@link GlowrootShape.Placer} — the same block-sink abstraction the Glowroot
 * structure/feature use (box-clipping vs. direct writes). Only the interface and the ore-seeding idea are
 * shared; the geometry here is entirely its own.</p>
 */
public final class MegaGlowcapShape {

    private MegaGlowcapShape() {}

    /** Size knobs for the giant mushroom. */
    public record Params(
            double stemRadius,
            int minHeight,
            int extraHeight,
            int baseFlare,
            int capRadius,
            int capRise,
            double capThickness,
            int skirt,
            int oreDepth) {}

    /** The town-sized giant: a ~50-tall stem under a ~44-wide domed cap, ore ~22 deep beneath. */
    public static final Params MEGA = new Params(5.0, 38, 14, 6, 22, 12, 4.5, 3, 22);

    /** Builds a mega Glowcap at {@code origin} (the surface block above the ground). */
    public static void generate(GlowrootShape.Placer placer, RandomSource rand, BlockPos origin, Params p) {
        int cx = origin.getX();
        int cz = origin.getZ();
        int baseY = origin.getY();
        int height = p.minHeight() + rand.nextInt(p.extraHeight());
        BlockState stem = ModBlocks.GIANT_GLOWCAP_STEM.get().defaultBlockState();
        BlockState cap = ModBlocks.GIANT_GLOWCAP_BLOCK.get().defaultBlockState();

        buildStem(placer, cx, baseY, cz, height, stem, p);
        buildCap(placer, cx, baseY + height, cz, cap, p);
        seedOreColumn(placer, rand, cx, baseY, cz, p);
    }

    /** A solid stem that flares out near the ground (a mushroom foot), straight above. */
    private static void buildStem(
            GlowrootShape.Placer placer, int cx, int baseY, int cz, int height, BlockState stem, Params p) {
        for (int dy = 0; dy < height; dy++) {
            double flare = dy < p.baseFlare() ? (p.baseFlare() - dy) * 0.45 : 0.0;
            fillDisc(placer, cx, baseY + dy, cz, p.stemRadius() + flare, stem);
        }
    }

    /**
     * A broad domed cap built as a SHELL: each rising layer is an annulus whose radius shrinks toward the
     * top, so the underside is hollow (the gill cavity) and the glowing crown lights it. A short downward
     * skirt at the rim gives the classic overhanging-mushroom flare.
     */
    private static void buildCap(GlowrootShape.Placer placer, int cx, int capBaseY, int cz, BlockState cap, Params p) {
        for (int dy = 0; dy <= p.capRise(); dy++) {
            double t = (double) dy / p.capRise();
            double outerR = p.capRadius() * Math.sqrt(Math.max(0.0, 1.0 - t * t));
            fillRing(placer, cx, capBaseY + dy, cz, outerR, p.capThickness(), cap);
        }
        for (int dy = 1; dy <= p.skirt(); dy++) {
            double outerR = p.capRadius() - dy * 0.6;
            fillRing(placer, cx, capBaseY - dy, cz, outerR, p.capThickness() * 0.8, cap);
        }
    }

    /**
     * Seeds a dense Lumen-Crystal-Ore cluster in the rock under the stem — Moonstone becomes Lumen Crystal
     * Ore, Deep Moonstone becomes Deep Lumen Crystal Ore — denser toward the centre. The same buried reward
     * the mega Glowroot leaves beneath its roots.
     */
    private static void seedOreColumn(
            GlowrootShape.Placer placer, RandomSource random, int cx, int baseY, int cz, Params p) {
        BlockState ore = ModBlocks.LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        BlockState deepOre = ModBlocks.DEEP_LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        double radius = p.stemRadius() + 3.0;
        int ir = (int) Math.ceil(radius);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dy = -1; dy >= -p.oreDepth(); dy--) {
            int y = baseY + dy;
            if (y <= placer.minY()) {
                break;
            }
            for (int dx = -ir; dx <= ir; dx++) {
                for (int dz = -ir; dz <= ir; dz++) {
                    double distSqr = dx * dx + dz * dz;
                    if (distSqr > radius * radius) {
                        continue;
                    }
                    double chance = 0.5 * (1.0 - Math.sqrt(distSqr) / (radius + 1.0));
                    if (random.nextDouble() >= chance) {
                        continue;
                    }
                    cursor.set(cx + dx, y, cz + dz);
                    BlockState here = placer.getState(cursor);
                    if (here.is(ModBlocks.MOONSTONE.get())) {
                        placer.set(cursor.immutable(), ore);
                    } else if (here.is(ModBlocks.DEEP_MOONSTONE.get())) {
                        placer.set(cursor.immutable(), deepOre);
                    }
                }
            }
        }
    }

    /** Fills a solid horizontal disc of {@code radius} at height {@code y}. */
    private static void fillDisc(GlowrootShape.Placer placer, int cx, int y, int cz, double radius, BlockState state) {
        int ir = (int) Math.ceil(radius);
        double r2 = radius * radius;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -ir; dx <= ir; dx++) {
            for (int dz = -ir; dz <= ir; dz++) {
                if (dx * dx + dz * dz <= r2) {
                    cursor.set(cx + dx, y, cz + dz);
                    placer.set(cursor.immutable(), state);
                }
            }
        }
    }

    /** Fills a horizontal annulus (ring) — radius in [{@code outerR - thickness}, {@code outerR}]. */
    private static void fillRing(
            GlowrootShape.Placer placer, int cx, int y, int cz, double outerR, double thickness, BlockState state) {
        if (outerR <= 0) {
            return;
        }
        int ir = (int) Math.ceil(outerR);
        double innerR = Math.max(0.0, outerR - thickness);
        double r2 = outerR * outerR;
        double ir2 = innerR * innerR;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -ir; dx <= ir; dx++) {
            for (int dz = -ir; dz <= ir; dz++) {
                double d2 = dx * dx + dz * dz;
                if (d2 <= r2 && d2 >= ir2) {
                    cursor.set(cx + dx, y, cz + dz);
                    placer.set(cursor.immutable(), state);
                }
            }
        }
    }
}
