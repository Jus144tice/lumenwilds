/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Sky Jelly — a floating jellyfish-like air creature; pure sky ambience and a low-gravity material source
 * (it drops Air Gel). Harmless: it just drifts slowly through the air, horizontally and vertically. With a
 * near-zero {@code GRAVITY} attribute and the flying move control it effectively hovers, reinforcing the
 * dimension's low-gravity feel. Non-breeding. Reuses the flight setup established by the Lantern Beetle (6c).
 */
public class SkyJelly extends Animal {

    public SkyJelly(EntityType<? extends SkyJelly> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
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
        this.goalSelector.addGoal(0, new WaterAvoidingRandomFlyingGoal(this, 1.0)); // drift around
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    /** Hovers + drifts: near-zero gravity, gentle flight, lifted off the ground by buoyancy in {@link #aiStep}. */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.GRAVITY, 0.01);
    }

    /** A floating jelly takes no fall damage (like vanilla bees) — it drifts, it doesn't crash. */
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public void aiStep() {
        super.aiStep();
        // Buoyancy: drift upward while the ground is close, so the jelly floats a few blocks aloft and roams
        // there instead of resting on the surface.
        if (!this.level().isClientSide() && groundWithin(5)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.05, 0.0));
        }
    }

    private boolean groundWithin(int blocks) {
        BlockPos pos = this.blockPosition();
        for (int i = 1; i <= blocks; i++) {
            if (!this.level().getBlockState(pos.below(i)).isAir()) {
                return true;
            }
        }
        return false;
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
