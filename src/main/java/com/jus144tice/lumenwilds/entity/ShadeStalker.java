/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.entity.ai.FleeBrightLightGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Shade Stalker — the Lumenwilds' main hostile surface predator (a thin dark quadruped with glowing eyes).
 * A true ambush predator: in the dark, with no prey, it <b>holds still and lurks</b> (no idle wandering —
 * only its head tracks), then <b>lunges fast</b> when a player comes within range. It embodies the dimension's
 * core rule: it <b>flees bright light</b> (daylight, Stillbloom Cores, Lumen lanterns) via
 * {@link FleeBrightLightGoal} at top priority — darting fast to the nearest shade, so living light genuinely
 * wards it off even mid-chase. Net feel: dart to shade → wait → pounce.
 *
 * <p>Native to the dimension, so it carries low gravity permanently (reduced {@code GRAVITY} base).</p>
 */
public class ShadeStalker extends Monster {

    public ShadeStalker(EntityType<? extends ShadeStalker> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // The signature: flee bright light above everything else (FAST — it bolts to the nearest shade);
        // lanterns / cores / daylight ward it off, even mid-chase.
        this.goalSelector.addGoal(1, new FleeBrightLightGoal(this, 1.5, 11));
        // Stalk-and-ambush: once it spots prey it lunges fast. There is deliberately NO idle wander goal — in
        // the dark, with no prey, the Shade Stalker holds still and lurks (only its head tracks via the look
        // goals), then strikes. So it reads as: dart to shade → wait → pounce.
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /** Fast, hardy ambusher with native low gravity (0.056 ≈ 0.08 × 0.7). */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.GRAVITY, 0.056);
    }
}
