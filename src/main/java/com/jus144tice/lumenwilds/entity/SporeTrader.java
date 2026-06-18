/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.entity;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

/**
 * Sporeman — a rare "fully grown Sporeling" wandering merchant of the Sporefall Jungle (v1.2). He's
 * <b>neutral</b>: he wanders, lets you trade, and only turns hostile (fighting back hard) if you strike him.
 * He deals exclusively in Lumenwilds goods, sold for <b>Overworld valuables</b> — mostly emeralds, with a few
 * premium gold/diamond trades — so the dimension's wealth flows back into your pocket. A grown {@link Sporeling}
 * in spirit: it reuses the Sporeling model (scaled up) on the client.
 */
public class SporeTrader extends AbstractVillager {

    public SporeTrader(EntityType<? extends SporeTrader> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        // Neutral but no pushover: when provoked he retaliates instead of fleeing.
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    /** Sturdy, calm, native low gravity — and the ATTACK_DAMAGE he needs to defend himself if attacked. */
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractVillager.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.GRAVITY, 0.056);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null; // doesn't breed — spawns wild in the jungle
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        boolean spawnEgg = ModItems.SPORE_TRADER_SPAWN_EGG.get() == held.getItem();
        if (!spawnEgg && this.isAlive() && !this.isTrading() && !this.isBaby()) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (!this.level().isClientSide) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void updateTrades() {
        // Exclusively Lumenwilds goods, paid in Overworld valuables. Build the full pool, then offer a
        // random handful so two Sporemen rarely carry the same wares.
        List<MerchantOffer> pool = new ArrayList<>();
        // Common — emerald-priced everyday Sporefall/flora goods.
        pool.add(buy(Items.EMERALD, 1, new ItemStack(ModItems.GLOWCAP_SPORES.get(), 4), 12));
        pool.add(buy(Items.EMERALD, 1, new ItemStack(ModItems.SPORE_SAC.get(), 2), 12));
        pool.add(buy(Items.EMERALD, 1, new ItemStack(ModItems.GLOW_POLLEN.get(), 6), 12));
        pool.add(buy(Items.EMERALD, 2, new ItemStack(ModBlocks.MOONBLOSSOM.get(), 3), 8));
        pool.add(buy(Items.EMERALD, 2, new ItemStack(ModBlocks.GLOW_FERN.get(), 4), 8));
        pool.add(buy(Items.EMERALD, 2, new ItemStack(ModBlocks.GIANT_GLOWCAP_BLOCK.get(), 1), 8));
        pool.add(buy(Items.EMERALD, 3, new ItemStack(ModItems.LUMEN_FRUIT.get(), 2), 8));
        pool.add(buy(Items.EMERALD, 3, new ItemStack(ModItems.GLOWCAP_STEW.get(), 1), 6));
        // Premium — bigger / gold / diamond trades for the rarer living-light goods.
        pool.add(buy(Items.GOLD_INGOT, 1, new ItemStack(ModItems.SPOREFIN.get(), 2), 6));
        pool.add(buy(Items.EMERALD, 5, new ItemStack(ModItems.LUMEN_CRYSTAL_SHARD.get(), 2), 6));
        pool.add(buy(Items.EMERALD, 5, new ItemStack(ModItems.LUMEN_NECTAR.get(), 2), 6));
        pool.add(buy(Items.EMERALD, 6, new ItemStack(ModBlocks.LUMENBULB.get(), 2), 5));
        pool.add(buy(Items.DIAMOND, 1, new ItemStack(ModItems.MEMORY_CRYSTAL_SHARD.get(), 1), 4));
        pool.add(buy(Items.DIAMOND, 1, new ItemStack(ModBlocks.LUMEN_CRYSTAL_BLOCK.get(), 1), 4));

        Collections.shuffle(pool, new java.util.Random(this.random.nextLong()));
        MerchantOffers offers = this.getOffers();
        int count = 6 + this.random.nextInt(3); // 6–8 wares
        for (int i = 0; i < count && i < pool.size(); i++) {
            offers.add(pool.get(i));
        }
    }

    private MerchantOffer buy(ItemLike cost, int costCount, ItemStack result, int maxUses) {
        return new MerchantOffer(new ItemCost(cost, costCount), result, maxUses, 2, 0.05F);
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int xp = 3 + this.random.nextInt(4);
            this.level()
                    .addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), xp));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? SoundEvents.WANDERING_TRADER_TRADE : SoundEvents.WANDERING_TRADER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WANDERING_TRADER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WANDERING_TRADER_DEATH;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean playSound) {
        return playSound ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }
}
