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
 * Small Vestige Outpost (Phase 10b) — the first, smallest ruin of the vanished Lumenwrights
 * ({@code docs/ancient_cities.txt}): the "arrival edge" of their civilization. A broken glowbrick road,
 * toppled pillars, one collapsed building shell, scattered debris, and a single Ruined Cache. Uncommon, an
 * early hint that something far more advanced once lived here. A single procedural {@link VestigeOutpostPiece}
 * at the surface chunk centre (anchored to {@code OCEAN_FLOOR_WG}, so it sits on the ground, not the sea).
 */
public class VestigeOutpostStructure extends Structure {

    public static final MapCodec<VestigeOutpostStructure> CODEC = simpleCodec(VestigeOutpostStructure::new);

    public VestigeOutpostStructure(Structure.StructureSettings settings) {
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
        // Dry land only — skip seabed placements below the Lumenwater sea level (see VestigeCityStructure).
        if (y <= context.chunkGenerator().getSeaLevel()) {
            return Optional.empty();
        }
        BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new VestigeOutpostPiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.VESTIGE_OUTPOST.get();
    }
}
