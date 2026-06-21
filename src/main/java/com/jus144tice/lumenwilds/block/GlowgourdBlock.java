/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlocks;
import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Glowgourd (v1.4 F3) — the pumpkin-analog gourd: shear it to carve a glowing face (Carved Glowgourd, a
 * brighter glowing "lantern"). A faithful port of {@link net.minecraft.world.level.block.PumpkinBlock#useItemOn}
 * keyed to the Lumenwilds blocks/seeds.
 */
public class GlowgourdBlock extends Block {

    public static final MapCodec<GlowgourdBlock> CODEC = simpleCodec(GlowgourdBlock::new);

    public GlowgourdBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public MapCodec<GlowgourdBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (!stack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHEARS_CARVE)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        } else if (level.isClientSide) {
            return ItemInteractionResult.sidedSuccess(true);
        } else {
            Direction face = hit.getDirection();
            Direction facing =
                    face.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : face;
            level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(
                    pos,
                    ModBlocks.CARVED_GLOWGOURD.get().defaultBlockState().setValue(CarvedPumpkinBlock.FACING, facing),
                    11);
            ItemEntity drop = new ItemEntity(
                    level,
                    pos.getX() + 0.5 + facing.getStepX() * 0.65,
                    pos.getY() + 0.1,
                    pos.getZ() + 0.5 + facing.getStepZ() * 0.65,
                    new ItemStack(ModItems.GLOWGOURD_SEEDS.get(), 4));
            drop.setDeltaMovement(
                    0.05 * facing.getStepX() + level.random.nextDouble() * 0.02,
                    0.05,
                    0.05 * facing.getStepZ() + level.random.nextDouble() * 0.02);
            level.addFreshEntity(drop);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            level.gameEvent(player, GameEvent.SHEAR, pos);
            player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            return ItemInteractionResult.sidedSuccess(false);
        }
    }
}
