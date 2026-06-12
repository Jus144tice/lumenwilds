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
 * Glasspetal Spires (Phase 8f) — a cluster of tapering crystal towers in the Glasspetal Crags, crowned with
 * Glasspetal Clusters and guarded by Crag Wraiths (via the structure's {@code spawn_overrides}). A single
 * procedural {@link GlasspetalSpiresPiece} at the surface chunk centre.
 */
public class GlasspetalSpiresStructure extends Structure {

    public static final MapCodec<GlasspetalSpiresStructure> CODEC = simpleCodec(GlasspetalSpiresStructure::new);

    public GlasspetalSpiresStructure(Structure.StructureSettings settings) {
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
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new GlasspetalSpiresPiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.GLASSPETAL_SPIRES.get();
    }
}
