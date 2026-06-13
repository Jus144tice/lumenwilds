/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import com.jus144tice.lumenwilds.registry.ModParticles;
import com.mojang.serialization.MapCodec;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Lumen Field Projector (Phase 11b) — the player-craftable heart of a liftshaft, looted-tech rebuilt at home.
 * It projects a vertical gravity column out of one face: an Ascension Field upward when its {@link #MODE} is
 * {@code ASCEND}, a Descent Field downward when {@code DESCEND}. Right-click with an empty hand to toggle the
 * mode (chat + tooltip show which). The column is (re)built each tick by its {@link LumenFieldProjectorBlockEntity}
 * via {@link LiftShaftNetwork} — 16 cells, extended by wall-mounted {@link GravityRepeaterBlock}s — and torn down
 * when the projector is removed or its mode flips. Standalone-powered (no Resonance network needed). Glows.
 */
public class LumenFieldProjectorBlock extends BaseEntityBlock {

    public static final MapCodec<LumenFieldProjectorBlock> CODEC = simpleCodec(LumenFieldProjectorBlock::new);

    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    public LumenFieldProjectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MODE, Mode.ASCEND));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(MODE);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LumenFieldProjectorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? null
                : createTickerHelper(
                        type, ModBlockEntities.LUMEN_FIELD_PROJECTOR.get(), LumenFieldProjectorBlockEntity::serverTick);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        Mode next = state.getValue(MODE) == Mode.ASCEND ? Mode.DESCEND : Mode.ASCEND;
        if (!level.isClientSide) {
            // Tear down the current column (old mode/direction) before flipping, so nothing is orphaned.
            if (level.getBlockEntity(pos) instanceof LumenFieldProjectorBlockEntity be) {
                be.clearField(level);
            }
            level.setBlock(pos, state.setValue(MODE, next), 3);
            level.playSound(
                    null,
                    pos,
                    SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.BLOCKS,
                    0.7F,
                    next == Mode.ASCEND ? 1.4F : 0.8F);
        }
        player.displayClientMessage(
                Component.literal("Mode: " + next.label()).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof LumenFieldProjectorBlockEntity be) {
                be.clearField(level);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Active feedback so a placed projector reads as alive: a glint streaming out of its working face + a
        // rare soft chime. The column itself carries the main particles (the field cells' animateTick).
        boolean ascend = state.getValue(MODE) == Mode.ASCEND;
        SimpleParticleType mote = ascend ? ModParticles.ASCENSION_MOTE.get() : ModParticles.DESCENT_MOTE.get();
        double faceY = ascend ? pos.getY() + 1.0 : pos.getY();
        double vy = ascend ? 0.16 : -0.16;
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
            double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
            level.addParticle(mote, x, faceY, z, 0.0, vy, 0.0);
        }
        if (random.nextInt(80) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SoundEvents.AMETHYST_BLOCK_RESONATE,
                    SoundSource.BLOCKS,
                    0.2F,
                    ascend ? 1.4F : 0.7F,
                    false);
        }
    }

    @Override
    public void appendHoverText(
            ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Projects a gravity column. Right-click to toggle Ascension / Descent.")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    /** Which way the projector throws its field: {@code ASCEND} (up) or {@code DESCEND} (down). */
    public enum Mode implements StringRepresentable {
        ASCEND("ascend", "Ascension"),
        DESCEND("descend", "Descent");

        private final String name;
        private final String label;

        Mode(String name, String label) {
            this.name = name;
            this.label = label;
        }

        public String label() {
            return label;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
