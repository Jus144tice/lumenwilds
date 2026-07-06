/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModStructures;
import com.jus144tice.lumenwilds.world.feature.GlowrootShape;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * The Glowroot mega tree piece. {@link #postProcess} runs once per chunk the tree overlaps; it
 * regenerates the whole tree from a position-seeded RNG (so every chunk's slice is identical) but writes
 * only inside the per-chunk {@code writeBox} — so the giant spans chunks with no "far chunk" errors. The
 * geometry itself lives in {@link GlowrootShape} (shared with the ordinary 2×2 Glowroot tree feature).
 */
public class GlowrootTreePiece extends StructurePiece {

    private static final int HORIZONTAL_REACH = 34; // must enclose the ~50-wide canopy + roots
    private static final int VERTICAL_TOP = GlowrootShape.MEGA.minHeight() + GlowrootShape.MEGA.extraHeight() + 12;
    // Enclose the deepest geometry: taproots (rootDepth) OR a buttress tendril propping a root that reached out
    // over a gully (GlowrootShape#dropTendril drops up to 56). writeBox isn't Y-clamped so tendrils render either
    // way, but the declared box should honestly cover them.
    private static final int VERTICAL_BOTTOM = Math.max(GlowrootShape.MEGA.rootDepth(), 56) + 2;

    private final BlockPos origin;

    public GlowrootTreePiece(BlockPos origin) {
        super(ModStructures.GLOWROOT_TREE_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public GlowrootTreePiece(CompoundTag tag) {
        super(ModStructures.GLOWROOT_TREE_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("ox"), tag.getInt("oy"), tag.getInt("oz"));
    }

    private static BoundingBox boxAround(BlockPos o) {
        return new BoundingBox(
                o.getX() - HORIZONTAL_REACH,
                o.getY() - VERTICAL_BOTTOM,
                o.getZ() - HORIZONTAL_REACH,
                o.getX() + HORIZONTAL_REACH,
                o.getY() + VERTICAL_TOP,
                o.getZ() + HORIZONTAL_REACH);
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

        GlowrootShape.Placer placer = new GlowrootShape.Placer() {
            @Override
            public void set(BlockPos p, BlockState state) {
                if (writeBox.isInside(p)) {
                    level.setBlock(p, state, 2);
                }
            }

            @Override
            public BlockState getState(BlockPos p) {
                return level.getBlockState(p);
            }

            @Override
            public int minY() {
                return level.getMinBuildHeight() + 1;
            }
        };

        GlowrootShape.generate(placer, rand, origin, GlowrootShape.MEGA);
    }
}
