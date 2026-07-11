/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.world.structure;

import com.jus144tice.lumenwilds.registry.ModStructures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * Duskglass Ruins (v1.7.0) — a ruined Dusk-portal site: a broken Duskglass frame with small lava pools nearby
 * (Duskglass is quenched lava) and a chest of Lumenwater buckets + treasure. Generates in the Lumenwilds and the
 * Nether (the Dusk portal's two realms). One procedural {@link DuskglassRuinsPiece}.
 *
 * <p>Placement is mode-split via a {@code nether} codec flag (like vanilla ruined_portal vs ruined_portal_nether):
 * open dims anchor to the surface heightmap; the enclosed Nether scans the read-only noise column
 * ({@code getBaseColumn}, chunk-safe) for a floored air pocket below the bedrock ceiling.</p>
 */
public class DuskglassRuinsStructure extends Structure {

    public static final MapCodec<DuskglassRuinsStructure> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    settingsCodec(inst),
                    com.mojang.serialization.Codec.BOOL
                            .optionalFieldOf("nether", false)
                            .forGetter(s -> s.nether))
            .apply(inst, DuskglassRuinsStructure::new));

    /** Top of the Nether scan (below the ~y127 bedrock ceiling) and minimum open run for a usable pocket. */
    private static final int NETHER_SCAN_TOP = 116;

    private static final int NETHER_MIN_OPEN = 4;

    private final boolean nether;

    public DuskglassRuinsStructure(Structure.StructureSettings settings, boolean nether) {
        super(settings);
        this.nether = nether;
    }

    @Override
    protected Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunk = context.chunkPos();
        int x = chunk.getMiddleBlockX();
        int z = chunk.getMiddleBlockZ();

        int y;
        if (nether) {
            int floor = netherFloor(context, x, z);
            if (floor == Integer.MIN_VALUE) {
                return Optional.empty(); // no floored pocket here — skip this chunk
            }
            y = floor;
        } else {
            y = context.chunkGenerator()
                    .getFirstOccupiedHeight(
                            x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
        }

        BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(
                new Structure.GenerationStub(origin, builder -> builder.addPiece(new DuskglassRuinsPiece(origin))));
    }

    /** Highest floored, open pocket in the playable Nether band (solid floor with ≥4 open cells above it). */
    private static int netherFloor(Structure.GenerationContext context, int x, int z) {
        NoiseColumn col = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());
        int bottom = context.heightAccessor().getMinBuildHeight() + 8;
        int open = 0;
        for (int y = NETHER_SCAN_TOP; y >= bottom; y--) {
            BlockState s = col.getBlock(y);
            boolean isOpen = s.isAir() || !s.getFluidState().isEmpty();
            if (isOpen) {
                open++;
            } else {
                if (open >= NETHER_MIN_OPEN) {
                    return y + 1; // floor = the first open cell above this solid block
                }
                open = 0;
            }
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.DUSKGLASS_RUINS.get();
    }
}
