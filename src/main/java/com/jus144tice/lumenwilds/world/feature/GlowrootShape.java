/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared procedural geometry for Glowroot trees — a tapering trunk, arching buttress roots, deep
 * taproots, and a broad, full canopy built from tiers of leafy branches (optionally a Lumen-Crystal-Ore root
 * cluster). One algorithm drives BOTH the rare {@code MEGA} variant (the town-sized
 * {@code world.structure} version) and the ordinary {@code MEDIUM} 2×2 tree (a normal worldgen feature),
 * just with different {@link Params}, so they read as the same species at every scale and don't look
 * like vanilla dark oak.
 *
 * <p>Block writes go through a {@link Placer}: the structure box-clips to the current chunk; the feature
 * writes directly.</p>
 */
public final class GlowrootShape {

    private GlowrootShape() {}

    /** Sink for the geometry — abstracts the structure's box-clipping vs. the feature's direct writes. */
    public interface Placer {
        void set(BlockPos pos, BlockState state);

        BlockState getState(BlockPos pos);

        int minY();
    }

    /**
     * Size knobs. {@code MEGA} is the structure giant; {@code MEDIUM} is the natural 2×2 tree (kept under
     * a ~13-block reach so it stays within a feature's chunk-write limit).
     */
    public record Params(
            double trunkRadius,
            int minHeight,
            int extraHeight,
            int rootDepth,
            int rootArms,
            int rootArmsRand,
            int rootLen,
            int rootLenRand,
            double rootThick,
            int branches, // branches PER canopy tier
            int branchesRand,
            int branchLen,
            int branchLenRand,
            double branchThick,
            int alongBlob,
            int endBlob,
            boolean ore) {}

    // The canopy is built from TIERS of branches (see #buildCanopy) — every leaf blob is seated on a branch-log,
    // and blob radius is capped at 3 so a leaf is at most 3·√3≈5.2 orthogonal leaf-steps from that log (≤6 → 0
    // gen-decay). A big, full canopy comes from MANY overlapping branch-blobs across tiers, NOT a trunk-only dome.
    public static final Params MEGA = new Params(10.0, 72, 18, 32, 12, 6, 14, 8, 3.2, 10, 6, 16, 8, 2.6, 3, 3, true);

    public static final Params MEDIUM = new Params(2.0, 14, 8, 6, 4, 3, 5, 3, 1.6, 5, 3, 5, 3, 1.6, 2, 2, false);

    /** Builds a Glowroot tree of the given size at {@code origin} (the surface block above the ground). */
    public static void generate(Placer placer, RandomSource rand, BlockPos origin, Params p) {
        int cx = origin.getX();
        int cz = origin.getZ();
        int baseY = origin.getY();
        int height = p.minHeight() + rand.nextInt(p.extraHeight());
        BlockState log = ModBlocks.GLOWROOT_LOG.get().defaultBlockState();

        buildTrunk(placer, cx, baseY, cz, height, log, p);
        buildButtressRoots(placer, rand, cx, baseY, cz, log, p);
        buildTaproots(placer, rand, cx, baseY, cz, log, p);
        if (p.ore()) {
            seedOreColumn(placer, rand, cx, baseY, cz, p);
        }
        buildCanopy(placer, rand, cx, baseY, cz, height, log, p);
    }

    private static void buildTrunk(Placer placer, int cx, int baseY, int cz, int height, BlockState log, Params p) {
        double taper = p.trunkRadius() * 0.4;
        double minR = Math.max(1.0, p.trunkRadius() < 4 ? 1.0 : 3.0);
        for (int dy = 0; dy < height; dy++) {
            double t = (double) dy / height;
            double r = p.trunkRadius() - (t > 0.6 ? (t - 0.6) / 0.4 * taper : 0.0);
            fillDisc(placer, cx, baseY + dy, cz, Math.max(minR, r), log, false);
        }
    }

    private static void buildButtressRoots(
            Placer placer, RandomSource random, int cx, int baseY, int cz, BlockState log, Params p) {
        int arms = p.rootArms() + random.nextInt(p.rootArmsRand());
        for (int k = 0; k < arms; k++) {
            double ang = (k / (double) arms) * Math.PI * 2.0 + random.nextDouble() * 0.5;
            double dirX = Math.cos(ang);
            double dirZ = Math.sin(ang);
            int len = p.rootLen() + random.nextInt(p.rootLenRand());
            double x = cx + dirX * (p.trunkRadius() - 1.0);
            double z = cz + dirZ * (p.trunkRadius() - 1.0);
            double y = baseY + 3 + random.nextInt(3);
            for (int i = 0; i < len; i++) {
                x += dirX;
                z += dirZ;
                double frac = i / (double) len;
                if (frac < 0.4) {
                    y += (random.nextDouble() - 0.45) * 0.6;
                } else {
                    y -= 1.0 + (random.nextDouble() - 0.5) * 0.4;
                }
                if (y < baseY - 10) {
                    break;
                }
                double r = Math.max(1.2, p.rootThick() - frac * (p.rootThick() * 0.55));
                fillDisc(placer, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), r, log, false);
            }
        }
    }

    private static void buildTaproots(
            Placer placer, RandomSource random, int cx, int baseY, int cz, BlockState log, Params p) {
        int taproots = 3 + random.nextInt(3);
        for (int k = 0; k < taproots; k++) {
            double ang = random.nextDouble() * Math.PI * 2.0;
            double x = cx + Math.cos(ang) * (p.trunkRadius() * 0.4);
            double z = cz + Math.sin(ang) * (p.trunkRadius() * 0.4);
            int depth = Math.max(6, p.rootDepth() / 2) + random.nextInt(Math.max(1, p.rootDepth() / 2));
            for (int i = 0; i < depth; i++) {
                int y = baseY - i;
                if (y <= placer.minY()) {
                    break;
                }
                x += (random.nextDouble() - 0.5) * 0.7;
                z += (random.nextDouble() - 0.5) * 0.7;
                fillDisc(
                        placer,
                        (int) Math.round(x),
                        y,
                        (int) Math.round(z),
                        Math.max(1.0, p.rootThick() * 0.55),
                        log,
                        false);
            }
        }
    }

    private static void seedOreColumn(Placer placer, RandomSource random, int cx, int baseY, int cz, Params p) {
        BlockState ore = ModBlocks.LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        BlockState deepOre = ModBlocks.DEEP_LUMEN_CRYSTAL_ORE.get().defaultBlockState();
        double radius = p.trunkRadius() + 2.0;
        int ir = (int) Math.ceil(radius);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dy = -1; dy >= -p.rootDepth(); dy--) {
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

    /**
     * Builds the canopy as a stack of branch tiers threading the upper trunk. Each tier sends out branches at
     * staggered angles; the middle tiers reach widest (a rounded dome), the top tapers. Every branch drops
     * radius-≤3 leaf blobs along its length and a blob at the tip — each blob seated on the branch-log it was
     * just drawn over — so all leaves stay within ~5 leaf-steps of a log (0 gen-decay) while the overlapping
     * blobs across tiers fill a wide, full canopy.
     */
    private static void buildCanopy(
            Placer placer, RandomSource random, int cx, int baseY, int cz, int height, BlockState log, Params p) {
        int top = baseY + height;
        int canopyBase = baseY + (int) (height * 0.45);
        int tiers = Math.max(2, height / 18);
        for (int tier = 0; tier < tiers; tier++) {
            double tierFrac = tiers == 1 ? 0.5 : tier / (double) (tiers - 1); // 0 = canopy bottom, 1 = top
            int tierY = canopyBase + (int) ((top - canopyBase) * tierFrac);
            double reachFactor = 0.55 + 0.45 * Math.sin(tierFrac * Math.PI); // middle tiers widest -> dome
            int branchesThisTier = p.branches() + random.nextInt(p.branchesRand());
            double angleOffset = random.nextDouble() * Math.PI * 2.0 + tier * 0.7; // stagger tiers
            for (int k = 0; k < branchesThisTier; k++) {
                double ang = (k / (double) branchesThisTier) * Math.PI * 2.0 + angleOffset + random.nextDouble() * 0.4;
                growBranch(placer, random, cx, tierY, cz, ang, reachFactor, log, p);
            }
        }
        // Cap the very top so the trunk crown isn't bald (the trunk-top log seats this blob).
        leafBlob(placer, cx, top, cz, Math.min(3, p.endBlob() + 1));
    }

    private static void growBranch(
            Placer placer,
            RandomSource random,
            int cx,
            int startY,
            int cz,
            double ang,
            double reachFactor,
            BlockState log,
            Params p) {
        double dirX = Math.cos(ang);
        double dirZ = Math.sin(ang);
        double x = cx;
        double z = cz;
        double y = startY;
        int len = Math.max(3, (int) Math.round((p.branchLen() + random.nextInt(p.branchLenRand())) * reachFactor));
        int alongR = Math.min(3, p.alongBlob());
        for (int i = 0; i < len; i++) {
            x += dirX;
            z += dirZ;
            double frac = i / (double) len;
            y += frac < 0.35 ? 0.7 : (frac < 0.7 ? 0.2 : -0.15); // arch up then level then slight droop
            double r = Math.max(1.0, p.branchThick() - frac * (p.branchThick() * 0.7));
            fillDisc(placer, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), r, log, false);
            if (frac > 0.25 && i % 2 == 0) {
                leafBlob(placer, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), alongR);
            }
        }
        leafBlob(
                placer,
                (int) Math.round(x),
                (int) Math.round(y),
                (int) Math.round(z),
                Math.min(3, p.endBlob() + random.nextInt(2)));
    }

    private static void leafBlob(Placer placer, int cx, int cy, int cz, int radius) {
        BlockState leaves = leaves();
        for (int dy = -radius; dy <= radius; dy++) {
            double layerR = radius * Math.sqrt(Math.max(0.0, 1.0 - (double) (dy * dy) / (double) (radius * radius)));
            fillDisc(placer, cx, cy + dy, cz, layerR, leaves, true);
        }
    }

    private static void fillDisc(
            Placer placer, int cx, int y, int cz, double radius, BlockState state, boolean onlyReplaceable) {
        int ir = (int) Math.ceil(radius);
        double r2 = radius * radius;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -ir; dx <= ir; dx++) {
            for (int dz = -ir; dz <= ir; dz++) {
                if (dx * dx + dz * dz > r2) {
                    continue;
                }
                cursor.set(cx + dx, y, cz + dz);
                if (onlyReplaceable && !placer.getState(cursor).canBeReplaced()) {
                    continue;
                }
                placer.set(cursor.immutable(), state);
            }
        }
    }

    private static BlockState leaves() {
        // NON-persistent (normal decay when the logs are cut), placed at DISTANCE 7 so vanilla recomputes the
        // real leaf→log distance. Every leaf blob in #buildCanopy is seated on a branch-log and is radius ≤3, so
        // the farthest leaf is ≤3·√3≈5.2 orthogonal leaf-steps from that log (≤6) → ZERO leaves decay on
        // generation; they only decay if you cut the supporting logs. Requires the logs to be in #minecraft:logs
        // (data/minecraft/tags/block/logs.json) — without that the distance check finds no trunk and ALL decay.
        // Decay is harmless: the leaf loot is a real leaves table (saplings/sticks/mostly nothing), not drop-self.
        return ModBlocks.GLOWROOT_LEAVES.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, 7);
    }
}
