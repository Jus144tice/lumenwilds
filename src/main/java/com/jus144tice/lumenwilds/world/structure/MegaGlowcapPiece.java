/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModStructures;
import com.jus144tice.lumenwilds.world.feature.GlowrootShape;
import com.jus144tice.lumenwilds.world.feature.MegaGlowcapShape;
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
 * The mega Glowcap piece. {@link #postProcess} runs once per chunk the mushroom overlaps; it regenerates
 * the whole giant from a position-seeded RNG (so every chunk's slice is identical) but writes only inside
 * the per-chunk {@code writeBox} — so the cap spans chunks with no "far chunk" errors. The geometry lives
 * in {@link MegaGlowcapShape}; this reuses {@link GlowrootShape.Placer} purely as the block sink.
 */
public class MegaGlowcapPiece extends StructurePiece {

    private static final int HORIZONTAL_REACH = MegaGlowcapShape.MEGA.capRadius() + 4; // enclose the cap + skirt
    private static final int VERTICAL_TOP = MegaGlowcapShape.MEGA.minHeight()
            + MegaGlowcapShape.MEGA.extraHeight()
            + MegaGlowcapShape.MEGA.capRise()
            + 6;
    private static final int VERTICAL_BOTTOM = MegaGlowcapShape.MEGA.oreDepth() + 2;

    private final BlockPos origin;

    public MegaGlowcapPiece(BlockPos origin) {
        super(ModStructures.MEGA_GLOWCAP_PIECE.get(), 0, boxAround(origin));
        this.origin = origin;
    }

    public MegaGlowcapPiece(CompoundTag tag) {
        super(ModStructures.MEGA_GLOWCAP_PIECE.get(), tag);
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

        MegaGlowcapShape.generate(placer, rand, origin, MegaGlowcapShape.MEGA);
    }
}
