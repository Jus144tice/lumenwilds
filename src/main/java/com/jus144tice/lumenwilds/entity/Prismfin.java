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
 * Prismfin (v1.4.2) — a vivid, multi-hued tropical schooling fish of the glowing water; the catchable
 * aquarium fish. Bucketable (scoop it with a water bucket → a Prismfin bucket; tip it into a Lumenwater tank
 * for an aquarium). Reuses the {@link LumenFish} groundwork (vanilla {@link AbstractSchoolingFish}); only the
 * texture and bucket differ.
 */
public class Prismfin extends AbstractSchoolingFish {

    public Prismfin(EntityType<? extends Prismfin> type, Level level) {
        super(type, level);
    }

    /** Native low gravity on top of the standard fish attributes. */
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes().add(Attributes.GRAVITY, 0.056);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.PRISMFIN_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource cause) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }
}
