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
 *
 * <p>The {@code guaranteed_mine} config field makes every city of this instance carry a Lumenwright liftshaft
 * mine — used by the separate {@code lumenwilds:vestige_mine} datapack structure so a player can
 * {@code /locate structure lumenwilds:vestige_mine} the nearest ancient city that's guaranteed to have one
 * (the default {@code vestige_city} keeps the rarer grand-always / medium-~40% roll).</p>
 */
public class VestigeCityStructure extends Structure {

    public static final MapCodec<VestigeCityStructure> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    settingsCodec(inst),
                    com.mojang.serialization.Codec.BOOL
                            .optionalFieldOf("guaranteed_mine", false)
                            .forGetter(s -> s.guaranteedMine))
            .apply(inst, VestigeCityStructure::new));

    private final boolean guaranteedMine;

    public VestigeCityStructure(Structure.StructureSettings settings, boolean guaranteedMine) {
        super(settings);
        this.guaranteedMine = guaranteedMine;
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
        // Biome flavor is decided HERE (placement) by sampling the biome source — safe, unlike reading
        // level.getBiome in postProcess (which can request an unavailable chunk and crash chunk-gen).
        net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome = context.chunkGenerator()
                .getBiomeSource()
                .getNoiseBiome(x >> 2, y >> 2, z >> 2, context.randomState().sampler());
        int flavor = VestigeCityPiece.flavorFor(biome);
        // Size tier: ~25% of cities are GRAND (bigger, a central Light Engine + broken spires); else medium.
        int tier = context.random().nextInt(100) < 25 ? 1 : 0;
        // A buried Vestige Vault sits ~22 blocks under the plaza, with a spiral shaft back up (10f.2).
        int vaultY = Math.max(context.heightAccessor().getMinBuildHeight() + 12, y - 22);
        BlockPos vaultOrigin = new BlockPos(x, vaultY, z);
        int spires = tier > 0 ? 1 + context.random().nextInt(2) : 0; // 1–2 spires in a grand city
        int spireSeed = context.random().nextInt();
        // A Lumenwright liftshaft + abandoned mine: a major discovery, so not in every city — grand cities
        // always, medium ~40%. The dais sits offset from the plaza (clear of the central vault shaft).
        boolean mine = guaranteedMine || tier > 0 || context.random().nextInt(100) < 40;
        double mineAng = context.random().nextDouble() * Math.PI * 2.0;
        // Out at the city edge (past the inner building rings + spires) so the dais is a distinct, spottable
        // satellite, not lost in the dense core.
        int mineDx = (int) Math.round(Math.cos(mineAng) * 22);
        int mineDz = (int) Math.round(Math.sin(mineAng) * 22);
        // Try to drop the mine into a real cavern (probe the noise column, read-only); else use a fixed depth.
        int caveY = VestigeMinePiece.findCaveFloor(
                context.chunkGenerator(), x + mineDx, z + mineDz, context.heightAccessor(), context.randomState(), y);
        boolean naturalCave = caveY != Integer.MIN_VALUE;
        int mineFloorY = naturalCave ? caveY : Math.max(context.heightAccessor().getMinBuildHeight() + 8, y - 38);
        return Optional.of(new Structure.GenerationStub(origin, builder -> {
            builder.addPiece(new VestigeCityPiece(origin, tier, flavor));
            builder.addPiece(new VestigeVaultPiece(vaultOrigin, y));
            for (int i = 0; i < spires; i++) {
                double ang = (spireSeed + i * 2.39996) % (Math.PI * 2.0);
                int dx = (int) Math.round(Math.cos(ang) * 16);
                int dz = (int) Math.round(Math.sin(ang) * 16);
                builder.addPiece(new VestigeSpirePiece(new BlockPos(x + dx, y, z + dz)));
            }
            if (mine && mineFloorY < y - 16) {
                builder.addPiece(
                        new VestigeMinePiece(new BlockPos(x + mineDx, mineFloorY, z + mineDz), y, naturalCave, flavor));
            }
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.VESTIGE_CITY.get();
    }
}
