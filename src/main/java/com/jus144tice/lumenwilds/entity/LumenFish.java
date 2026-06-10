/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Lumen Fish — the bible's "fish-like native mobs" that live in Lumenwater (the Moonmire pools and other
 * glowing water). A small passive schooling swimmer; it is <b>bucketable</b> (catch it with a water bucket →
 * a Lumen Fish bucket) and is the in-world source of edible Mirefish (its death drop). Built on the vanilla
 * {@link AbstractSchoolingFish} (schooling + swim navigation + water survival come for free), reusing the
 * water-mob groundwork from the Mirelurker (6e).
 */
public class LumenFish extends AbstractSchoolingFish {

    public LumenFish(EntityType<? extends LumenFish> type, Level level) {
        super(type, level);
    }

    /** Native low gravity on top of the standard fish attributes. */
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes().add(Attributes.GRAVITY, 0.056);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.LUMEN_FISH_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource cause) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }
}
