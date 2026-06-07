/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.client;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBoatTypes;
import com.jus144tice.lumenwilds.registry.ModWoodTypes;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only setup ({@link Dist#CLIENT}).
 *
 * <ul>
 *   <li>Registers the Glowwood {@link net.minecraft.world.level.block.state.properties.WoodType} with
 *       {@link Sheets} so its sign / hanging-sign materials are atlased and the vanilla sign renderers
 *       can draw Glowwood signs.</li>
 *   <li>Registers boat + chest-boat model layers for the Glowwood {@link Boat.Type}; the vanilla
 *       {@code BoatRenderer} (which iterates {@code Boat.Type.values()}) then draws them, using the
 *       textures at {@code lumenwilds:entity/boat/glowwood} and {@code .../chest_boat/glowwood}.</li>
 * </ul>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class LumenwildsClient {

    private LumenwildsClient() {}

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> Sheets.addWoodType(ModWoodTypes.GLOWWOOD));
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        Boat.Type type = ModBoatTypes.glowwood();
        event.registerLayerDefinition(ModelLayers.createBoatModelName(type), BoatModel::createBodyModel);
        event.registerLayerDefinition(ModelLayers.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
    }
}
