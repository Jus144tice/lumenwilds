/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.registry;

import com.jus144tice.lumenwilds.Lumenwilds;
import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import java.util.EnumMap;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Armor material(s) for the Lumenwilds gear sets (v1.3 Phase D2 — the dimension's first armor).
 *
 * <p>{@link #RESONITE} pairs with the diamond-capable {@link ModToolTiers#RESONITE} tool tier as the deep's
 * top reward: defense between iron and diamond, toughness 1.5, high enchantability, repaired with Resonite
 * Ingot. The worn-armor textures are {@code textures/models/armor/resonite_layer_{1,2}.png} (resolved from the
 * {@link ArmorMaterial.Layer}'s {@code lumenwilds:resonite} asset name); the inventory icons are normal item
 * textures. The {@link ArmorItem}s themselves live in {@link ModItems}.</p>
 *
 * <p>This is a real {@code Registries.ARMOR_MATERIAL} DeferredRegister, so it is wired on the mod bus in the
 * {@link Lumenwilds} constructor (Mandate #1).</p>
 */
public final class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Lumenwilds.MOD_ID);

    /** Luminite armor — iron-tier (matches the {@link ModToolTiers#LUMINITE} iron-equivalent tools): vanilla
     * iron defense/toughness/enchantability, repaired with Luminite Ingot. */
    public static final Holder<ArmorMaterial> LUMINITE = ARMOR_MATERIALS.register(
            "luminite",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                        m.put(ArmorItem.Type.BOOTS, 2);
                        m.put(ArmorItem.Type.LEGGINGS, 5);
                        m.put(ArmorItem.Type.CHESTPLATE, 6);
                        m.put(ArmorItem.Type.HELMET, 2);
                        m.put(ArmorItem.Type.BODY, 5);
                    }),
                    9, // enchantmentValue (iron)
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(ModItems.LUMINITE_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocationHelper.modLoc("luminite"))),
                    0.0F, // toughness (iron)
                    0.0F)); // knockbackResistance

    /** Resonite armor — between iron and diamond defense, toughness 1.5; the endgame Lumenwilds set. */
    public static final Holder<ArmorMaterial> RESONITE = ARMOR_MATERIALS.register(
            "resonite",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                        m.put(ArmorItem.Type.BOOTS, 3);
                        m.put(ArmorItem.Type.LEGGINGS, 6);
                        m.put(ArmorItem.Type.CHESTPLATE, 7);
                        m.put(ArmorItem.Type.HELMET, 3);
                        m.put(ArmorItem.Type.BODY, 11);
                    }),
                    18, // enchantmentValue
                    SoundEvents.ARMOR_EQUIP_DIAMOND,
                    () -> Ingredient.of(ModItems.RESONITE_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocationHelper.modLoc("resonite"))),
                    1.5F, // toughness
                    0.0F)); // knockbackResistance

    private ModArmorMaterials() {}
}
