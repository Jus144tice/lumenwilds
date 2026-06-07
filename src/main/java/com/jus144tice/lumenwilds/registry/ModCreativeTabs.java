/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The single creative tab for the mod, titled "The Lumenwilds". It auto-populates from every entry
 * in {@link ModItems#ITEMS}, so newly registered items/blocks show up without editing this class.
 */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Lumenwilds.MOD_ID);

    private ModCreativeTabs() {}

    public static final Supplier<CreativeModeTab> LUMENWILDS_TAB =
            CREATIVE_MODE_TABS.register("lumenwilds", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.lumenwilds"))
                    .icon(() -> new ItemStack(ModItems.LUMEN_STRIKER.get()))
                    .displayItems(
                            (params, output) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                    .build());
}
