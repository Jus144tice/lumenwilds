/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A reusable flight goal: periodically scans a small box around the mob for a block matching a predicate
 * (e.g. Moonblossoms / Lumenbulbs / Glowvines) and flies to hover just above the nearest match. Drives the
 * bible's "flies around flowers and lights" behaviour for the Lantern Beetle (and later the Glowmoth). The
 * scan is throttled (cooldown + random gate) so it stays cheap with several mobs around.
 */
public class FlyToBlocksGoal extends Goal {

    private final PathfinderMob mob;
    private final Predicate<BlockState> target;
    private final int searchRange;
    private final double speedModifier;
    private BlockPos found;
    private int cooldown;

    public FlyToBlocksGoal(PathfinderMob mob, Predicate<BlockState> target, int searchRange, double speedModifier) {
        this.mob = mob;
        this.target = target;
        this.searchRange = searchRange;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        if (this.mob.getRandom().nextInt(40) != 0) {
            return false;
        }
        this.found = findTarget();
        return this.found != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.found != null
                && !this.mob.getNavigation().isDone()
                && this.target.test(this.mob.level().getBlockState(this.found));
    }

    @Override
    public void start() {
        this.mob
                .getNavigation()
                .moveTo(this.found.getX() + 0.5, this.found.getY() + 1.0, this.found.getZ() + 0.5, this.speedModifier);
    }

    @Override
    public void stop() {
        this.found = null;
        this.cooldown = 80 + this.mob.getRandom().nextInt(120);
    }

    private BlockPos findTarget() {
        BlockPos origin = this.mob.blockPosition();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -this.searchRange; dx <= this.searchRange; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -this.searchRange; dz <= this.searchRange; dz++) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (this.target.test(this.mob.level().getBlockState(cursor))) {
                        double dist = origin.distSqr(cursor);
                        if (dist < bestDist) {
                            bestDist = dist;
                            best = cursor.immutable();
                        }
                    }
                }
            }
        }
        return best;
    }
}
