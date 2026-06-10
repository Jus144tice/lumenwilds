/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Rootback — a large, slow, peaceful turtle-like "living feature": glowing shrubs grow on its shell and it
 * gently seeds flora as it wanders, almost a roaming natural decoration. Neutral — it only fights back when
 * attacked (it's a tanky retaliator). Non-breeding.
 *
 * <p>The shell-plants are a render layer (Phase 9); here the "plants grow near where it rests" rule is
 * modelled by {@link #customServerAiStep} occasionally seeding a Glow Fern / Moonblossom on nearby soil.</p>
 */
public class Rootback extends Animal {

    public Rootback(EntityType<? extends Rootback> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true)); // idle until provoked
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6)); // slow wander
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this)); // neutral: only retaliates
    }

    /** A MASSIVE, very slow, hardy tank — huge health, near-immovable, native low gravity. */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.STEP_HEIGHT, 1.5)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        // "Plants grow near where it rests": rarely seed a Glow Fern / Moonblossom on nearby Lumenwilds soil.
        if (this.getRandom().nextInt(600) != 0) {
            return;
        }
        BlockPos base = this.blockPosition();
        BlockPos pos =
                base.offset(this.getRandom().nextInt(5) - 2, 0, this.getRandom().nextInt(5) - 2);
        BlockState ground = this.level().getBlockState(pos.below());
        if ((ground.is(ModBlocks.LUMEN_GRASS_BLOCK.get()) || ground.is(ModBlocks.MOONLOAM.get()))
                && this.level().getBlockState(pos).isAir()) {
            BlockState plant = this.getRandom().nextBoolean()
                    ? ModBlocks.GLOW_FERN.get().defaultBlockState()
                    : ModBlocks.MOONBLOSSOM.get().defaultBlockState();
            if (plant.canSurvive(this.level(), pos)) {
                this.level().setBlock(pos, plant, 3);
            }
        }
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
