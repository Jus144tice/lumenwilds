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
        // Dry land only — the Lumenwilds' surface biomes hold Lumenwater seas, and OCEAN_FLOOR_WG is the seabed,
        // so without this a city would generate submerged. Skip placements at/below sea level.
        if (y <= context.chunkGenerator().getSeaLevel()) {
            return Optional.empty();
        }
        BlockPos origin = new BlockPos(x, y, z);
        // Size tier: ~25% of cities are GRAND (bigger, a central Light Engine + broken spires); else medium.
        int tier = context.random().nextInt(100) < 25 ? 1 : 0;
        // A buried Vestige Vault sits ~22 blocks under the plaza, with a spiral shaft back up (10f.2).
        int vaultY = Math.max(context.heightAccessor().getMinBuildHeight() + 12, y - 22);
        BlockPos vaultOrigin = new BlockPos(x, vaultY, z);
        int spires = tier > 0 ? 1 + context.random().nextInt(2) : 0; // 1–2 spires in a grand city
        int spireSeed = context.random().nextInt();
        // A Lumenwright liftshaft + abandoned mine: a major discovery, so not in every city — grand cities
        // always, medium ~40%. The dais sits offset from the plaza (clear of the central vault shaft).
        boolean mine = tier > 0 || context.random().nextInt(100) < 40;
        double mineAng = context.random().nextDouble() * Math.PI * 2.0;
        int mineDx = (int) Math.round(Math.cos(mineAng) * 12);
        int mineDz = (int) Math.round(Math.sin(mineAng) * 12);
        int mineFloorY = Math.max(context.heightAccessor().getMinBuildHeight() + 8, y - 38);
        return Optional.of(new Structure.GenerationStub(origin, builder -> {
            builder.addPiece(new VestigeCityPiece(origin, tier));
            builder.addPiece(new VestigeVaultPiece(vaultOrigin, y));
            for (int i = 0; i < spires; i++) {
                double ang = (spireSeed + i * 2.39996) % (Math.PI * 2.0);
                int dx = (int) Math.round(Math.cos(ang) * 16);
                int dz = (int) Math.round(Math.sin(ang) * 16);
                builder.addPiece(new VestigeSpirePiece(new BlockPos(x + dx, y, z + dz)));
            }
            if (mine && mineFloorY < y - 16) {
                builder.addPiece(new VestigeMinePiece(new BlockPos(x + mineDx, mineFloorY, z + mineDz), y));
            }
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.VESTIGE_CITY.get();
    }
}
