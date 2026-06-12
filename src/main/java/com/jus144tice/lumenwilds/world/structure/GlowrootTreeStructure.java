/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModStructures;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * The Glowroot mega tree structure. A single procedural {@link GlowrootTreePiece} placed at the centre
 * of the chunk, on the surface. Being a structure (not a feature) lets the tree span many chunks cleanly
 * — each overlapping chunk writes its own slice via the piece's bounding box.
 */
public class GlowrootTreeStructure extends Structure {

    public static final MapCodec<GlowrootTreeStructure> CODEC = simpleCodec(GlowrootTreeStructure::new);

    public GlowrootTreeStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();
        int y = context.chunkGenerator()
                .getFirstOccupiedHeight(
                        x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new GlowrootTreePiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.GLOWROOT_TREE.get();
    }
}
