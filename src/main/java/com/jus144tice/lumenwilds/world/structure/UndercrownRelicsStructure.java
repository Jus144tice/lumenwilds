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
 * Undercrown Relics (Phase 8g) — a buried dungeon hall in the Undercrown Caverns: a carved chamber with a
 * mob spawner, loot chests, and portal-stabilization (Lumen Anchor) parts. Unlike the surface structures,
 * {@link #findGenerationPoint} picks a <b>deep</b> Y (deterministic per chunk, well below the surface) so the
 * relic generates underground; the {@link UndercrownRelicsPiece} carves its own shell into the rock.
 */
public class UndercrownRelicsStructure extends Structure {

    public static final MapCodec<UndercrownRelicsStructure> CODEC = simpleCodec(UndercrownRelicsStructure::new);

    public UndercrownRelicsStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();
        int surface = context.chunkGenerator()
                .getFirstOccupiedHeight(
                        x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

        int minY = context.heightAccessor().getMinBuildHeight() + 8;
        int maxY = surface - 16;
        if (maxY <= minY) {
            return Optional.empty();
        }
        // Deterministic deep Y per chunk (biased toward the deeper half).
        int span = maxY - minY + 1;
        int y = minY + Math.floorMod(chunk.x * 73856093 ^ chunk.z * 19349663, span) / 2;
        BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new UndercrownRelicsPiece(origin))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.UNDERCROWN_RELICS.get();
    }
}
