/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;

/**
 * Luminite Umbrella — a light <b>stone-tier weapon</b> (a bludgeon; no better than a stone sword) that doubles
 * as a <b>rain shield</b>. While held in either hand, the wielder is not counted as being in rain
 * ({@code mixin.EntityMixin} cancels {@code Entity#isInRain}), so the dimension's Lumenwater rain — and any mod
 * that damages a race/class for being rained on — no longer wets them. It deliberately shields only the
 * <em>rain</em>, not water in general (standing in water still counts, via the Lumenwater-is-water path).
 */
public class LuminiteUmbrellaItem extends SwordItem {

    public LuminiteUmbrellaItem(Properties properties) {
        super(Tiers.STONE, properties.attributes(SwordItem.createAttributes(Tiers.STONE, 3.0F, -2.4F)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lumenwilds.luminite_umbrella.desc")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
