/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.world.structure.GlasspetalSpiresPiece;
import com.jus144tice.lumenwilds.world.structure.GlasspetalSpiresStructure;
import com.jus144tice.lumenwilds.world.structure.GlowrootTreePiece;
import com.jus144tice.lumenwilds.world.structure.GlowrootTreeStructure;
import com.jus144tice.lumenwilds.world.structure.LumenboundRuinsPiece;
import com.jus144tice.lumenwilds.world.structure.LumenboundRuinsStructure;
import com.jus144tice.lumenwilds.world.structure.MegaGlowcapPiece;
import com.jus144tice.lumenwilds.world.structure.MegaGlowcapStructure;
import com.jus144tice.lumenwilds.world.structure.RootshrinePiece;
import com.jus144tice.lumenwilds.world.structure.RootshrineStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Worldgen {@link StructureType} + {@link StructurePieceType} registries.
 *
 * <p>The Glowroot mega tree is a <em>structure</em> (not a feature): structures generate per-chunk via a
 * bounding box, so the tree can span many chunks with no size cap and no "far chunk" write errors — the
 * same reason villages/fortresses are structures. The structure instance + spawn spacing are data-driven
 * (see {@code data/lumenwilds/worldgen/structure*}); these are the code types they bind to.</p>
 */
public final class ModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, Lumenwilds.MOD_ID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, Lumenwilds.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<GlowrootTreeStructure>> GLOWROOT_TREE =
            STRUCTURE_TYPES.register("glowroot_tree", () -> () -> GlowrootTreeStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> GLOWROOT_TREE_PIECE =
            STRUCTURE_PIECES.register(
                    "glowroot_tree", () -> (StructurePieceType.ContextlessType) GlowrootTreePiece::new);

    public static final DeferredHolder<StructureType<?>, StructureType<MegaGlowcapStructure>> MEGA_GLOWCAP =
            STRUCTURE_TYPES.register("mega_glowcap", () -> () -> MegaGlowcapStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> MEGA_GLOWCAP_PIECE =
            STRUCTURE_PIECES.register("mega_glowcap", () -> (StructurePieceType.ContextlessType) MegaGlowcapPiece::new);

    public static final DeferredHolder<StructureType<?>, StructureType<RootshrineStructure>> ROOTSHRINE =
            STRUCTURE_TYPES.register("rootshrine", () -> () -> RootshrineStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> ROOTSHRINE_PIECE =
            STRUCTURE_PIECES.register("rootshrine", () -> (StructurePieceType.ContextlessType) RootshrinePiece::new);

    public static final DeferredHolder<StructureType<?>, StructureType<LumenboundRuinsStructure>> LUMENBOUND_RUINS =
            STRUCTURE_TYPES.register("lumenbound_ruins", () -> () -> LumenboundRuinsStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> LUMENBOUND_RUINS_PIECE =
            STRUCTURE_PIECES.register(
                    "lumenbound_ruins", () -> (StructurePieceType.ContextlessType) LumenboundRuinsPiece::new);

    public static final DeferredHolder<StructureType<?>, StructureType<GlasspetalSpiresStructure>> GLASSPETAL_SPIRES =
            STRUCTURE_TYPES.register("glasspetal_spires", () -> () -> GlasspetalSpiresStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> GLASSPETAL_SPIRES_PIECE =
            STRUCTURE_PIECES.register(
                    "glasspetal_spires", () -> (StructurePieceType.ContextlessType) GlasspetalSpiresPiece::new);

    private ModStructures() {}
}
