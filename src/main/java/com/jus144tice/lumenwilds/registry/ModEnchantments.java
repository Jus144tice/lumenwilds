/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * {@link ResourceKey} handles for the Lumenwilds' custom enchantments (v1.1g — the fished "spell books").
 *
 * <p>The enchantments themselves are <b>pure data</b> ({@code data/lumenwilds/enchantment/*.json}, 1.21.1's
 * data-driven enchantment system) — these keys exist only so code/loot/tags can reference them by a typed
 * handle. They are deliberately kept OUT of {@code #minecraft:in_enchanting_table} / {@code tradeable} /
 * {@code on_random_loot}, so the only way to obtain them is as enchanted books fished from Lumenwater
 * (the {@code gameplay/fishing/lumenwater} loot table), applied to gear at an anvil.</p>
 *
 * <ul>
 *   <li><b>Armor "while worn"</b> (a {@code minecraft:tick} → {@code apply_mob_effect} refresh):
 *       {@link #LIGHTFOOTED} (boots → Lightfoot), {@link #NIGHTSIGHT} (helmet → Night Vision),
 *       {@link #LUMENWARD} (chest → Water Breathing + Dolphin's Grace, the Lumenwater affinity).</li>
 *   <li><b>Weapon "on hit"</b> (a {@code minecraft:post_attack} → {@code apply_mob_effect} on the victim):
 *       {@link #GLOWBRAND} (Glowmarked), {@link #SPORESTRIKE} (Sporeblind), {@link #ROOTBINDING} (Rooted).</li>
 * </ul>
 */
public final class ModEnchantments {

    public static final ResourceKey<Enchantment> LIGHTFOOTED = key("lightfooted");
    public static final ResourceKey<Enchantment> NIGHTSIGHT = key("nightsight");
    public static final ResourceKey<Enchantment> LUMENWARD = key("lumenward");
    public static final ResourceKey<Enchantment> GLOWBRAND = key("glowbrand");
    public static final ResourceKey<Enchantment> SPORESTRIKE = key("sporestrike");
    public static final ResourceKey<Enchantment> ROOTBINDING = key("rootbinding");

    private ModEnchantments() {}

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocationHelper.modLoc(name));
    }
}
