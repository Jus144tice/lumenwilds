/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Container/menu types added by The Lumenwilds. Empty for Phase 1.
 *
 * <p>TODO: if the mod adds machines/containers (e.g. a crystal infuser), register their
 * {@code MenuType}s here and their screens client-side via {@code RegisterMenuScreensEvent}.</p>
 */
public final class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Lumenwilds.MOD_ID);

    private ModMenus() {}
}
