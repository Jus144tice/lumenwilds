/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModWoodTypes;
import net.minecraft.client.renderer.Sheets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only setup ({@link Dist#CLIENT}). Registers the Glowwood
 * {@link net.minecraft.world.level.block.state.properties.WoodType} with {@link Sheets} so its sign /
 * hanging-sign materials are atlased and the vanilla sign renderers can draw Glowwood signs.
 *
 * <p>Boat + chest-boat model layers do NOT need registering here: vanilla {@code LayerDefinitions}
 * already iterates {@code Boat.Type.values()} (which now includes our enum-extended Glowwood type), and
 * the vanilla {@code BoatRenderer} draws it from {@code lumenwilds:entity/boat|chest_boat/glowwood}.
 * Registering them again caused a duplicate-layer crash.</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class LumenwildsClient {

    private LumenwildsClient() {}

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> Sheets.addWoodType(ModWoodTypes.GLOWWOOD));
    }
}
