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
 * Lumenbound Ruins (Phase 8e) — a ruined Lumenwilds portal site in the <b>Overworld</b>: a broken Lumenbound
 * Stone frame, rubble, and a chest of striker/frame ingredients. The in-world explanation of how to reach the
 * Lumenwilds — a player who finds one can learn the frame material + bootstrap their own portal. A single
 * procedural {@link LumenboundRuinsPiece} at the surface chunk centre.
 */
public class LumenboundRuinsStructure extends Structure {

    public static final MapCodec<LumenboundRuinsStructure> CODEC = simpleCodec(LumenboundRuinsStructure::new);

    public LumenboundRuinsStructure(Structure.StructureSettings settings) {
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
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new LumenboundRuinsPiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.LUMENBOUND_RUINS.get();
    }
}
