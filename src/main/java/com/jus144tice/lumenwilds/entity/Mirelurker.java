/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

/**
 * Mirelurker — the Moonmire's amphibious ambush predator. It lurks in the shallow Lumenwater (it doesn't
 * drown and treats water as freely walkable), then lunges at players who come close, and is <b>faster /
 * more aggressive at night</b> (a transient speed boost). The first amphibious mob — it establishes the
 * water-capable navigation the Lumen Fish (6f) reuses.
 *
 * <p>The glowing lure that mimics a harmless plant is a render/behaviour refinement (Phase 9).</p>
 */
public class Mirelurker extends Monster {

    private static final ResourceLocation NIGHT_SPEED_ID = ResourceLocationHelper.modLoc("mirelurker_night_speed");
    private static final AttributeModifier NIGHT_SPEED =
            new AttributeModifier(NIGHT_SPEED_ID, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public Mirelurker(EntityType<? extends Mirelurker> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F); // water is freely walkable
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    // Breathing underwater is driven by the #minecraft:can_breathe_under_water entity-type tag (the
    // canBreatheUnderwater() method is final), added in data/minecraft/tags/entity_type/.

    @Override
    public boolean isPushedByFluid(net.neoforged.neoforge.fluids.FluidType type) {
        return false; // stays put in the water, lurking (FluidType-sensitive — the no-arg form is deprecated)
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8)); // wanders into water (not water-avoiding)
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /** Hardy ambusher with native low gravity; speed is boosted at night in {@link #customServerAiStep}. */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.26)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 18.0)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }
        boolean night = !this.level().isDay();
        boolean boosted = speed.getModifier(NIGHT_SPEED_ID) != null;
        if (night && !boosted) {
            speed.addTransientModifier(NIGHT_SPEED);
        } else if (!night && boosted) {
            speed.removeModifier(NIGHT_SPEED_ID);
        }
    }
}
