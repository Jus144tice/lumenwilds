/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.block.BottledLanternBeetleBlock;
import com.jus144tice.lumenwilds.block.BuddingLumenCrystalBlock;
import com.jus144tice.lumenwilds.block.LumenAnchorBlock;
import com.jus144tice.lumenwilds.block.LumenCoralBlock;
import com.jus144tice.lumenwilds.fluid.LumenwaterBlock;
import com.jus144tice.lumenwilds.portal.LumenPortalBlock;
import com.jus144tice.lumenwilds.world.LumenConfiguredFeatures;
import java.util.Optional;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.AttachedStemBlock;
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
import net.minecraft.world.level.block.StemBlock;
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
     * Lumen Anchor (Phase 8c) — the portal-link device ({@link LumenAnchorBlock} + a block entity). Link two
     * with the Lumen Striker so return travel lands precisely. Glows faintly.
     */
    public static final DeferredBlock<LumenAnchorBlock> LUMEN_ANCHOR = BLOCKS.registerBlock(
            "lumen_anchor",
            LumenAnchorBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(4.0F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 7)
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

    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenGrassBlock> LUMEN_GRASS_BLOCK =
            BLOCKS.registerBlock(
                    "lumen_grass_block",
                    com.jus144tice.lumenwilds.block.LumenGrassBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLUE)
                            .strength(0.6F)
                            .randomTicks() // grass spread / revert (see block.LumenGrassBlock)
                            .sound(SoundType.GRASS));

    // Farming soil (v1.4 Phase F1) — Moonloam is the "dirt": hoe it → Lumen Farmland, shovel Lumen Grass →
    // Lumen Dirt Path (see event.LumenFarmingEvents). Both revert to Moonloam; farmland hydrates from Lumenwater.
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenFarmlandBlock> LUMEN_FARMLAND =
            BLOCKS.registerBlock(
                    "lumen_farmland",
                    com.jus144tice.lumenwilds.block.LumenFarmlandBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLUE)
                            .strength(0.6F)
                            .randomTicks()
                            .sound(SoundType.GRAVEL)
                            .isViewBlocking((s, l, p) -> true)
                            .isSuffocating((s, l, p) -> true));
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenDirtPathBlock> LUMEN_DIRT_PATH =
            BLOCKS.registerBlock(
                    "lumen_dirt_path",
                    com.jus144tice.lumenwilds.block.LumenDirtPathBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLUE)
                            .strength(0.65F)
                            .sound(SoundType.GRASS)
                            .isViewBlocking((s, l, p) -> true)
                            .isSuffocating((s, l, p) -> true));

    public static final DeferredBlock<Block> MOONSTONE = BLOCKS.registerSimpleBlock(
            "moonstone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    /**
     * Veinstone (v1.3 mining overhaul, Phase B) — a granite/andesite-style accent rock generated as blobs
     * through the Moonstone band, so the upper strata aren't a flat sea of Moonstone. Stone-tier; drops self.
     */
    public static final DeferredBlock<Block> VEINSTONE = BLOCKS.registerSimpleBlock("veinstone", moonstoneProps());

    // Veinstone build set (v1.4.7) — gives the accent rock a real build tree (polished + bricks + shapes),
    // crafted + stonecut like the Moonstone family. Reuses the generic moon* helpers (same moonstoneProps).
    public static final DeferredBlock<Block> POLISHED_VEINSTONE = moonCube("polished_veinstone");
    public static final DeferredBlock<Block> VEINSTONE_BRICKS = moonCube("veinstone_bricks");
    public static final DeferredBlock<StairBlock> VEINSTONE_STAIRS = moonStairs("veinstone_stairs", VEINSTONE);
    public static final DeferredBlock<SlabBlock> VEINSTONE_SLAB = moonSlab("veinstone_slab");
    public static final DeferredBlock<WallBlock> VEINSTONE_WALL = moonWall("veinstone_wall");
    public static final DeferredBlock<StairBlock> POLISHED_VEINSTONE_STAIRS =
            moonStairs("polished_veinstone_stairs", POLISHED_VEINSTONE);
    public static final DeferredBlock<SlabBlock> POLISHED_VEINSTONE_SLAB = moonSlab("polished_veinstone_slab");
    public static final DeferredBlock<WallBlock> POLISHED_VEINSTONE_WALL = moonWall("polished_veinstone_wall");
    public static final DeferredBlock<StairBlock> VEINSTONE_BRICK_STAIRS =
            moonStairs("veinstone_brick_stairs", VEINSTONE_BRICKS);
    public static final DeferredBlock<SlabBlock> VEINSTONE_BRICK_SLAB = moonSlab("veinstone_brick_slab");
    public static final DeferredBlock<WallBlock> VEINSTONE_BRICK_WALL = moonWall("veinstone_brick_wall");

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

    // Renders as a passable glowing cross (a hanging vine), not a solid cube. A block.GlowvineBlock so breaking
    // one SEVERS the strand below it like a vanilla vine (see that class); it stays a simple block (no support
    // rule) so the Undercrown ore-feature can still embed it in cave rock and structures can creep it over ruins.
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.GlowvineBlock> GLOWVINE = BLOCKS.registerBlock(
            "glowvine",
            com.jus144tice.lumenwilds.block.GlowvineBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(0.2F)
                    .lightLevel(state -> 7)
                    .noCollission()
                    .noOcclusion()
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
                    .replaceable() // like the ferns — placing a block replaces it (not buries it), v1.5.0
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

    /** Glow Algae — luminous teal ground cover of the Moonmire swamp (Phase 5d.4). Cross-model. */
    public static final DeferredBlock<TallGrassBlock> GLOW_ALGAE = BLOCKS.registerBlock(
            "glow_algae",
            TallGrassBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 3)
                    .sound(SoundType.WET_GRASS)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY));

    /** Lumen Reeds — tall glowing blue reeds that fringe the Moonmire's pools (Phase 5d.4). Cross-model. */
    public static final DeferredBlock<TallGrassBlock> LUMEN_REEDS = BLOCKS.registerBlock(
            "lumen_reeds",
            TallGrassBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 4)
                    .sound(SoundType.GRASS)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY));

    // --- Underwater life — the Lumenwater seabed (Phase 9 drawing-board) -------------------------

    /** Lumensand — the glowing seabed of the Lumenwater seas (placed underwater by the surface rule). */
    public static final DeferredBlock<Block> LUMENSAND = BLOCKS.registerSimpleBlock(
            "lumensand",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.5F)
                    .lightLevel(state -> 6)
                    .sound(SoundType.SAND));

    /** Lumen Coral Block — a solid, brightly glowing coral (reef mounds + building). */
    public static final DeferredBlock<Block> LUMEN_CORAL_BLOCK = BLOCKS.registerSimpleBlock(
            "lumen_coral_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(1.0F)
                    .lightLevel(state -> 10)
                    .sound(SoundType.CORAL_BLOCK));

    /** Lumen Coral — a glowing waterlogged frond growing on the reef (see {@link LumenCoralBlock}). */
    public static final DeferredBlock<LumenCoralBlock> LUMEN_CORAL = BLOCKS.registerBlock(
            "lumen_coral",
            LumenCoralBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 9)
                    .sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY));

    /** Lumen Kelp — a glowing waterlogged frond (a teal-green sea plant; reuses {@link LumenCoralBlock}). */
    public static final DeferredBlock<LumenCoralBlock> LUMEN_KELP = BLOCKS.registerBlock(
            "lumen_kelp",
            LumenCoralBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 7)
                    .sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY));

    // --- Surface harvestables (Phase 9) — alien fruits/veggies scattered on the land ----------------

    /**
     * Lumenberry Bush (v1.1c) — a renewable, plantable, bone-mealable alien berry bush
     * ({@link com.jus144tice.lumenwilds.block.LumenberryBushBlock}, sweet-berry-style): ripens through
     * {@code AGE 0..3}, glows brighter as it matures (light 3→6), harvested by right-click. Planted with the
     * Lumenberry item (an {@code ItemNameBlockItem} over this block). No standalone BlockItem (skipped in the
     * {@code ModItems} loop); its loot is hand-authored to drop Lumenberries.
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenberryBushBlock> LUMENBERRY_BUSH =
            BLOCKS.registerBlock(
                    "lumenberry_bush",
                    com.jus144tice.lumenwilds.block.LumenberryBushBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .lightLevel(com.jus144tice.lumenwilds.block.LumenberryBushBlock::lightFor)
                            .sound(SoundType.SWEET_BERRY_BUSH)
                            .pushReaction(PushReaction.DESTROY));

    // --- Native crops (v1.4 Farming) — grow in dim light, faster on Lumenwater-hydrated Lumen Farmland.
    // Mature stages glow faintly (bioluminescence). Planted via ItemNameBlockItem seeds (no own BlockItem).
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumengrainCropBlock> LUMENGRAIN_CROP =
            BLOCKS.registerBlock(
                    "lumengrain_crop",
                    com.jus144tice.lumenwilds.block.LumengrainCropBlock::new,
                    cropProps()
                            .lightLevel(s -> cropGlow(s.getValue(net.minecraft.world.level.block.CropBlock.AGE), 7)));
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.GlimmerrootCropBlock> GLIMMERROOT_CROP =
            BLOCKS.registerBlock(
                    "glimmerroot_crop",
                    com.jus144tice.lumenwilds.block.GlimmerrootCropBlock::new,
                    cropProps()
                            .lightLevel(s -> cropGlow(s.getValue(net.minecraft.world.level.block.CropBlock.AGE), 7)));
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.MoonbeetCropBlock> MOONBEET_CROP =
            BLOCKS.registerBlock(
                    "moonbeet_crop",
                    com.jus144tice.lumenwilds.block.MoonbeetCropBlock::new,
                    cropProps()
                            .lightLevel(s ->
                                    cropGlow(s.getValue(com.jus144tice.lumenwilds.block.MoonbeetCropBlock.AGE), 3)));

    // --- Gourds (v1.4 F3) — melon/pumpkin-analog stem crops. Stems reference fruit/seed by ResourceKey (lazy,
    // no registration-order issue). Seeds are ItemNameBlockItems over the stems (planted on tilled/native soil).
    /** Moonmelon — the glowing melon-analog gourd (a light source); broken for slices. */
    public static final DeferredBlock<Block> MOONMELON = BLOCKS.registerSimpleBlock(
            "moonmelon",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(1.0F)
                    .lightLevel(s -> 9)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

    // Custom LumenStemBlock/LumenAttachedStemBlock so stems survive on Lumen Farmland + Moonloam (vanilla stems
    // accept ONLY minecraft:farmland → they broke instantly on hoed Lumenwilds soil, v1.5.0).
    public static final DeferredBlock<StemBlock> MOONMELON_STEM = BLOCKS.registerBlock(
            "moonmelon_stem",
            p -> new com.jus144tice.lumenwilds.block.LumenStemBlock(
                    bKey("moonmelon"), bKey("attached_moonmelon_stem"), iKey("moonmelon_seeds"), p),
            stemProps());
    public static final DeferredBlock<AttachedStemBlock> ATTACHED_MOONMELON_STEM = BLOCKS.registerBlock(
            "attached_moonmelon_stem",
            p -> new com.jus144tice.lumenwilds.block.LumenAttachedStemBlock(
                    bKey("moonmelon_stem"), bKey("moonmelon"), iKey("moonmelon_seeds"), p),
            attachedStemProps());

    /** Glowgourd — the pumpkin-analog gourd; shear it to carve a glowing Carved Glowgourd. */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.GlowgourdBlock> GLOWGOURD = BLOCKS.registerBlock(
            "glowgourd",
            com.jus144tice.lumenwilds.block.GlowgourdBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(1.0F)
                    .lightLevel(s -> 5)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<net.minecraft.world.level.block.CarvedPumpkinBlock> CARVED_GLOWGOURD =
            BLOCKS.registerBlock(
                    "carved_glowgourd",
                    net.minecraft.world.level.block.CarvedPumpkinBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .strength(1.0F)
                            .lightLevel(s -> 11)
                            .sound(SoundType.WOOD)
                            .pushReaction(PushReaction.DESTROY));
    public static final DeferredBlock<StemBlock> GLOWGOURD_STEM = BLOCKS.registerBlock(
            "glowgourd_stem",
            p -> new com.jus144tice.lumenwilds.block.LumenStemBlock(
                    bKey("glowgourd"), bKey("attached_glowgourd_stem"), iKey("glowgourd_seeds"), p),
            stemProps());
    public static final DeferredBlock<AttachedStemBlock> ATTACHED_GLOWGOURD_STEM = BLOCKS.registerBlock(
            "attached_glowgourd_stem",
            p -> new com.jus144tice.lumenwilds.block.LumenAttachedStemBlock(
                    bKey("glowgourd_stem"), bKey("glowgourd"), iKey("glowgourd_seeds"), p),
            attachedStemProps());

    // --- Alien crops (v1.4 F4) ------------------------------------------------------------------
    /** Glimmerreed — the Lumenwater-loving cane (a plain sugar-cane: grows on Moonloam beside Lumenwater, since
     * Lumenwater is #minecraft:water + hydrates). Glows. Planted/dropped via the GLIMMERREED item (no own item). */
    public static final DeferredBlock<net.minecraft.world.level.block.SugarCaneBlock> GLIMMERREED =
            BLOCKS.registerBlock(
                    "glimmerreed",
                    net.minecraft.world.level.block.SugarCaneBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .randomTicks()
                            .instabreak()
                            .lightLevel(s -> 5)
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY));
    /** Duskbean — the darkness-loving crop (grows in the dark, AGE_3). */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.DuskbeanCropBlock> DUSKBEAN_CROP =
            BLOCKS.registerBlock(
                    "duskbean_crop",
                    com.jus144tice.lumenwilds.block.DuskbeanCropBlock::new,
                    cropProps()
                            .lightLevel(s ->
                                    cropGlow(s.getValue(com.jus144tice.lumenwilds.block.DuskbeanCropBlock.AGE), 3)));
    /** Cavecap — the stone-grown cave fungus (grows on Moonstone/strata, AGE_3). */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.CavecapCropBlock> CAVECAP_CROP =
            BLOCKS.registerBlock(
                    "cavecap_crop",
                    com.jus144tice.lumenwilds.block.CavecapCropBlock::new,
                    cropProps()
                            .lightLevel(s ->
                                    cropGlow(s.getValue(com.jus144tice.lumenwilds.block.CavecapCropBlock.AGE), 3)));

    // --- Stillbloom Basin — the giant Stillbloom flower (Phase 5d.6) ----------------------------
    // Three soft, glowing blocks assembled by StillbloomFeature into a 3–8-tall giant flower: a stem
    // column carrying a petal dome around a brilliant core. The brightest blocks in the dimension.

    /** Stillbloom Stem — the soft glowing stalk of a giant Stillbloom. */
    public static final DeferredBlock<Block> STILLBLOOM_STEM = BLOCKS.registerSimpleBlock(
            "stillbloom_stem",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F)
                    .lightLevel(state -> 3)
                    .sound(SoundType.WET_GRASS));

    /** Stillbloom Petal — the pale blue-white petals; glow softly (light 7). */
    public static final DeferredBlock<Block> STILLBLOOM_PETAL = BLOCKS.registerSimpleBlock(
            "stillbloom_petal",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(0.2F)
                    .lightLevel(state -> 7)
                    .sound(SoundType.WART_BLOCK));

    /** Stillbloom Core — the brilliant heart of the bloom (light 12); hostiles shun it (Phase 6). */
    public static final DeferredBlock<Block> STILLBLOOM_CORE = BLOCKS.registerSimpleBlock(
            "stillbloom_core",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(0.4F)
                    .lightLevel(state -> 12)
                    .sound(SoundType.WART_BLOCK));

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

    /**
     * Bottled Lantern Beetle (Phase 6c) — a caught {@code LanternBeetle} set down as a small glowing lamp
     * (light 12). Sits on a flat surface (needs support below); see {@code block.BottledLanternBeetleBlock}.
     * Obtained by right-clicking a Lantern Beetle with a glass bottle; its `BlockItem` is auto-registered.
     */
    public static final DeferredBlock<BottledLanternBeetleBlock> BOTTLED_LANTERN_BEETLE = BLOCKS.registerBlock(
            "bottled_lantern_beetle",
            BottledLanternBeetleBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.1F)
                    .lightLevel(state -> 12)
                    .noOcclusion()
                    .sound(SoundType.GLASS)
                    .pushReaction(PushReaction.DESTROY));

    // --- Resource / storage ---------------------------------------------------------------------

    public static final DeferredBlock<Block> LUMEN_CRYSTAL_BLOCK = BLOCKS.registerSimpleBlock(
            "lumen_crystal_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 6)
                    .sound(SoundType.AMETHYST));

    /** Glasspetal Block — the solid blue-violet crystal the Glasspetal Crags' growths + spires are built from. */
    public static final DeferredBlock<Block> GLASSPETAL_BLOCK = BLOCKS.registerSimpleBlock(
            "glasspetal_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(2.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 7)
                    .sound(SoundType.AMETHYST));

    /** Lumen Crystal Ore — drops Lumen Crystal Shard; glows faintly (helps light caves). */
    public static final DeferredBlock<DropExperienceBlock> LUMEN_CRYSTAL_ORE = BLOCKS.registerBlock(
            "lumen_crystal_ore",
            props -> new DropExperienceBlock(UniformInt.of(2, 5), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 6)
                    .sound(SoundType.STONE));

    public static final DeferredBlock<DropExperienceBlock> DEEP_LUMEN_CRYSTAL_ORE = BLOCKS.registerBlock(
            "deep_lumen_crystal_ore",
            props -> new DropExperienceBlock(UniformInt.of(2, 5), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 6)
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

    /** Glowcap cap — azure variant (the standard Glowcaps come in a few colours so the jungle varies). */
    public static final DeferredBlock<Block> GIANT_GLOWCAP_AZURE = BLOCKS.registerSimpleBlock(
            "giant_glowcap_azure",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(0.2F)
                    .lightLevel(state -> 9)
                    .sound(SoundType.WOOD)
                    .ignitedByLava());

    /** Glowcap cap — violet variant. */
    public static final DeferredBlock<Block> GIANT_GLOWCAP_VIOLET = BLOCKS.registerSimpleBlock(
            "giant_glowcap_violet",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.2F)
                    .lightLevel(state -> 9)
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
                    .lightLevel(state -> 5)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<TrapDoorBlock> GLOWWOOD_TRAPDOOR = BLOCKS.registerBlock(
            "glowwood_trapdoor",
            props -> new TrapDoorBlock(ModWoodTypes.GLOWWOOD_SET, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.0F)
                    .lightLevel(state -> 5)
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

    // --- Glowroot building set (Phase v1.1a) ----------------------------------------------------
    // The signature self-lit tree's full wood set (GLOWROOT_LOG/LEAVES/SAPLING are declared above). Unlike
    // Glowwood, the whole set glows faintly (glowrootLogProps light 4 / glowrootPlanksProps light 3) — living
    // wood that carries its own light. Uses the bespoke GLOWROOT WoodType/BlockSetType (ModWoodTypes).

    public static final DeferredBlock<RotatedPillarBlock> GLOWROOT_WOOD =
            BLOCKS.registerBlock("glowroot_wood", RotatedPillarBlock::new, glowrootLogProps());

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_GLOWROOT_LOG =
            BLOCKS.registerBlock("stripped_glowroot_log", RotatedPillarBlock::new, glowrootLogProps());

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_GLOWROOT_WOOD =
            BLOCKS.registerBlock("stripped_glowroot_wood", RotatedPillarBlock::new, glowrootLogProps());

    public static final DeferredBlock<Block> GLOWROOT_PLANKS =
            BLOCKS.registerSimpleBlock("glowroot_planks", glowrootPlanksProps());

    public static final DeferredBlock<StairBlock> GLOWROOT_STAIRS = BLOCKS.registerBlock(
            "glowroot_stairs",
            props -> new StairBlock(GLOWROOT_PLANKS.get().defaultBlockState(), props),
            glowrootPlanksProps());

    public static final DeferredBlock<SlabBlock> GLOWROOT_SLAB =
            BLOCKS.registerBlock("glowroot_slab", SlabBlock::new, glowrootPlanksProps());

    public static final DeferredBlock<FenceBlock> GLOWROOT_FENCE =
            BLOCKS.registerBlock("glowroot_fence", FenceBlock::new, glowrootPlanksProps());

    public static final DeferredBlock<FenceGateBlock> GLOWROOT_FENCE_GATE = BLOCKS.registerBlock(
            "glowroot_fence_gate", props -> new FenceGateBlock(ModWoodTypes.GLOWROOT, props), glowrootPlanksProps());

    public static final DeferredBlock<DoorBlock> GLOWROOT_DOOR = BLOCKS.registerBlock(
            "glowroot_door",
            props -> new DoorBlock(ModWoodTypes.GLOWROOT_SET, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0F)
                    .lightLevel(state -> 5)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<TrapDoorBlock> GLOWROOT_TRAPDOOR = BLOCKS.registerBlock(
            "glowroot_trapdoor",
            props -> new TrapDoorBlock(ModWoodTypes.GLOWROOT_SET, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0F)
                    .lightLevel(state -> 5)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .isValidSpawn((s, l, p, e) -> false));

    public static final DeferredBlock<ButtonBlock> GLOWROOT_BUTTON = BLOCKS.registerBlock(
            "glowroot_button",
            props -> new ButtonBlock(ModWoodTypes.GLOWROOT_SET, 30, props),
            BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(0.5F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<PressurePlateBlock> GLOWROOT_PRESSURE_PLATE = BLOCKS.registerBlock(
            "glowroot_pressure_plate",
            props -> new PressurePlateBlock(ModWoodTypes.GLOWROOT_SET, props),
            BlockBehaviour.Properties.of()
                    .noCollission()
                    .strength(0.5F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<StandingSignBlock> GLOWROOT_SIGN = BLOCKS.registerBlock(
            "glowroot_sign", props -> new StandingSignBlock(ModWoodTypes.GLOWROOT, props), signProps());

    public static final DeferredBlock<WallSignBlock> GLOWROOT_WALL_SIGN = BLOCKS.registerBlock(
            "glowroot_wall_sign", props -> new WallSignBlock(ModWoodTypes.GLOWROOT, props), signProps());

    public static final DeferredBlock<CeilingHangingSignBlock> GLOWROOT_HANGING_SIGN = BLOCKS.registerBlock(
            "glowroot_hanging_sign", props -> new CeilingHangingSignBlock(ModWoodTypes.GLOWROOT, props), signProps());

    public static final DeferredBlock<WallHangingSignBlock> GLOWROOT_WALL_HANGING_SIGN = BLOCKS.registerBlock(
            "glowroot_wall_hanging_sign", props -> new WallHangingSignBlock(ModWoodTypes.GLOWROOT, props), signProps());

    // --- Glowing wood containers (v1.1.3) -------------------------------------------------------
    // Barrels reuse the vanilla BarrelBlock + BlockEntityType.BARREL (the blocks are added to that BE type in
    // event.ModBlockEntityTypes, like the signs). They render as a normal MODEL, so the emissive model parents
    // make them glow; lightLevel makes them also cast light. (The matching glowing chests are a separate block
    // with a custom block-entity renderer.)
    public static final DeferredBlock<net.minecraft.world.level.block.BarrelBlock> GLOWWOOD_BARREL =
            BLOCKS.registerBlock("glowwood_barrel", net.minecraft.world.level.block.BarrelBlock::new, barrelProps());

    public static final DeferredBlock<net.minecraft.world.level.block.BarrelBlock> GLOWROOT_BARREL =
            BLOCKS.registerBlock("glowroot_barrel", net.minecraft.world.level.block.BarrelBlock::new, barrelProps());

    // Chests use block.LumenChestBlock (a ChestBlock subclass) so they actually create our LumenChestBlockEntity
    // (ModBlockEntities#LUMEN_CHEST) — a plain ChestBlock hardcodes a vanilla minecraft:chest BE and crashes on
    // placement at our block (v1.4.6 fix). The bespoke client.LumenChestRenderer gives each species its glowing
    // texture. Light 7.
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenChestBlock> GLOWWOOD_CHEST =
            BLOCKS.registerBlock("glowwood_chest", com.jus144tice.lumenwilds.block.LumenChestBlock::new, barrelProps());

    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenChestBlock> GLOWROOT_CHEST =
            BLOCKS.registerBlock("glowroot_chest", com.jus144tice.lumenwilds.block.LumenChestBlock::new, barrelProps());

    // --- Wood variants (v1.4.2, Quark parity) — bookshelf + ladder + post for both species. All glow like
    // the rest of the wood sets. Chests + barrels already exist above. Limonero/other-mod woods are Quark's job.
    public static final DeferredBlock<Block> GLOWWOOD_BOOKSHELF =
            BLOCKS.registerSimpleBlock("glowwood_bookshelf", planksProps());
    public static final DeferredBlock<Block> GLOWROOT_BOOKSHELF =
            BLOCKS.registerSimpleBlock("glowroot_bookshelf", glowrootPlanksProps());
    public static final DeferredBlock<net.minecraft.world.level.block.LadderBlock> GLOWWOOD_LADDER =
            BLOCKS.registerBlock("glowwood_ladder", net.minecraft.world.level.block.LadderBlock::new, ladderProps());
    public static final DeferredBlock<net.minecraft.world.level.block.LadderBlock> GLOWROOT_LADDER =
            BLOCKS.registerBlock("glowroot_ladder", net.minecraft.world.level.block.LadderBlock::new, ladderProps());
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.WoodPostBlock> GLOWWOOD_POST =
            BLOCKS.registerBlock(
                    "glowwood_post",
                    com.jus144tice.lumenwilds.block.WoodPostBlock::new,
                    planksProps().noOcclusion());
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.WoodPostBlock> GLOWROOT_POST =
            BLOCKS.registerBlock(
                    "glowroot_post",
                    com.jus144tice.lumenwilds.block.WoodPostBlock::new,
                    glowrootPlanksProps().noOcclusion());

    // --- Moonstone stone set (Phase 4) ----------------------------------------------------------
    // MOONSTONE + COBBLED_MOONSTONE are declared above. Cube variants first, then stairs/slabs/walls.

    public static final DeferredBlock<Block> SMOOTH_MOONSTONE = moonCube("smooth_moonstone");
    public static final DeferredBlock<Block> MOONSTONE_BRICKS = moonCube("moonstone_bricks");
    public static final DeferredBlock<Block> CHISELED_MOONSTONE = moonCube("chiseled_moonstone");
    public static final DeferredBlock<Block> MOONSTONE_TILES = moonCube("moonstone_tiles");
    public static final DeferredBlock<Block> CRACKED_MOONSTONE_BRICKS = moonCube("cracked_moonstone_bricks");

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

    /**
     * Pale Tuff (v1.3 mining overhaul, Phase B) — a soft, pale tuff-style accent rock generated as blobs
     * through the Deep Moonstone band, breaking up the deep strata (and a host for deep ores in Phase C).
     * Deepslate-tier hardness; drops self.
     */
    public static final DeferredBlock<Block> PALE_TUFF = BLOCKS.registerSimpleBlock("pale_tuff", deepProps());

    public static final DeferredBlock<Block> COBBLED_DEEP_MOONSTONE = deepCube("cobbled_deep_moonstone");
    public static final DeferredBlock<Block> POLISHED_DEEP_MOONSTONE = deepCube("polished_deep_moonstone");
    public static final DeferredBlock<Block> DEEP_MOONSTONE_BRICKS = deepCube("deep_moonstone_bricks");
    public static final DeferredBlock<Block> DEEP_MOONSTONE_TILES = deepCube("deep_moonstone_tiles");
    public static final DeferredBlock<Block> CRACKED_DEEP_MOONSTONE_BRICKS = deepCube("cracked_deep_moonstone_bricks");

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

    // --- Luminite (the Lumenwrights' ancient structural metal, Phase 10a) -----------------------
    // Refined into Glowbrick, the signature material of the Vestige Cities. Ore generates in moonstone
    // (surface) + deep moonstone (deep) and drops raw_luminite (smelt → luminite_ingot). A plain mineral
    // metal — it does NOT glow (it contrasts with the self-lit Lumen Crystal Ore). See ancient_cities.txt.

    public static final DeferredBlock<DropExperienceBlock> LUMINITE_ORE = BLOCKS.registerBlock(
            "luminite_ore",
            props -> new DropExperienceBlock(UniformInt.of(1, 3), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE));

    public static final DeferredBlock<DropExperienceBlock> DEEP_LUMINITE_ORE = BLOCKS.registerBlock(
            "deep_luminite_ore",
            props -> new DropExperienceBlock(UniformInt.of(1, 3), props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE));

    /** Luminite Block — refined ingot storage; the metallic luster of the Lumenwright alloy. */
    public static final DeferredBlock<Block> LUMINITE_BLOCK = BLOCKS.registerSimpleBlock(
            "luminite_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(4.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL));

    // --- Mining-overhaul ores (v1.3 Phase C) -----------------------------------------------------
    // Three new ores, each with a deep variant (DEEPSLATE-tinted, tougher), generated in the moonstone +
    // deep-moonstone + veinstone + pale_tuff rock (worldgen) and depth-banded so digging deeper pays off.
    // Loot routes by name in ModLootTableProvider (createOreDrop); harvest tier in ModTagProvider.
    /** Emberglow — the dimension's coal/fuel analog (drops Emberglow, a furnace fuel). Faint glow 3. */
    public static final DeferredBlock<DropExperienceBlock> EMBERGLOW_ORE = oreBlock("emberglow_ore", 1, 3, false, 3);

    public static final DeferredBlock<DropExperienceBlock> DEEP_EMBERGLOW_ORE =
            oreBlock("deep_emberglow_ore", 1, 3, true, 3);
    /** Pale Opal — a decorative/trade gem (drops Pale Opal). Non-glowing, deep. */
    public static final DeferredBlock<DropExperienceBlock> PALE_OPAL_ORE = oreBlock("pale_opal_ore", 2, 5, false, 0);

    public static final DeferredBlock<DropExperienceBlock> DEEP_PALE_OPAL_ORE =
            oreBlock("deep_pale_opal_ore", 2, 5, true, 0);
    /** Resonite — the rare deep treasure (drops Raw Resonite → ingot → the Phase-D gear tier). Cold glow 5. */
    public static final DeferredBlock<DropExperienceBlock> RESONITE_ORE = oreBlock("resonite_ore", 3, 7, false, 5);

    public static final DeferredBlock<DropExperienceBlock> DEEP_RESONITE_ORE =
            oreBlock("deep_resonite_ore", 3, 7, true, 5);

    /** Emberglow Block — fuel storage (a fuel block, like a coal block; see event.ModFuels). Glow 3. */
    public static final DeferredBlock<Block> EMBERGLOW_BLOCK = BLOCKS.registerSimpleBlock(
            "emberglow_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 3)
                    .sound(SoundType.STONE));
    // Emberglow Torch (v1.4.7) — a torch crafted from Emberglow (the fuel material) that burns with a bespoke
    // greenish-blue flame (ModParticles#EMBERGLOW_FLAME). Functions exactly like a vanilla torch (light 14,
    // floor + wall placement). The wall variant has no own item; the standing torch item places both.
    public static final DeferredBlock<net.minecraft.world.level.block.TorchBlock> EMBERGLOW_TORCH =
            BLOCKS.registerBlock(
                    "emberglow_torch",
                    props -> new net.minecraft.world.level.block.TorchBlock(
                            net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME, props),
                    torchProps());
    public static final DeferredBlock<net.minecraft.world.level.block.WallTorchBlock> EMBERGLOW_WALL_TORCH =
            BLOCKS.registerBlock(
                    "emberglow_wall_torch",
                    props -> new net.minecraft.world.level.block.WallTorchBlock(
                            net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME, props),
                    torchProps().lootFrom(EMBERGLOW_TORCH));

    /** Pale Opal Block — decorative gem storage. */
    public static final DeferredBlock<Block> PALE_OPAL_BLOCK = BLOCKS.registerSimpleBlock(
            "pale_opal_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST));
    /** Resonite Block — refined resonite-ingot storage; glows with the Lumenwrights' resonant cold light. */
    public static final DeferredBlock<Block> RESONITE_BLOCK = BLOCKS.registerSimpleBlock(
            "resonite_block",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 5)
                    .sound(SoundType.METAL));

    // --- Lumen Geode (v1.3 Phase E1) -------------------------------------------------------------
    // A buried crystal pocket (the `lumen_geode` feature) lined with Lumen Crystal Block, Shimmerstone, and
    // Deep Moonstone, with Budding Lumen Crystal growing renewable crystal buds → clusters (shards = the payoff).
    /** Budding Lumen Crystal — grows the buds below (see block.BuddingLumenCrystalBlock); not harvestable. */
    public static final DeferredBlock<BuddingLumenCrystalBlock> BUDDING_LUMEN_CRYSTAL = BLOCKS.registerBlock(
            "budding_lumen_crystal",
            BuddingLumenCrystalBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(1.5F)
                    .randomTicks()
                    .requiresCorrectToolForDrops()
                    .lightLevel(s -> 4)
                    .sound(SoundType.AMETHYST)
                    .noLootTable());

    public static final DeferredBlock<AmethystClusterBlock> SMALL_LUMEN_CRYSTAL_BUD =
            lumenBud("small_lumen_crystal_bud", 3.0F, 4.0F, 2);
    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_LUMEN_CRYSTAL_BUD =
            lumenBud("medium_lumen_crystal_bud", 4.0F, 3.0F, 3);
    public static final DeferredBlock<AmethystClusterBlock> LARGE_LUMEN_CRYSTAL_BUD =
            lumenBud("large_lumen_crystal_bud", 5.0F, 3.0F, 4);
    public static final DeferredBlock<AmethystClusterBlock> LUMEN_CRYSTAL_CLUSTER =
            lumenBud("lumen_crystal_cluster", 7.0F, 3.0F, 5);

    // --- Glowbrick (Lumenwright luminous architecture, Phase 10a) -------------------------------
    // The signature building material of the Vestige Cities: a luminous alien brick refined from Luminite.
    // Architectural light, NOT a lantern — intact glowbrick glows 6, cracked 3, ancient barely 1, so ruins
    // visibly fade from bright intact areas to dying old sections. Higher blast resistance than stone.

    public static final DeferredBlock<Block> GLOWBRICK = BLOCKS.registerSimpleBlock("glowbrick", glowbrickProps(6));
    public static final DeferredBlock<Block> CRACKED_GLOWBRICK =
            BLOCKS.registerSimpleBlock("cracked_glowbrick", glowbrickProps(3));
    public static final DeferredBlock<Block> ANCIENT_GLOWBRICK =
            BLOCKS.registerSimpleBlock("ancient_glowbrick", glowbrickProps(1));
    public static final DeferredBlock<Block> GLOWBRICK_TILES =
            BLOCKS.registerSimpleBlock("glowbrick_tiles", glowbrickProps(6));
    public static final DeferredBlock<Block> CHISELED_GLOWBRICK =
            BLOCKS.registerSimpleBlock("chiseled_glowbrick", glowbrickProps(6));

    public static final DeferredBlock<RotatedPillarBlock> GLOWBRICK_PILLAR =
            BLOCKS.registerBlock("glowbrick_pillar", RotatedPillarBlock::new, glowbrickProps(6));

    public static final DeferredBlock<StairBlock> GLOWBRICK_STAIRS = glowbrickStairs("glowbrick_stairs", GLOWBRICK);
    public static final DeferredBlock<SlabBlock> GLOWBRICK_SLAB =
            BLOCKS.registerBlock("glowbrick_slab", SlabBlock::new, glowbrickProps(6));
    public static final DeferredBlock<WallBlock> GLOWBRICK_WALL =
            BLOCKS.registerBlock("glowbrick_wall", WallBlock::new, glowbrickProps(6));

    // --- Vestige decay blocks (Phase 10a) — used by the ruined-city decay/overgrowth processors --

    /** Overgrown Glowbrick — glowbrick reclaimed by living growth; faint residual glow. */
    public static final DeferredBlock<Block> OVERGROWN_GLOWBRICK =
            BLOCKS.registerSimpleBlock("overgrown_glowbrick", glowbrickProps(2));

    /** Broken Sporeglass — shattered, dimmer sporeglass for ruined city windows. */
    public static final DeferredBlock<TransparentBlock> BROKEN_SPOREGLASS = BLOCKS.registerBlock(
            "broken_sporeglass",
            TransparentBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(0.3F)
                    .lightLevel(state -> 2)
                    .sound(SoundType.GLASS)
                    .noOcclusion());

    /** Mossy Moonstone Bricks — moonstone bricks reclaimed by moss (a decay variant). */
    public static final DeferredBlock<Block> MOSSY_MOONSTONE_BRICKS = moonCube("mossy_moonstone_bricks");

    /** Rooted Moonstone — moonstone shot through with living roots (a decay variant). */
    public static final DeferredBlock<Block> ROOTED_MOONSTONE = moonCube("rooted_moonstone");

    // --- Lumenwright lore tech (Phase 10c) ------------------------------------------------------

    /**
     * Memory Crystal — a tall glowing crystal that stores fragments of the Lumenwrights' memory. Right-click
     * (empty hand) to read a fragmented lore line via {@code event.MemoryCrystalInteractEvents}; drops a
     * {@code memory_crystal_shard}. Emissive (model parents {@code _emissive_cube}); light 11.
     */
    public static final DeferredBlock<Block> MEMORY_CRYSTAL = BLOCKS.registerSimpleBlock(
            "memory_crystal",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 11)
                    .noOcclusion()
                    .sound(SoundType.AMETHYST));

    /**
     * Lumen Conduit — the cities' glowing energy lines ({@link com.jus144tice.lumenwilds.block.LumenConduitBlock}).
     * Its {@code conduit_state} (dead/dim/active) sets the light level (0/2/8); decorative in 10c (ruins place
     * dead/dim), driven dynamically by the Resonance network in 10e.
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenConduitBlock> LUMEN_CONDUIT =
            BLOCKS.registerBlock(
                    "lumen_conduit",
                    com.jus144tice.lumenwilds.block.LumenConduitBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(1.5F, 6.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(com.jus144tice.lumenwilds.block.LumenConduitBlock::lightFor)
                            .sound(SoundType.STONE));

    // --- Resonance tech (Phase 10e) -------------------------------------------------------------

    /**
     * Resonance Core ({@link com.jus144tice.lumenwilds.block.ResonanceCoreBlock}) — the network power source.
     * A placed core floods power through connected Lumen Conduits and opens the ancient doors they reach. The
     * second `ModBlockEntities` content; glows steadily (light 10).
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.ResonanceCoreBlock> RESONANCE_CORE =
            BLOCKS.registerBlock(
                    "resonance_core",
                    com.jus144tice.lumenwilds.block.ResonanceCoreBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(3.0F, 9.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 10)
                            .noOcclusion()
                            .sound(SoundType.AMETHYST));

    /**
     * Ancient Door — a heavy alien door that cannot be opened by hand (iron block-set), only by the Resonance
     * network ({@code block.ResonanceNetwork} sets it open while a powered conduit/core touches it).
     */
    public static final DeferredBlock<DoorBlock> ANCIENT_DOOR = BLOCKS.registerBlock(
            "ancient_door",
            props -> new DoorBlock(net.minecraft.world.level.block.state.properties.BlockSetType.IRON, props),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.0F, 9.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 3)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY));

    // --- Gravity tech (Phase 10e.2; shared with the future liftshafts) --------------------------

    /**
     * Gravity Lens ({@link com.jus144tice.lumenwilds.block.GravityLensBlock}) — a powered lens gently lifts
     * entities above it (`event.LumenGravityEvents`). Light 6 powered / 2 dead; set powered by the network.
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.GravityLensBlock> GRAVITY_LENS =
            BLOCKS.registerBlock(
                    "gravity_lens",
                    com.jus144tice.lumenwilds.block.GravityLensBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(2.0F, 6.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(com.jus144tice.lumenwilds.block.GravityLensBlock::lightFor)
                            .noOcclusion()
                            .sound(SoundType.AMETHYST));

    /** Cracked Gravity Lens — the common ruined lens; drops a {@code gravity_lens_fragment}. */
    public static final DeferredBlock<Block> CRACKED_GRAVITY_LENS = BLOCKS.registerSimpleBlock(
            "cracked_gravity_lens",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 1)
                    .noOcclusion()
                    .sound(SoundType.AMETHYST));

    /** Lumen Relay ({@link com.jus144tice.lumenwilds.block.LumenRelayBlock}) — bridges the resonance network across gaps. */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenRelayBlock> LUMEN_RELAY =
            BLOCKS.registerBlock(
                    "lumen_relay",
                    com.jus144tice.lumenwilds.block.LumenRelayBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(2.0F, 6.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 4)
                            .sound(SoundType.AMETHYST));

    /**
     * Dormant Light Engine ({@link com.jus144tice.lumenwilds.block.DormantLightEngineBlock}) — the dead city
     * centrepiece; right-click with a {@code resonance_core_fragment} to restore it to an Active Light Engine.
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.DormantLightEngineBlock> DORMANT_LIGHT_ENGINE =
            BLOCKS.registerBlock(
                    "dormant_light_engine",
                    com.jus144tice.lumenwilds.block.DormantLightEngineBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(4.0F, 12.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 3)
                            .noOcclusion()
                            .sound(SoundType.AMETHYST));

    /**
     * Active Light Engine ({@link com.jus144tice.lumenwilds.block.ActiveLightEngineBlock}, a Resonance Core) —
     * the restored engine, brightly lit (13), powering the city's conduit network.
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.ActiveLightEngineBlock> ACTIVE_LIGHT_ENGINE =
            BLOCKS.registerBlock(
                    "active_light_engine",
                    com.jus144tice.lumenwilds.block.ActiveLightEngineBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(4.0F, 12.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 13)
                            .noOcclusion()
                            .sound(SoundType.AMETHYST));

    // --- Liftshaft fields (Phase 11a; the bible's gravity columns) ------------------------------

    /**
     * Ascension Field ({@link com.jus144tice.lumenwilds.block.AscensionFieldBlock}) — the upward gravity column
     * cell. Non-solid, no-collision, unbreakable (strength -1), no BlockItem, {@code noLootTable}; projected by
     * a Lumen Field Projector (11b) and pre-placed in ruin shafts. Glows (light 7).
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.AscensionFieldBlock> ASCENSION_FIELD =
            BLOCKS.registerBlock(
                    "ascension_field",
                    com.jus144tice.lumenwilds.block.AscensionFieldBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_LIGHT_BLUE)
                            .noCollission()
                            .noLootTable()
                            .strength(-1.0F)
                            .lightLevel(state -> 7)
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY));

    /**
     * Descent Field ({@link com.jus144tice.lumenwilds.block.DescentFieldBlock}) — the downward gravity column
     * cell (controlled safe descent). Same plumbing as the ascension field; glows softer (light 5).
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.DescentFieldBlock> DESCENT_FIELD =
            BLOCKS.registerBlock(
                    "descent_field",
                    com.jus144tice.lumenwilds.block.DescentFieldBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .noCollission()
                            .noLootTable()
                            .strength(-1.0F)
                            .lightLevel(state -> 5)
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY));

    /**
     * Lumen Field Projector ({@link com.jus144tice.lumenwilds.block.LumenFieldProjectorBlock}) — the player's
     * craftable liftshaft source (Phase 11b). A `BaseEntityBlock`; right-click toggles its mode (ascend/descend)
     * and its block entity projects/clears the field column. Glows (light 6). The 3rd `ModBlockEntities` content.
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.LumenFieldProjectorBlock> LUMEN_FIELD_PROJECTOR =
            BLOCKS.registerBlock(
                    "lumen_field_projector",
                    com.jus144tice.lumenwilds.block.LumenFieldProjectorBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .strength(3.0F, 9.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 6)
                            .noOcclusion()
                            .sound(SoundType.AMETHYST));

    /**
     * Gravity Repeater ({@link com.jus144tice.lumenwilds.block.GravityRepeaterBlock}) — a flush wall block that
     * extends a liftshaft another segment whenever a face touches the field (Phase 11b). Glows faintly (light 3).
     */
    public static final DeferredBlock<com.jus144tice.lumenwilds.block.GravityRepeaterBlock> GRAVITY_REPEATER =
            BLOCKS.registerBlock(
                    "gravity_repeater",
                    com.jus144tice.lumenwilds.block.GravityRepeaterBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(2.0F, 6.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 3)
                            .sound(SoundType.AMETHYST));

    // --- Property + stone-family helpers --------------------------------------------------------

    private static BlockBehaviour.Properties glowbrickProps(int light) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .strength(2.0F, 9.0F) // higher blast resistance than stone (6.0)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> light)
                .sound(SoundType.STONE);
    }

    private static DeferredBlock<StairBlock> glowbrickStairs(String name, DeferredBlock<? extends Block> base) {
        return BLOCKS.registerBlock(
                name, props -> new StairBlock(base.get().defaultBlockState(), props), glowbrickProps(6));
    }

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

    /** Shared crop block properties (v1.4 Farming): passable, random-ticking, instant-break, crop sound. */
    private static BlockBehaviour.Properties cropProps() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    /** Stem (gourd crop) properties — passable, random-ticking, instant-break. */
    private static BlockBehaviour.Properties stemProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.HARD_CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    /** Attached-stem properties (the bent stem once a gourd has grown). */
    private static BlockBehaviour.Properties attachedStemProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY);
    }

    /** A {@code ResourceKey<Block>} for {@code lumenwilds:<n>} (lazy stem→fruit wiring; no registration order). */
    private static net.minecraft.resources.ResourceKey<Block> bKey(String n) {
        return net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.BLOCK,
                com.jus144tice.lumenwilds.util.ResourceLocationHelper.modLoc(n));
    }

    /** A {@code ResourceKey<Item>} for {@code lumenwilds:<n>} (the stem's seed item). */
    private static net.minecraft.resources.ResourceKey<net.minecraft.world.item.Item> iKey(String n) {
        return net.minecraft.resources.ResourceKey.create(
                net.minecraft.core.registries.Registries.ITEM,
                com.jus144tice.lumenwilds.util.ResourceLocationHelper.modLoc(n));
    }

    /** Bioluminescent crop glow by age: dark when young, faint mid-growth, brighter when mature. */
    private static int cropGlow(int age, int maxAge) {
        if (age >= maxAge) {
            return 5;
        }
        if (age >= maxAge - (maxAge >= 7 ? 4 : 1)) {
            return 2;
        }
        return 0;
    }

    /** Lumen-crystal bud/cluster factory (v1.3 Phase E1): an {@link AmethystClusterBlock} that glows faintly. */
    private static DeferredBlock<AmethystClusterBlock> lumenBud(
            String name, float height, float aabbOffset, int light) {
        return BLOCKS.registerBlock(
                name,
                p -> new AmethystClusterBlock(height, aabbOffset, p),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_CYAN)
                        .noOcclusion()
                        .strength(1.5F)
                        .lightLevel(s -> light)
                        .sound(SoundType.AMETHYST_CLUSTER)
                        .pushReaction(PushReaction.DESTROY));
    }

    /** Ore-block factory (v1.3 Phase C): {@code deep} = the deepslate-tinted tougher variant; {@code light} 0 = none. */
    private static DeferredBlock<DropExperienceBlock> oreBlock(
            String name, int xpMin, int xpMax, boolean deep, int light) {
        return BLOCKS.registerBlock(
                name,
                props -> new DropExperienceBlock(UniformInt.of(xpMin, xpMax), props),
                BlockBehaviour.Properties.of()
                        .mapColor(deep ? MapColor.DEEPSLATE : MapColor.STONE)
                        .strength(deep ? 4.5F : 3.0F, 3.0F)
                        .requiresCorrectToolForDrops()
                        .lightLevel(s -> light)
                        .sound(deep ? SoundType.DEEPSLATE : SoundType.STONE));
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

    // Glowwood + Glowroot are glowing biome woods (the bible's "living light" selling point): their whole sets
    // are EMISSIVE-rendered (the block models inherit the `_emissive_*` parents, so they look luminous in any
    // light) AND emit real light — logs/wood 7, planks + plank-derived 5. (Glowroot uses the parallel
    // glowrootLogProps/glowrootPlanksProps; same values, the species differ by texture colour.)
    private static BlockBehaviour.Properties logProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .strength(2.0F)
                .lightLevel(state -> 7)
                .sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties planksProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .strength(2.0F, 3.0F)
                .lightLevel(state -> 5)
                .sound(SoundType.WOOD);
    }

    /** Glowing wood barrel — wood-strength container that emits light 7 (its model is emissive too). */
    private static BlockBehaviour.Properties barrelProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.5F)
                .lightLevel(state -> 7)
                .sound(SoundType.WOOD);
    }

    /** Torch behaviour (v1.4.7) — exactly like a vanilla torch: no collision, instant break, light 14. */
    private static BlockBehaviour.Properties torchProps() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .instabreak()
                .lightLevel(state -> 14)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY);
    }

    /** Wood ladder properties (v1.4.2) — thin, climbable, glowing like the wood sets. */
    private static BlockBehaviour.Properties ladderProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(0.4F)
                .lightLevel(state -> 5)
                .sound(SoundType.LADDER)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }

    /** Glowroot logs/wood — the self-lit species; emissive-rendered + emits light 7. */
    private static BlockBehaviour.Properties glowrootLogProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(2.0F)
                .lightLevel(state -> 7)
                .sound(SoundType.WOOD);
    }

    /** Glowroot planks + plank-derived shapes — emissive-rendered + emits light 5. */
    private static BlockBehaviour.Properties glowrootPlanksProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(2.0F, 3.0F)
                .lightLevel(state -> 5)
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
