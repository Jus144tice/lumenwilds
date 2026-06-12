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
 * taproots, spreading leafy branches, and a broad dual-dome canopy (optionally a Lumen-Crystal-Ore root
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
            int branches,
            int branchesRand,
            int branchLen,
            int branchLenRand,
            double branchThick,
            int alongBlob,
            int endBlob,
            int crownHoriz,
            int crownVert,
            boolean ore) {}

    // alongBlob/endBlob/crownHoriz tuned so every leaf is within 6 leaf-steps of a log (see #leaves()):
    // end-blob ≤3, along-blob ≤4 (on a log line), crown horiz ≤ trunkRadius+4. The wide canopy comes from the
    // log-supported branch blobs, not a giant trunk-only crown.
    public static final Params MEGA =
            new Params(10.0, 72, 18, 32, 12, 6, 14, 8, 3.2, 16, 8, 16, 8, 2.6, 4, 2, 14, 10, true);

    public static final Params MEDIUM = new Params(2.0, 14, 8, 6, 4, 3, 4, 3, 1.6, 6, 4, 5, 3, 1.6, 2, 2, 6, 4, false);

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
        buildBranchesAndCrown(placer, rand, cx, baseY, cz, height, log, p);
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

    private static void buildBranchesAndCrown(
            Placer placer, RandomSource random, int cx, int baseY, int cz, int height, BlockState log, Params p) {
        int branches = p.branches() + random.nextInt(p.branchesRand());
        int firstBranchY = baseY + (int) (height * 0.40);
        int top = baseY + height;
        for (int k = 0; k < branches; k++) {
            double ang = (k / (double) branches) * Math.PI * 2.0 + random.nextDouble() * 0.6;
            double dirX = Math.cos(ang);
            double dirZ = Math.sin(ang);
            double x = cx;
            double z = cz;
            double y = firstBranchY + random.nextInt(Math.max(1, top - firstBranchY));
            int len = p.branchLen() + random.nextInt(p.branchLenRand());
            for (int i = 0; i < len; i++) {
                x += dirX;
                z += dirZ;
                double frac = i / (double) len;
                y += frac < 0.4 ? 0.8 : (frac < 0.75 ? 0.15 : -0.2);
                double r = Math.max(1.0, p.branchThick() - frac * (p.branchThick() * 0.7));
                fillDisc(placer, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), r, log, false);
                if (frac > 0.4 && i % 2 == 0) {
                    leafBlob(placer, (int) Math.round(x), (int) Math.round(y), (int) Math.round(z), p.alongBlob());
                }
            }
            leafBlob(
                    placer,
                    (int) Math.round(x),
                    (int) Math.round(y),
                    (int) Math.round(z),
                    p.endBlob() + random.nextInt(2));
        }
        crown(placer, cx, baseY + (int) (height * 0.86), cz, p.crownHoriz(), p.crownVert());
        crown(placer, cx, baseY + (int) (height * 0.72), cz, (int) (p.crownHoriz() * 0.75), (int)
                (p.crownVert() * 0.7));
    }

    private static void crown(Placer placer, int cx, int cy, int cz, int horiz, int vert) {
        if (vert <= 0 || horiz <= 0) {
            return;
        }
        for (int dy = -vert; dy <= vert; dy++) {
            double layerR = horiz * Math.sqrt(Math.max(0.0, 1.0 - (double) (dy * dy) / (double) (vert * vert)));
            fillDisc(placer, cx, cy + dy, cz, layerR, leaves(), true);
        }
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
        // real leaf→log distance. The Params are sized so every leaf is within 6 leaf-steps of a log (end-blob
        // r≤3 → 3·√3≈5.2; along-blob r≤4 on a log line → 4·√2≈5.7; crown r ≤ trunkRadius+4 → 4·√2≈5.7), so
        // ZERO leaves decay on generation — they only decay if you cut the supporting logs. Decay is harmless
        // because the leaf loot table is a real leaves table (saplings/sticks/mostly nothing), not drop-self.
        return ModBlocks.GLOWROOT_LEAVES.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, 7);
    }
}
