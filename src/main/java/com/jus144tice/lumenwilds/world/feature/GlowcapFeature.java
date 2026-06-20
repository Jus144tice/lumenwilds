/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.feature;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * The ordinary Sporefall Jungle <b>Glowcap</b> mushroom (Phase 9 — replaces the flat vanilla
 * {@code huge_brown_mushroom}). A bell/dome-shaped glowing cap on a slim stem, with real <b>variation</b>:
 * each rolls a size (small / medium / large) and one of three cap colours (the original red, azure, violet),
 * so the jungle reads as a varied mushroom forest rather than identical flat slabs. The town-sized version is
 * the rare mega-Glowcap structure. Chunk-safe (cap reach ≤ ~8). Bound to {@code ModFeatures#GLOWCAP}.
 */
public class GlowcapFeature extends Feature<NoneFeatureConfiguration> {

    public GlowcapFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = context.random();
        if (!isGround(level.getBlockState(origin.below()))) {
            return false;
        }

        int height;
        int capR;
        int capRise;
        double s = rand.nextDouble();
        if (s < 0.5) {
            height = 3 + rand.nextInt(2); // small toadstool
            capR = 2;
            capRise = 2;
        } else if (s < 0.85) {
            height = 5 + rand.nextInt(3); // medium
            capR = 3 + rand.nextInt(2);
            capRise = 3;
        } else {
            height = 8 + rand.nextInt(5); // large
            capR = 5 + rand.nextInt(2);
            capRise = 4;
        }

        BlockState stem = ModBlocks.GIANT_GLOWCAP_STEM.get().defaultBlockState();
        BlockState cap = pickCap(rand);
        int cx = origin.getX();
        int cz = origin.getZ();
        int baseY = origin.getY();

        // Slim stem (a little thicker at the foot).
        for (int dy = 0; dy < height; dy++) {
            fillDisc(level, cx, baseY + dy, cz, dy == 0 ? 1.4 : 0.9, stem);
        }

        // Bell/dome cap: widest at the rim, doming up — clearly not a flat slab.
        int capBaseY = baseY + height;
        for (int dy = 0; dy <= capRise; dy++) {
            double t = (double) dy / capRise;
            double r = capR * Math.sqrt(Math.max(0.0, 1.0 - t * t));
            fillDisc(level, cx, capBaseY + dy, cz, r, cap);
        }
        // A downturned rim (the bell's overhang).
        fillRing(level, cx, capBaseY - 1, cz, capR + 0.4, 1.2, cap);
        return true;
    }

    private static BlockState pickCap(RandomSource rand) {
        int c = rand.nextInt(3);
        return (c == 0
                        ? ModBlocks.GIANT_GLOWCAP_BLOCK
                        : c == 1 ? ModBlocks.GIANT_GLOWCAP_AZURE : ModBlocks.GIANT_GLOWCAP_VIOLET)
                .get()
                .defaultBlockState();
    }

    private static boolean isGround(BlockState state) {
        // Surface soil + any Lumenwilds stone floor (so underground glowcaps grow on deep cave floors too, E3).
        return state.is(ModBlocks.LUMEN_GRASS_BLOCK.get())
                || state.is(ModBlocks.MOONLOAM.get())
                || state.is(ModBlocks.MOONSTONE.get())
                || state.is(ModBlocks.DEEP_MOONSTONE.get())
                || state.is(ModBlocks.VEINSTONE.get())
                || state.is(ModBlocks.PALE_TUFF.get());
    }

    private static void fillDisc(WorldGenLevel level, int cx, int y, int cz, double radius, BlockState state) {
        int ir = (int) Math.ceil(radius);
        double r2 = radius * radius;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -ir; dx <= ir; dx++) {
            for (int dz = -ir; dz <= ir; dz++) {
                if (dx * dx + dz * dz <= r2) {
                    cursor.set(cx + dx, y, cz + dz);
                    if (level.getBlockState(cursor).canBeReplaced()) {
                        level.setBlock(cursor, state, 2);
                    }
                }
            }
        }
    }

    private static void fillRing(
            WorldGenLevel level, int cx, int y, int cz, double outerR, double thickness, BlockState state) {
        int ir = (int) Math.ceil(outerR);
        double r2 = outerR * outerR;
        double ir2 = Math.max(0.0, outerR - thickness) * Math.max(0.0, outerR - thickness);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -ir; dx <= ir; dx++) {
            for (int dz = -ir; dz <= ir; dz++) {
                double d2 = dx * dx + dz * dz;
                if (d2 <= r2 && d2 >= ir2) {
                    cursor.set(cx + dx, y, cz + dz);
                    if (level.getBlockState(cursor).canBeReplaced()) {
                        level.setBlock(cursor, state, 2);
                    }
                }
            }
        }
    }
}
