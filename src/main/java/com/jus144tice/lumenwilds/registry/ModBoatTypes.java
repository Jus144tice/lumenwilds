/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import java.util.function.Supplier;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

/**
 * The Glowwood {@link Boat.Type}, added to the vanilla enum via NeoForge's enum extension. The
 * {@link EnumProxy} holds the constructor arguments; the actual enum value is created during mod load
 * (driven by {@code META-INF/enumextensions.json}) and retrieved with {@link #glowwood()}.
 *
 * <p>This reuses the vanilla {@link Boat}/{@code ChestBoat} entities and renderer — no custom entity is
 * needed. The constructor used is the modded-boat one taking lazy suppliers (planks for fall drops, the
 * boat/chest-boat items for pickup, the stick for break drops) plus the {@code raft} flag.</p>
 */
public final class ModBoatTypes {

    public static final EnumProxy<Boat.Type> GLOWWOOD_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class,
            (Supplier<Block>) () -> ModBlocks.GLOWWOOD_PLANKS.get(),
            Lumenwilds.MOD_ID + ":glowwood",
            (Supplier<Item>) () -> ModItems.GLOWWOOD_BOAT.get(),
            (Supplier<Item>) () -> ModItems.GLOWWOOD_CHEST_BOAT.get(),
            (Supplier<Item>) () -> Items.STICK,
            false);

    public static final EnumProxy<Boat.Type> GLOWROOT_BOAT_TYPE = new EnumProxy<>(
            Boat.Type.class,
            (Supplier<Block>) () -> ModBlocks.GLOWROOT_PLANKS.get(),
            Lumenwilds.MOD_ID + ":glowroot",
            (Supplier<Item>) () -> ModItems.GLOWROOT_BOAT.get(),
            (Supplier<Item>) () -> ModItems.GLOWROOT_CHEST_BOAT.get(),
            (Supplier<Item>) () -> Items.STICK,
            false);

    private ModBoatTypes() {}

    /** The created Glowwood boat type (valid once enum extension has run during mod load). */
    public static Boat.Type glowwood() {
        return GLOWWOOD_BOAT_TYPE.getValue();
    }

    /** The created Glowroot boat type (valid once enum extension has run during mod load). */
    public static Boat.Type glowroot() {
        return GLOWROOT_BOAT_TYPE.getValue();
    }
}
