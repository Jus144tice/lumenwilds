/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModFluidTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;

/**
 * Registers the <b>Lumenwater ↔ lava</b> reaction (v1.6.0), the Lumenwilds analog of vanilla water+lava. Like
 * water, Lumenwater quenches lava — but into dimension-native stone: a lava <b>source</b> becomes
 * {@link ModBlocks#DUSKGLASS} (the "obsidian"), and <b>flowing</b> lava becomes {@link ModBlocks#COBBLED_MOONSTONE}
 * (the "cobblestone"). Registered on the vanilla {@code LAVA_TYPE} with Lumenwater as the neighbour, so it fires
 * whichever way the two meet (mirroring how vanilla drives obsidian/cobblestone off {@code LAVA_TYPE}).
 *
 * <p>Mostly a player-driven interaction — lava is rare inside the Lumenwilds, and Lumenwater reverts to plain
 * water outside it, so it's a Lumenwilds affair (bring lava in a bucket, à la an Overworld obsidian farm).</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class ModFluidInteractions {

    private ModFluidInteractions() {}

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        // The registry is a static synchronized map — register on the setup work queue.
        event.enqueueWork(() -> FluidInteractionRegistry.addInteraction(
                NeoForgeMod.LAVA_TYPE.value(),
                new FluidInteractionRegistry.InteractionInformation(
                        ModFluidTypes.LUMENWATER_TYPE.value(), fluidState -> (fluidState.isSource()
                                        ? ModBlocks.DUSKGLASS.get()
                                        : ModBlocks.COBBLED_MOONSTONE.get())
                                .defaultBlockState())));
    }
}
