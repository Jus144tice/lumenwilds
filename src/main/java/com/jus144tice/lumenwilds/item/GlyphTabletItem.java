/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Ancient Glyph Tablet (Phase 10c) — a collectible lore item from the Vestige Cities. Right-click to read its
 * short translated fragment of the Lumenwrights' story; the same fragment shows as an italic tooltip. Each of
 * the six tablets ({@code glyph_tablet_sky/roots/light/fall/silence/return}) carries its own line, passed in at
 * registration ({@code ModItems}).
 */
public class GlyphTabletItem extends Item {

    private final Component lore;

    public GlyphTabletItem(Properties properties, String loreText) {
        super(properties);
        this.lore = Component.literal(loreText).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            player.displayClientMessage(this.lore, false);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(this.lore);
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
