/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.registry.ModEntities;
import com.jus144tice.lumenwilds.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Lumen Grazer — the Lumenwilds' peaceful herd herbivore (the alien deer/cow equivalent; six-legged in the
 * final art). Grazes the Lumen Glade / Glowroot Forest / Stillbloom Basin in small herds, is skittish
 * (flees players unless lured), and breeds with Lumen Fruit. Native to the dimension, so it carries the
 * low-gravity feel <em>permanently</em> via a reduced {@code GRAVITY} base attribute (vs. the player's
 * transient on-entry modifier from Phase 3).
 *
 * <p>Deferred (Phase 9 art/polish): the six-legged model (placeholder reuses the vanilla cow model), the
 * faint night glow (needs an emissive render layer), and the grass-grazing block-eat behaviour.</p>
 */
public class LumenGrazer extends Animal {

    public LumenGrazer(EntityType<? extends LumenGrazer> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, LumenGrazer::isGrazerFood, false));
        // Skittish: flees nearby players (the bible's "flees from players"), but the Lumen-Fruit tempt above
        // out-prioritises this, so it can still be lured and bred.
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0, 1.3));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    /**
     * Grazer food / breeding item: a Lumenberry (renewable — plant + harvest bushes) or a Lumen Fruit (rarer,
     * from chests/Stillblooms). Lumenberry is the practical breeding food now that the bush is farmable (v1.1.1).
     */
    public static boolean isGrazerFood(ItemStack stack) {
        return stack.is(ModItems.LUMENBERRY.get()) || stack.is(ModItems.LUMEN_FRUIT.get());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isGrazerFood(stack);
    }

    /** Native low gravity (0.056 ≈ 0.08 × 0.7) baked into the supplier; otherwise a hardy grazer. */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Override
    public LumenGrazer getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.LUMEN_GRAZER.get().create(level);
    }
}
