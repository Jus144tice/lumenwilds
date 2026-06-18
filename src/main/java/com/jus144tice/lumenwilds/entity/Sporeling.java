/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.registry.ModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Sporeling — a small <b>neutral</b> fungal swarm critter of the Sporefall Jungle and Undercrown Caverns.
 * Cute and harmless until provoked: it never aggros on sight, but attacking one makes it fight back and
 * alerts the nearby swarm (the "hurt one, anger the group" identity). On death it bursts into a <b>spore
 * cloud</b> that briefly clouds vision (Darkness) and mildly slows whoever is caught in it — "Sporeblind".
 *
 * <p>The death cloud is an {@link AreaEffectCloud} (the reusable pattern for later cloud-on-death mobs). A
 * dedicated, bespoke <em>Sporeblind</em> effect with its own screen overlay is a Phase 8 task; here it
 * stands in with vanilla Darkness + Slowness.</p>
 */
public class Sporeling extends Monster {

    public Sporeling(EntityType<? extends Sporeling> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        // Neutral by default — cute fungal wanderers that DON'T aggro on sight. They only fight back when
        // attacked, and hurting one still alerts the swarm (the "hurt one, anger the group" identity).
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
    }

    /** Weak, quick swarm member with native low gravity. */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.level().isClientSide) {
            AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(0.5), this.getZ());
            cloud.setRadius(2.5F);
            cloud.setDuration(80);
            cloud.setRadiusOnUse(-0.25F);
            cloud.setWaitTime(0);
            cloud.setParticle(ParticleTypes.SPORE_BLOSSOM_AIR);
            cloud.addEffect(new MobEffectInstance(ModMobEffects.SPOREBLIND, 120, 0)); // the real "Sporeblind" (8a)
            cloud.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
            this.level().addFreshEntity(cloud);
        }
        super.die(cause);
    }
}
