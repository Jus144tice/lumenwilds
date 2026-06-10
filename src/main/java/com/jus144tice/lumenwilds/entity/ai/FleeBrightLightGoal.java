/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * The Lumenwilds' core "living light keeps danger away" behaviour, reusable across light-shy mobs (Shade
 * Stalker first; later Sporeling, etc.). When the mob stands in bright light — daylight on the surface OR
 * the block light of a Stillbloom Core / Lumenbulb / lantern — it breaks off and flees toward darkness,
 * even mid-chase. So a lit base or a held lantern genuinely wards it off.
 *
 * <p>Brightness uses {@link net.minecraft.world.level.LevelReader#getMaxLocalRawBrightness(BlockPos)} (block
 * light combined with day-adjusted skylight), so it naturally covers both natural and Lumen light and goes
 * dormant in the dark.</p>
 */
public class FleeBrightLightGoal extends Goal {

    private final PathfinderMob mob;
    private final double speedModifier;
    private final int lightThreshold;
    private double wantedX;
    private double wantedY;
    private double wantedZ;

    public FleeBrightLightGoal(PathfinderMob mob, double speedModifier, int lightThreshold) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.lightThreshold = lightThreshold;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return isBright(this.mob.blockPosition()) && findDarkerPos();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    private boolean isBright(BlockPos pos) {
        return this.mob.level().getMaxLocalRawBrightness(pos) >= this.lightThreshold;
    }

    /** Samples nearby positions and keeps the first dark, lower one to bolt to. */
    private boolean findDarkerPos() {
        RandomSource random = this.mob.getRandom();
        BlockPos base = this.mob.blockPosition();
        for (int i = 0; i < 10; i++) {
            BlockPos candidate = base.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!isBright(candidate)) {
                Vec3 target = Vec3.atBottomCenterOf(candidate);
                this.wantedX = target.x;
                this.wantedY = target.y;
                this.wantedZ = target.z;
                return true;
            }
        }
        return false;
    }
}
