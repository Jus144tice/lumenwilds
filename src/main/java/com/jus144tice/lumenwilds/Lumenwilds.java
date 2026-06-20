/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds;

import com.jus144tice.lumenwilds.config.LumenConfig;
import com.jus144tice.lumenwilds.registry.ModArmorMaterials;
import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModCreativeTabs;
import com.jus144tice.lumenwilds.registry.ModEntities;
import com.jus144tice.lumenwilds.registry.ModFeatures;
import com.jus144tice.lumenwilds.registry.ModFluidTypes;
import com.jus144tice.lumenwilds.registry.ModFluids;
import com.jus144tice.lumenwilds.registry.ModItems;
import com.jus144tice.lumenwilds.registry.ModLootModifiers;
import com.jus144tice.lumenwilds.registry.ModMenus;
import com.jus144tice.lumenwilds.registry.ModMobEffects;
import com.jus144tice.lumenwilds.registry.ModParticles;
import com.jus144tice.lumenwilds.registry.ModPotions;
import com.jus144tice.lumenwilds.registry.ModSounds;
import com.jus144tice.lumenwilds.registry.ModStructures;
import com.jus144tice.lumenwilds.registry.ModWoodTypes;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * The Lumenwilds — mod entry point.
 *
 * <p>The Lumenwilds is an alien, bioluminescent custom dimension reached through a portal whose
 * frame is built from {@code lumenwilds:lumenbound_stone} (NOT vanilla lodestone) and lit with the
 * {@code lumenwilds:lumen_striker}. This class is intentionally thin: it wires every
 * {@code DeferredRegister} to the mod event bus and logs lifecycle milestones. Content registration
 * lives in the {@code registry} package; behaviour lives in {@code portal}, {@code item},
 * {@code world}, {@code effects} and {@code event}.</p>
 *
 * <p>Feature-complete as of 1.0 — the portal, dimension, worldgen/biomes, mobs, structures (incl. the
 * Lumenwright Vestige Cities + liftshaft mines) and full portal behaviour are all in. See
 * {@code docs/IMPLEMENTATION_PLAN.md} and {@code CHANGELOG.md} for the roadmap/history.</p>
 */
@Mod(Lumenwilds.MOD_ID)
public final class Lumenwilds {
    /** Mod id — used as the namespace for every {@link net.minecraft.resources.ResourceLocation}. */
    public static final String MOD_ID = "lumenwilds";

    /** Human-readable display name. */
    public static final String MOD_NAME = "The Lumenwilds";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Lumenwilds(IEventBus modBus, ModContainer container) {
        LOGGER.info(
                "[{}] Initialising {} v{}.",
                MOD_ID,
                MOD_NAME,
                container.getModInfo().getVersion());

        // Register the Glowwood WoodType/BlockSetType BEFORE blocks that reference them are built.
        ModWoodTypes.init();

        // --- Register every DeferredRegister to the mod event bus. Order is not significant. ---
        ModSounds.SOUNDS.register(modBus);
        ModParticles.PARTICLES.register(modBus);
        ModMobEffects.MOB_EFFECTS.register(modBus);
        ModPotions.POTIONS.register(modBus);
        ModFluidTypes.FLUID_TYPES.register(modBus);
        ModFluids.FLUIDS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modBus);
        ModFeatures.FEATURES.register(modBus);
        ModStructures.STRUCTURE_TYPES.register(modBus);
        ModStructures.STRUCTURE_PIECES.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModMenus.MENUS.register(modBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modBus);
        ModLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modBus);

        // Worldgen registries (configured/placed features, biomes, dimensions) are data-driven for
        // now (see src/main/resources/data/lumenwilds/...). The Java-side resource keys live in the
        // world package and are registered via DataPack JSON, not a DeferredRegister.

        modBus.addListener(this::onCommonSetup);

        // Gameplay config (Phase 9h): gravity strength, ambient events, day-cycle rate.
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, LumenConfig.SPEC);

        LOGGER.info("[{}] DeferredRegisters wired to the mod event bus.", MOD_ID);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> LOGGER.info("[{}] Common setup complete.", MOD_ID));
    }
}
