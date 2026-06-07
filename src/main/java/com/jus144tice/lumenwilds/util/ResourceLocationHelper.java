/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.util;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.minecraft.resources.ResourceLocation;

/**
 * Small convenience helpers for building {@link ResourceLocation}s in the {@code lumenwilds}
 * namespace. In 1.21.1 the {@code ResourceLocation} constructor is private, so all construction goes
 * through {@link ResourceLocation#fromNamespaceAndPath(String, String)} / {@link ResourceLocation#parse(String)}.
 */
public final class ResourceLocationHelper {

    private ResourceLocationHelper() {}

    /** A {@link ResourceLocation} in the mod's namespace ({@code lumenwilds:<path>}). */
    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Lumenwilds.MOD_ID, path);
    }

    /** A {@link ResourceLocation} in the vanilla {@code minecraft} namespace. */
    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}
