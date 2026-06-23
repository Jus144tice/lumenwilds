/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Lumen Silkworm (v1.4.4) — the dimension's small, glowing larva and its only fibre source: the Lumenwilds has
 * no wool-bearing animal, so this slow passive bug drops <b>Lumensilk</b> (its loot table), which crafts into
 * white wool (and thus beds). Breeds with Glow Fern (renewable, so the silk supply is renewable), grazes the
 * leafy biomes, and carries the native low gravity. Built on the {@link LumenGrazer} passive-breeder template.
 */
public class LumenSilkworm extends Animal {

    public LumenSilkworm(EntityType<? extends LumenSilkworm> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.15, LumenSilkworm::isSilkwormFood, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /** Breeding / tempt food: Glow Fern — common, renewable leafy flora, so silk stays renewable. */
    public static boolean isSilkwormFood(ItemStack stack) {
        return stack.is(ModBlocks.GLOW_FERN.asItem());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isSilkwormFood(stack);
    }

    /** Small + fragile, native low gravity (0.056) baked in. */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.16)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Override
    public LumenSilkworm getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.LUMEN_SILKWORM.get().create(level);
    }
}
