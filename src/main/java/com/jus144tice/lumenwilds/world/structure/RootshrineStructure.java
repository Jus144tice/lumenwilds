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
 * Rootshrine (Phase 8d) — a small, early-reward shrine nestled inside a cage of arching Glowroot roots, with
 * a loot chest at its heart. A single procedural {@link RootshrinePiece} placed on the surface at the chunk
 * centre (same plumbing as the mega Glowroot tree, just small). Generates in the Glowroot Forest.
 */
public class RootshrineStructure extends Structure {

    public static final MapCodec<RootshrineStructure> CODEC = simpleCodec(RootshrineStructure::new);

    public RootshrineStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();
        int y = context.chunkGenerator()
                .getFirstOccupiedHeight(
                        x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new RootshrinePiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.ROOTSHRINE.get();
    }
}
