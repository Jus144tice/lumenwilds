/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.entity.ai.FlyToBlocksGoal;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Glowmoth — a large luminous moth and a flower guardian. It is <b>neutral</b>: by default it just circles
 * bright flowers and Lumen lights ({@link FlyToBlocksGoal}). But if a player breaks a nearby Moonblossom or
 * Stillbloom it <b>turns hostile</b> — the {@code event.GlowmothAggroEvents} block-break handler sets it on
 * the culprit, and its {@link MeleeAttackGoal} (idle until it has a target) drives the attack. Flying mob,
 * non-breeding; reuses the flight setup from the Lantern Beetle (6c).
 */
public class Glowmoth extends Animal {

    public Glowmoth(EntityType<? extends Glowmoth> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    /** Blocks the moth is drawn to circle (bright flowers + strong Lumen light). */
    public static boolean isAttractor(BlockState state) {
        return state.is(ModBlocks.MOONBLOSSOM.get())
                || state.is(ModBlocks.STILLBLOOM_CORE.get())
                || state.is(ModBlocks.LUMENBULB.get())
                || state.is(ModBlocks.BOTTLED_LANTERN_BEETLE.get());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, true)); // idle until provoked (has a target)
        this.goalSelector.addGoal(2, new FlyToBlocksGoal(this, Glowmoth::isAttractor, 8, 1.0));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 0.7));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));

        // Neutral: only fights back when hurt; the flower-break aggro is set externally (GlowmothAggroEvents).
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    /** A nimble flutterer with native low gravity and a light sting. */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.GRAVITY, 0.03);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null; // does not breed
    }
}
