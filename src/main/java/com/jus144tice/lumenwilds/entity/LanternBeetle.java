/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.entity.ai.FlyToBlocksGoal;
import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Lantern Beetle — a small glowing flying insect; the dimension's "make it feel alive" ambience mob. It
 * flies around flowers, Glowvines and Lumenbulbs (via {@link FlyToBlocksGoal}), wanders the air otherwise,
 * and can be **caught in a glass bottle** → a Bottled Lantern Beetle. Non-breeding.
 *
 * <p>This is the Lumenwilds' first <b>flying</b> mob — it establishes the reusable flight setup
 * ({@link FlyingMoveControl} + {@link FlyingPathNavigation} + {@code FLYING_SPEED}) that the Sky Jelly,
 * Glowmoth and Crag Wraith will follow. The moving light it should emit is deferred (needs dynamic entity
 * lighting; Phase 9).</p>
 */
public class LanternBeetle extends Animal {

    public LanternBeetle(EntityType<? extends LanternBeetle> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(
                2,
                new FlyToBlocksGoal(
                        this,
                        state -> state.is(ModBlocks.MOONBLOSSOM.get())
                                || state.is(ModBlocks.LUMENBULB.get())
                                || state.is(ModBlocks.GLOWVINE.get()),
                        6,
                        1.0));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    /** Tiny, fast flyer with native low gravity. Not breedable. */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null; // does not breed
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.GLASS_BOTTLE)) {
            if (!this.level().isClientSide) {
                held.shrink(1);
                ItemStack bottled = new ItemStack(ModItems.BOTTLED_LANTERN_BEETLE.get());
                if (held.isEmpty()) {
                    player.setItemInHand(hand, bottled);
                } else if (!player.getInventory().add(bottled)) {
                    player.drop(bottled, false);
                }
                this.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
                this.discard();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }
}
