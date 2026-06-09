/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.fluid.LumenwaterBlock;
import com.jus144tice.lumenwilds.portal.LumenPortalBlock;
import com.jus144tice.lumenwilds.world.LumenConfiguredFeatures;
import java.util.Optional;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All blocks added by The Lumenwilds.
 *
 * <p>Phase 1 placeholders. Every block is registered cleanly so it can be fleshed out later (custom
 * models, block entities, plant/vine behaviour, etc.). Block <em>items</em> are registered separately
 * in {@link ModItems} so the item layer stays in one place.</p>
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Lumenwilds.MOD_ID);

    private ModBlocks() {}

    // --- Portal & progression -------------------------------------------------------------------

    /**
     * Lumenbound Stone — the REQUIRED portal frame block (replaces the earlier vanilla-lodestone
     * idea, which is too expensive for this progression point). Visually: stone infused with
     * amethyst resonance and Nether light. Sturdy, pickaxe-mined, stone-like. No special behaviour
     * yet — the {@link com.jus144tice.lumenwilds.item.LumenStrikerItem} keys off this block.
     */
    public static final DeferredBlock<Block> LUMENBOUND_STONE = BLOCKS.registerSimpleBlock(
            "lumenbound_stone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.0F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    /**
     * Lumen Portal — the portal interior block. Non-solid, no collision, glows. Placeholder: it does
     * not yet teleport (see {@link com.jus144tice.lumenwilds.portal.LumenPortalBlock}).
     */
    public static final DeferredBlock<LumenPortalBlock> LUMEN_PORTAL = BLOCKS.registerBlock(
            "lumen_portal",
            LumenPortalBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .noLootTable()
                    .strength(-1.0F)
                    .lightLevel(state -> 11)
                    .pushReaction(PushReaction.BLOCK));

    // --- Terrain & world blocks -----------------------------------------------------------------

    public static final DeferredBlock<Block> MOONLOAM = BLOCKS.registerSimpleBlock(
            "moonloam",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(0.6F)
                    .sound(SoundType.GRAVEL));

    public static final DeferredBlock<Block> LUMEN_GRASS_BLOCK = BLOCKS.registerSimpleBlock(
            "lumen_grass_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(0.6F)
                    .sound(SoundType.GRASS));

    public static final DeferredBlock<Block> MOONSTONE = BLOCKS.registerSimpleBlock(
            "moonstone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    public static final DeferredBlock<Block> COBBLED_MOONSTONE = BLOCKS.registerSimpleBlock(
            "cobbled_moonstone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    // --- Wood & plants --------------------------------------------------------------------------

    public static final DeferredBlock<RotatedPillarBlock> GLOWWOOD_LOG =
            BLOCKS.registerBlock("glowwood_log", RotatedPillarBlock::new, logProps());

    public static final DeferredBlock<Block> GLOWWOOD_PLANKS =
            BLOCKS.registerSimpleBlock("glowwood_planks", planksProps());

    public static final DeferredBlock<RotatedPillarBlock> GLOWROOT_LOG = BLOCKS.registerBlock(
            "glowroot_log",
            RotatedPillarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.0F)
                    .sound(SoundType.WOOD));

    /** Glowroot Leaves — broad teal canopy of the Glowroot trees (and the mega tree). Faint glow. */
    public static final DeferredBlock<LeavesBlock> GLOWROOT_LEAVES = BLOCKS.registerBlock(
            "glowroot_leaves",
            LeavesBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.2F)
                    .randomTicks()
                    .lightLevel(state -> 3)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isViewBlocking((s, l, p) -> false)
                    .pushReaction(PushReaction.DESTROY));

    /** Grows a normal Glowroot tree, or the 2×2 variant when 4 saplings are planted together. */
    private static final TreeGrower GLOWROOT_GROWER = new TreeGrower(
            Lumenwilds.MOD_ID + ":glowroot",
            Optional.of(LumenConfiguredFeatures.GLOWROOT_TREE_2X2),
            Optional.of(LumenConfiguredFeatures.GLOWROOT_TREE),
            Optional.empty());

    public static final DeferredBlock<SaplingBlock> GLOWROOT_SAPLING = BLOCKS.registerBlock(
            "glowroot_sapling",
            props -> new SaplingBlock(GLOWROOT_GROWER, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY));

    // TODO: glowvine should become a climbable/decay vine block (LeavesBlock/GrowingPlantBlock-like).
    public static final DeferredBlock<Block> GLOWVINE = BLOCKS.registerSimpleBlock(
            "glowvine",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.2F)
                    .lightLevel(state -> 7)
                    .sound(SoundType.VINE));

    /** Moonblossom — a common glowing flower (light 9); brief night vision in suspicious stew/brewing. */
    public static final DeferredBlock<FlowerBlock> MOONBLOSSOM = BLOCKS.registerBlock(
            "moonblossom",
            props -> new FlowerBlock(MobEffects.NIGHT_VISION, 5.0F, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 9)
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY));

    /** Glow Fern — alien ground cover with a faint cyan glow. Cross-model, instabreak, replaceable. */
    public static final DeferredBlock<TallGrassBlock> GLOW_FERN = BLOCKS.registerBlock(
            "glow_fern",
            TallGrassBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 2)
                    .sound(SoundType.GRASS)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY));

    /** Grows the {@code lumenwilds:glowwood_tree} configured feature (Phase 5c); no mega tree yet. */
    private static final TreeGrower GLOWWOOD_GROWER = new TreeGrower(
            Lumenwilds.MOD_ID + ":glowwood",
            Optional.empty(),
            Optional.of(LumenConfiguredFeatures.GLOWWOOD_TREE),
            Optional.empty());

    /** Glowwood Sapling — grows into a Glowwood tree (bonemeal/random tick). */
    public static final DeferredBlock<SaplingBlock> GLOWWOOD_SAPLING = BLOCKS.registerBlock(
            "glowwood_sapling",
            props -> new SaplingBlock(GLOWWOOD_GROWER, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY));

    /** Lumenbulb — a native living light source. Placeholder full cube that emits max light. */
    public static final DeferredBlock<Block> LUMENBULB = BLOCKS.registerSimpleBlock(
            "lumenbulb",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.3F)
                    .lightLevel(state -> 15)
                    .sound(SoundType.GLASS));

    // --- Resource / storage ---------------------------------------------------------------------

    public static final DeferredBlock<Block> LUMEN_CRYSTAL_BLOCK = BLOCKS.registerSimpleBlock(
            "lumen_crystal_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 6)
                    .sound(SoundType.AMETHYST));

    /** Lumen Crystal Ore — drops Lumen Crystal Shard; glows faintly (helps light caves). */
    public static final DeferredBlock<DropExperienceBlock> LUMEN_CRYSTAL_ORE = BLOCKS.registerBlock(
            "lumen_crystal_ore",
            props -> new DropExperienceBlock(UniformInt.of(2, 5), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 4)
                    .sound(SoundType.STONE));

    public static final DeferredBlock<DropExperienceBlock> DEEP_LUMEN_CRYSTAL_ORE = BLOCKS.registerBlock(
            "deep_lumen_crystal_ore",
            props -> new DropExperienceBlock(UniformInt.of(2, 5), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 4)
                    .sound(SoundType.DEEPSLATE));

    /**
     * Glasspetal Cluster (Phase 5d.2) — translucent blue-violet crystal petals that grow from the rock
     * faces of the Glasspetal Crags. A directional, waterloggable {@link AmethystClusterBlock} (places
     * facing the clicked face) that glows (light 7). The 7.0F/3.0F box matches a vanilla large cluster.
     */
    public static final DeferredBlock<AmethystClusterBlock> GLASSPETAL_CLUSTER = BLOCKS.registerBlock(
            "glasspetal_cluster",
            props -> new AmethystClusterBlock(7.0F, 3.0F, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noOcclusion()
                    .strength(1.5F)
                    .lightLevel(state -> 7)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .pushReaction(PushReaction.DESTROY));

    // --- Sporefall Jungle — Giant Glowcap (Phase 5d.3) ------------------------------------------
    // The bible's enormous jungle mushroom: a wide, glowing cap on a pale stem. Built by the vanilla
    // huge_brown_mushroom feature (cap + stem providers), so both are HugeMushroomBlocks. The cap glows
    // (light 9) to stand in for the separate luminous "gills" sub-block (a later refinement).

    /** Giant Glowcap cap block — the broad, glowing crown (light 9). */
    public static final DeferredBlock<HugeMushroomBlock> GIANT_GLOWCAP_BLOCK = BLOCKS.registerBlock(
            "giant_glowcap_block",
            HugeMushroomBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.2F)
                    .lightLevel(state -> 9)
                    .sound(SoundType.WOOD)
                    .ignitedByLava());

    /** Giant Glowcap stem block — the pale supporting trunk. */
    public static final DeferredBlock<HugeMushroomBlock> GIANT_GLOWCAP_STEM = BLOCKS.registerBlock(
            "giant_glowcap_stem",
            HugeMushroomBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOL)
                    .strength(0.2F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava());

    // --- Fluids (Phase 5e) ----------------------------------------------------------------------
    // Liquid block for Lumenwater. NOT given a BlockItem (placed via bucket) and has noLootTable. The
    // custom LumenwaterBlock reverts to water outside the dimension (anti-OP). See ModFluids/ModFluidTypes.

    public static final DeferredBlock<LiquidBlock> LUMENWATER_BLOCK = BLOCKS.registerBlock(
            "lumenwater",
            props -> new LumenwaterBlock(ModFluids.LUMENWATER.get(), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .randomTicks()
                    .lightLevel(state -> 4)
                    .pushReaction(PushReaction.DESTROY));

    // --- Glowwood building set (Phase 4) --------------------------------------------------------
    // Wood-set sounds/behaviour + signs use the bespoke Glowwood WoodType/BlockSetType (ModWoodTypes).

    public static final DeferredBlock<RotatedPillarBlock> GLOWWOOD_WOOD =
            BLOCKS.registerBlock("glowwood_wood", RotatedPillarBlock::new, logProps());

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_GLOWWOOD_LOG =
            BLOCKS.registerBlock("stripped_glowwood_log", RotatedPillarBlock::new, logProps());

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_GLOWWOOD_WOOD =
            BLOCKS.registerBlock("stripped_glowwood_wood", RotatedPillarBlock::new, logProps());

    /** Glowwood Leaves — faint glow (light 2) per the bible; no full set of leaf drops yet. */
    public static final DeferredBlock<LeavesBlock> GLOWWOOD_LEAVES = BLOCKS.registerBlock(
            "glowwood_leaves",
            LeavesBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.2F)
                    .randomTicks()
                    .lightLevel(state -> 2)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isViewBlocking((s, l, p) -> false)
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<StairBlock> GLOWWOOD_STAIRS = BLOCKS.registerBlock(
            "glowwood_stairs",
            props -> new StairBlock(GLOWWOOD_PLANKS.get().defaultBlockState(), props),
            planksProps());

    public static final DeferredBlock<SlabBlock> GLOWWOOD_SLAB =
            BLOCKS.registerBlock("glowwood_slab", SlabBlock::new, planksProps());

    public static final DeferredBlock<FenceBlock> GLOWWOOD_FENCE =
            BLOCKS.registerBlock("glowwood_fence", FenceBlock::new, planksProps());

    public static final DeferredBlock<FenceGateBlock> GLOWWOOD_FENCE_GATE = BLOCKS.registerBlock(
            "glowwood_fence_gate", props -> new FenceGateBlock(ModWoodTypes.GLOWWOOD, props), planksProps());

    public static final DeferredBlock<DoorBlock> GLOWWOOD_DOOR = BLOCKS.registerBlock(
            "glowwood_door",
            props -> new DoorBlock(ModWoodTypes.GLOWWOOD_SET, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<TrapDoorBlock> GLOWWOOD_TRAPDOOR = BLOCKS.registerBlock(
            "glowwood_trapdoor",
            props -> new TrapDoorBlock(ModWoodTypes.GLOWWOOD_SET, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .isValidSpawn((s, l, p, e) -> false));

    public static final DeferredBlock<ButtonBlock> GLOWWOOD_BUTTON = BLOCKS.registerBlock(
            "glowwood_button",
            props -> new ButtonBlock(ModWoodTypes.GLOWWOOD_SET, 30, props),
            BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(0.5F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<PressurePlateBlock> GLOWWOOD_PRESSURE_PLATE = BLOCKS.registerBlock(
            "glowwood_pressure_plate",
            props -> new PressurePlateBlock(ModWoodTypes.GLOWWOOD_SET, props),
            BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(0.5F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

    // Signs (standing + wall, hanging + wall-hanging). Wall variants share the standing item; all four
    // use the vanilla SIGN/HANGING_SIGN block entities (wired via event.ModBlockEntityTypes).
    public static final DeferredBlock<StandingSignBlock> GLOWWOOD_SIGN = BLOCKS.registerBlock(
            "glowwood_sign", props -> new StandingSignBlock(ModWoodTypes.GLOWWOOD, props), signProps());

    public static final DeferredBlock<WallSignBlock> GLOWWOOD_WALL_SIGN = BLOCKS.registerBlock(
            "glowwood_wall_sign", props -> new WallSignBlock(ModWoodTypes.GLOWWOOD, props), signProps());

    public static final DeferredBlock<CeilingHangingSignBlock> GLOWWOOD_HANGING_SIGN = BLOCKS.registerBlock(
            "glowwood_hanging_sign", props -> new CeilingHangingSignBlock(ModWoodTypes.GLOWWOOD, props), signProps());

    public static final DeferredBlock<WallHangingSignBlock> GLOWWOOD_WALL_HANGING_SIGN = BLOCKS.registerBlock(
            "glowwood_wall_hanging_sign", props -> new WallHangingSignBlock(ModWoodTypes.GLOWWOOD, props), signProps());

    // --- Moonstone stone set (Phase 4) ----------------------------------------------------------
    // MOONSTONE + COBBLED_MOONSTONE are declared above. Cube variants first, then stairs/slabs/walls.

    public static final DeferredBlock<Block> SMOOTH_MOONSTONE = moonCube("smooth_moonstone");
    public static final DeferredBlock<Block> MOONSTONE_BRICKS = moonCube("moonstone_bricks");
    public static final DeferredBlock<Block> CHISELED_MOONSTONE = moonCube("chiseled_moonstone");
    public static final DeferredBlock<Block> MOONSTONE_TILES = moonCube("moonstone_tiles");

    public static final DeferredBlock<StairBlock> MOONSTONE_STAIRS = moonStairs("moonstone_stairs", MOONSTONE);
    public static final DeferredBlock<SlabBlock> MOONSTONE_SLAB = moonSlab("moonstone_slab");
    public static final DeferredBlock<WallBlock> MOONSTONE_WALL = moonWall("moonstone_wall");

    public static final DeferredBlock<StairBlock> COBBLED_MOONSTONE_STAIRS =
            moonStairs("cobbled_moonstone_stairs", COBBLED_MOONSTONE);
    public static final DeferredBlock<SlabBlock> COBBLED_MOONSTONE_SLAB = moonSlab("cobbled_moonstone_slab");
    public static final DeferredBlock<WallBlock> COBBLED_MOONSTONE_WALL = moonWall("cobbled_moonstone_wall");

    public static final DeferredBlock<StairBlock> MOONSTONE_BRICK_STAIRS =
            moonStairs("moonstone_brick_stairs", MOONSTONE_BRICKS);
    public static final DeferredBlock<SlabBlock> MOONSTONE_BRICK_SLAB = moonSlab("moonstone_brick_slab");
    public static final DeferredBlock<WallBlock> MOONSTONE_BRICK_WALL = moonWall("moonstone_brick_wall");

    public static final DeferredBlock<StairBlock> MOONSTONE_TILE_STAIRS =
            moonStairs("moonstone_tile_stairs", MOONSTONE_TILES);
    public static final DeferredBlock<SlabBlock> MOONSTONE_TILE_SLAB = moonSlab("moonstone_tile_slab");
    public static final DeferredBlock<WallBlock> MOONSTONE_TILE_WALL = moonWall("moonstone_tile_wall");

    public static final DeferredBlock<StairBlock> SMOOTH_MOONSTONE_STAIRS =
            moonStairs("smooth_moonstone_stairs", SMOOTH_MOONSTONE);
    public static final DeferredBlock<SlabBlock> SMOOTH_MOONSTONE_SLAB = moonSlab("smooth_moonstone_slab");

    // --- Deep Moonstone (deepslate analog, deeper layers) (Phase 4) -----------------------------

    public static final DeferredBlock<Block> DEEP_MOONSTONE = deepCube("deep_moonstone");
    public static final DeferredBlock<Block> COBBLED_DEEP_MOONSTONE = deepCube("cobbled_deep_moonstone");
    public static final DeferredBlock<Block> POLISHED_DEEP_MOONSTONE = deepCube("polished_deep_moonstone");
    public static final DeferredBlock<Block> DEEP_MOONSTONE_BRICKS = deepCube("deep_moonstone_bricks");
    public static final DeferredBlock<Block> DEEP_MOONSTONE_TILES = deepCube("deep_moonstone_tiles");

    public static final DeferredBlock<StairBlock> COBBLED_DEEP_MOONSTONE_STAIRS =
            deepStairs("cobbled_deep_moonstone_stairs", COBBLED_DEEP_MOONSTONE);
    public static final DeferredBlock<SlabBlock> COBBLED_DEEP_MOONSTONE_SLAB = deepSlab("cobbled_deep_moonstone_slab");
    public static final DeferredBlock<WallBlock> COBBLED_DEEP_MOONSTONE_WALL = deepWall("cobbled_deep_moonstone_wall");

    public static final DeferredBlock<StairBlock> POLISHED_DEEP_MOONSTONE_STAIRS =
            deepStairs("polished_deep_moonstone_stairs", POLISHED_DEEP_MOONSTONE);
    public static final DeferredBlock<SlabBlock> POLISHED_DEEP_MOONSTONE_SLAB =
            deepSlab("polished_deep_moonstone_slab");
    public static final DeferredBlock<WallBlock> POLISHED_DEEP_MOONSTONE_WALL =
            deepWall("polished_deep_moonstone_wall");

    public static final DeferredBlock<StairBlock> DEEP_MOONSTONE_BRICK_STAIRS =
            deepStairs("deep_moonstone_brick_stairs", DEEP_MOONSTONE_BRICKS);
    public static final DeferredBlock<SlabBlock> DEEP_MOONSTONE_BRICK_SLAB = deepSlab("deep_moonstone_brick_slab");
    public static final DeferredBlock<WallBlock> DEEP_MOONSTONE_BRICK_WALL = deepWall("deep_moonstone_brick_wall");

    public static final DeferredBlock<StairBlock> DEEP_MOONSTONE_TILE_STAIRS =
            deepStairs("deep_moonstone_tile_stairs", DEEP_MOONSTONE_TILES);
    public static final DeferredBlock<SlabBlock> DEEP_MOONSTONE_TILE_SLAB = deepSlab("deep_moonstone_tile_slab");
    public static final DeferredBlock<WallBlock> DEEP_MOONSTONE_TILE_WALL = deepWall("deep_moonstone_tile_wall");

    // --- Shimmerstone (high-end blue-violet crystalline stone) (Phase 4) ------------------------

    public static final DeferredBlock<Block> SHIMMERSTONE = shimmerCube("shimmerstone");
    public static final DeferredBlock<Block> POLISHED_SHIMMERSTONE = shimmerCube("polished_shimmerstone");
    public static final DeferredBlock<Block> SHIMMERSTONE_BRICKS = shimmerCube("shimmerstone_bricks");
    public static final DeferredBlock<Block> SHIMMERSTONE_TILES = shimmerCube("shimmerstone_tiles");

    public static final DeferredBlock<RotatedPillarBlock> SHIMMERSTONE_PILLAR =
            BLOCKS.registerBlock("shimmerstone_pillar", RotatedPillarBlock::new, shimmerProps());

    public static final DeferredBlock<TransparentBlock> SHIMMERSTONE_GLASS = BLOCKS.registerBlock(
            "shimmerstone_glass",
            TransparentBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.4F)
                    .sound(SoundType.GLASS)
                    .noOcclusion());

    public static final DeferredBlock<StairBlock> POLISHED_SHIMMERSTONE_STAIRS =
            shimmerStairs("polished_shimmerstone_stairs", POLISHED_SHIMMERSTONE);
    public static final DeferredBlock<SlabBlock> POLISHED_SHIMMERSTONE_SLAB = shimmerSlab("polished_shimmerstone_slab");
    public static final DeferredBlock<WallBlock> POLISHED_SHIMMERSTONE_WALL = shimmerWall("polished_shimmerstone_wall");

    public static final DeferredBlock<StairBlock> SHIMMERSTONE_BRICK_STAIRS =
            shimmerStairs("shimmerstone_brick_stairs", SHIMMERSTONE_BRICKS);
    public static final DeferredBlock<SlabBlock> SHIMMERSTONE_BRICK_SLAB = shimmerSlab("shimmerstone_brick_slab");
    public static final DeferredBlock<WallBlock> SHIMMERSTONE_BRICK_WALL = shimmerWall("shimmerstone_brick_wall");

    public static final DeferredBlock<StairBlock> SHIMMERSTONE_TILE_STAIRS =
            shimmerStairs("shimmerstone_tile_stairs", SHIMMERSTONE_TILES);
    public static final DeferredBlock<SlabBlock> SHIMMERSTONE_TILE_SLAB = shimmerSlab("shimmerstone_tile_slab");
    public static final DeferredBlock<WallBlock> SHIMMERSTONE_TILE_WALL = shimmerWall("shimmerstone_tile_wall");

    // --- Sporeglass (translucent glowing glass, light 6) (Phase 4) ------------------------------

    public static final DeferredBlock<TransparentBlock> SPOREGLASS = BLOCKS.registerBlock(
            "sporeglass",
            TransparentBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.4F)
                    .lightLevel(state -> 6)
                    .sound(SoundType.GLASS)
                    .noOcclusion());

    public static final DeferredBlock<IronBarsBlock> SPOREGLASS_PANE = BLOCKS.registerBlock(
            "sporeglass_pane",
            IronBarsBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F)
                    .lightLevel(state -> 6)
                    .sound(SoundType.GLASS)
                    .noOcclusion());

    // --- Property + stone-family helpers --------------------------------------------------------

    private static BlockBehaviour.Properties shimmerProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.AMETHYST);
    }

    private static DeferredBlock<Block> shimmerCube(String name) {
        return BLOCKS.registerSimpleBlock(name, shimmerProps());
    }

    private static DeferredBlock<StairBlock> shimmerStairs(String name, DeferredBlock<? extends Block> base) {
        return BLOCKS.registerBlock(
                name, props -> new StairBlock(base.get().defaultBlockState(), props), shimmerProps());
    }

    private static DeferredBlock<SlabBlock> shimmerSlab(String name) {
        return BLOCKS.registerBlock(name, SlabBlock::new, shimmerProps());
    }

    private static DeferredBlock<WallBlock> shimmerWall(String name) {
        return BLOCKS.registerBlock(name, WallBlock::new, shimmerProps());
    }

    private static BlockBehaviour.Properties moonstoneProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE);
    }

    private static BlockBehaviour.Properties deepProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .strength(3.0F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.DEEPSLATE);
    }

    private static DeferredBlock<Block> moonCube(String name) {
        return BLOCKS.registerSimpleBlock(name, moonstoneProps());
    }

    private static DeferredBlock<StairBlock> moonStairs(String name, DeferredBlock<? extends Block> base) {
        return BLOCKS.registerBlock(
                name, props -> new StairBlock(base.get().defaultBlockState(), props), moonstoneProps());
    }

    private static DeferredBlock<SlabBlock> moonSlab(String name) {
        return BLOCKS.registerBlock(name, SlabBlock::new, moonstoneProps());
    }

    private static DeferredBlock<WallBlock> moonWall(String name) {
        return BLOCKS.registerBlock(name, WallBlock::new, moonstoneProps());
    }

    private static DeferredBlock<Block> deepCube(String name) {
        return BLOCKS.registerSimpleBlock(name, deepProps());
    }

    private static DeferredBlock<StairBlock> deepStairs(String name, DeferredBlock<? extends Block> base) {
        return BLOCKS.registerBlock(name, props -> new StairBlock(base.get().defaultBlockState(), props), deepProps());
    }

    private static DeferredBlock<SlabBlock> deepSlab(String name) {
        return BLOCKS.registerBlock(name, SlabBlock::new, deepProps());
    }

    private static DeferredBlock<WallBlock> deepWall(String name) {
        return BLOCKS.registerBlock(name, WallBlock::new, deepProps());
    }

    // --- Property helpers -----------------------------------------------------------------------

    private static BlockBehaviour.Properties logProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .strength(2.0F)
                .sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties planksProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties signProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .forceSolidOn()
                .noCollission()
                .strength(1.0F)
                .sound(SoundType.WOOD);
    }
}
