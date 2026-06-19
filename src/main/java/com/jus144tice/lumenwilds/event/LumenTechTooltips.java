/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.event;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * Adds a short "how to use" hover description to the Lumenwright resonance / gravity tech blocks, whose
 * behaviour isn't obvious from the name (power networks, restore-with-a-fragment, gravity columns, lore
 * crystals, …). One central {@link ItemTooltipEvent} handler covers them all — including the plain {@code
 * Block}s (Memory Crystal, Ancient Door, Cracked Gravity Lens) that have no custom class — instead of an
 * {@code appendHoverText} override per block. Style matches the Lumen Field Projector's own tooltip
 * (aqua italic); the projector keeps its bespoke (mode-aware) line and isn't duplicated here.
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID)
public final class LumenTechTooltips {

    private LumenTechTooltips() {}

    /** Lazily built (items must be registered first); item identity → description line. */
    private static Map<Item, String> descriptions;

    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        if (descriptions == null) {
            descriptions = build();
        }
        String desc = descriptions.get(event.getItemStack().getItem());
        if (desc != null) {
            event.getToolTip().add(Component.literal(desc).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        }
    }

    private static Map<Item, String> build() {
        Map<Item, String> m = new IdentityHashMap<>();
        // Resonance network.
        put(
                m,
                ModBlocks.RESONANCE_CORE,
                "Floods power through connected Lumen Conduits and opens adjacent Ancient Doors.");
        put(
                m,
                ModBlocks.LUMEN_CONDUIT,
                "Carries Resonance power — lights up when fed by a Resonance Core or Light Engine.");
        put(m, ModBlocks.ANCIENT_DOOR, "Opens only while powered by an adjacent active conduit or core — not by hand.");
        put(m, ModBlocks.LUMEN_RELAY, "Bridges a Resonance network across a gap between conduits.");
        put(
                m,
                ModBlocks.DORMANT_LIGHT_ENGINE,
                "Right-click with a Resonance Core Fragment to restore it and power a dead city's network.");
        put(
                m,
                ModBlocks.ACTIVE_LIGHT_ENGINE,
                "A restored city heart — powers the Resonance network like a Resonance Core.");
        // Gravity tech.
        put(
                m,
                ModBlocks.GRAVITY_LENS,
                "While powered by the Resonance network, gently lifts entities in the column above it.");
        put(m, ModBlocks.CRACKED_GRAVITY_LENS, "A broken Gravity Lens — inert. Mine it for a Gravity Lens Fragment.");
        put(
                m,
                ModBlocks.GRAVITY_REPEATER,
                "Mount flush in a liftshaft wall to extend a Lumen Field Projector's range, so the gravity column climbs higher.");
        // Lore + portal link.
        put(
                m,
                ModBlocks.MEMORY_CRYSTAL,
                "Right-click to read a fragment of Lumenwright lore. Drops a Memory Crystal Shard when mined.");
        put(
                m,
                ModBlocks.LUMEN_ANCHOR,
                "Pair two anchors with the Lumen Striker; a linked anchor by a portal lands return travel at its partner.");
        return m;
    }

    private static void put(
            Map<Item, String> m, net.neoforged.neoforge.registries.DeferredBlock<?> block, String desc) {
        ItemLike item = block.get();
        if (item.asItem() != net.minecraft.world.item.Items.AIR) {
            m.put(item.asItem(), desc);
        }
    }
}
