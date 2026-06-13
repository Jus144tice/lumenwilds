/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * The Echo Sentinel's ranged attack (Phase 10f) — a charged <b>light pulse</b> rather than a melee swing or a
 * travelling projectile. When the sentinel has line-of-sight to its target in range it briefly <b>charges</b>
 * (a gathering glow at its eye), then <b>fires</b> an instant beam (a line of glow particles) that damages and
 * lightly knocks back the target, then pauses on cooldown — the "charge → glow → pulse → flicker" cadence the
 * bible describes, implemented hitscan-style so it needs no projectile entity or renderer.
 */
public class LightPulseAttackGoal extends Goal {

    private final Mob mob;
    private final float damage;
    private final double range;
    private final int chargeTime;
    private final int cooldownTime;

    private int charge;
    private int cooldown;

    public LightPulseAttackGoal(Mob mob, float damage, double range, int chargeTime, int cooldownTime) {
        this.mob = mob;
        this.damage = damage;
        this.range = range;
        this.chargeTime = chargeTime;
        this.cooldownTime = cooldownTime;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.charge = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (this.cooldown > 0) {
            this.cooldown--;
            return;
        }

        boolean inRange = this.mob.distanceToSqr(target) <= this.range * this.range;
        if (inRange && this.mob.getSensing().hasLineOfSight(target)) {
            this.charge++;
            chargeParticles();
            if (this.charge >= this.chargeTime) {
                fire(target);
                this.charge = 0;
                this.cooldown = this.cooldownTime;
            }
        } else {
            this.charge = 0;
        }
    }

    private void chargeParticles() {
        if (this.mob.level() instanceof ServerLevel sl) {
            Vec3 eye = this.mob.getEyePosition();
            sl.sendParticles(ParticleTypes.GLOW, eye.x, eye.y, eye.z, 2, 0.15, 0.15, 0.15, 0.0);
        }
    }

    private void fire(LivingEntity target) {
        if (!(this.mob.level() instanceof ServerLevel sl)) {
            return;
        }
        Vec3 from = this.mob.getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 step = to.subtract(from);
        int points = (int) Math.max(4, step.length() * 2);
        for (int i = 0; i <= points; i++) {
            double t = (double) i / points;
            Vec3 p = from.add(step.scale(t));
            sl.sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        target.hurt(this.mob.damageSources().indirectMagic(this.mob, this.mob), this.damage);
        Vec3 push = to.subtract(from).normalize().scale(0.4);
        target.push(push.x, 0.1, push.z);
        sl.playSound(null, this.mob.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0F, 1.4F);
    }
}
