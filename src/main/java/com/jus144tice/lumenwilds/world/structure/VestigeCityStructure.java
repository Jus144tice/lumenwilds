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
 * Medium Vestige City (Phase 10d) — the main Lumenwright ruin experience ({@code docs/ancient_cities.txt}):
 * a village-sized dead alien city built around a central plaza. A radial layout (chiseled-glowbrick plaza
 * with a dry crystal fountain + flickering light pylons, four broken roads spoking out to an outer ring of
 * collapsed crescent houses / hollow pods / archways / root chambers), heavily decayed and overgrown via the
 * shared {@link VestigeDecay} processors. Rare. A single procedural {@link VestigeCityPiece} at the surface
 * chunk centre (anchored to {@code OCEAN_FLOOR_WG}).
 */
public class VestigeCityStructure extends Structure {

    public static final MapCodec<VestigeCityStructure> CODEC = simpleCodec(VestigeCityStructure::new);

    public VestigeCityStructure(Structure.StructureSettings settings) {
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
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new VestigeCityPiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.VESTIGE_CITY.get();
    }
}
