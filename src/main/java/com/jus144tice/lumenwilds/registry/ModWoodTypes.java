/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * The bespoke Glowwood {@link WoodType} / {@link BlockSetType}.
 *
 * <p>These are NOT {@code DeferredRegister} content — {@code BlockSetType}/{@code WoodType} live in their
 * own static vanilla registries and must be registered <em>before</em> any block that references them is
 * constructed (i.e. before the block {@code RegisterEvent} fires). {@link #init()} is therefore called at
 * the very top of the {@link Lumenwilds} constructor. The {@code WoodType} name is namespaced
 * ({@code lumenwilds:glowwood}) so sign textures resolve under our namespace
 * ({@code lumenwilds:entity/signs/glowwood}).</p>
 *
 * <p>The single-arg constructors default to wood sounds (door/trapdoor/button/plate click + fence-gate +
 * hanging-sign), which suits Glowwood; bespoke sound events can be added later.</p>
 */
public final class ModWoodTypes {

    public static final BlockSetType GLOWWOOD_SET =
            BlockSetType.register(new BlockSetType(Lumenwilds.MOD_ID + ":glowwood"));

    public static final WoodType GLOWWOOD =
            WoodType.register(new WoodType(Lumenwilds.MOD_ID + ":glowwood", GLOWWOOD_SET));

    /** Glowroot — the signature self-lit species (its wood set glows faintly; see {@code ModBlocks}). */
    public static final BlockSetType GLOWROOT_SET =
            BlockSetType.register(new BlockSetType(Lumenwilds.MOD_ID + ":glowroot"));

    public static final WoodType GLOWROOT =
            WoodType.register(new WoodType(Lumenwilds.MOD_ID + ":glowroot", GLOWROOT_SET));

    private ModWoodTypes() {}

    /** Forces class-load so the static registrations above run. Call early in the mod constructor. */
    public static void init() {}
}
