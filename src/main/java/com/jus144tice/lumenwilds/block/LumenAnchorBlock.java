/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModItems;
import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Lumen Anchor (Phase 8c) — the bible's portal-stabilization device. A block with a
 * {@link LumenAnchorBlockEntity} that stores a link to a partner anchor (possibly cross-dimension). Placed
 * near a Lumen Portal, a linked anchor makes return travel land at its partner precisely (see
 * {@code portal.LumenAnchorLinks} + the override in {@code portal.LumenPortalBlock}).
 *
 * <p><b>Linking:</b> right-click an anchor with the {@link ModItems#LUMEN_STRIKER} to select it, then
 * right-click a second anchor to pair them (each stores the other). The select is per-player + transient.</p>
 */
public class LumenAnchorBlock extends BaseEntityBlock {

    public static final MapCodec<LumenAnchorBlock> CODEC = simpleCodec(LumenAnchorBlock::new);

    /** Transient per-player "first anchor picked" while linking (cleared on link). */
    private static final Map<UUID, GlobalPos> PENDING = new HashMap<>();

    public LumenAnchorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL; // a normal-looking block (BaseEntityBlock defaults to INVISIBLE)
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LumenAnchorBlockEntity(pos, state);
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
        if (!stack.is(ModItems.LUMEN_STRIKER.get())) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return ItemInteractionResult.sidedSuccess(true);
        }

        GlobalPos here = GlobalPos.of(level.dimension(), pos);
        GlobalPos pending = PENDING.get(player.getUUID());
        if (pending == null) {
            PENDING.put(player.getUUID(), here);
            player.displayClientMessage(Component.translatable("lumenwilds.anchor.selected"), true);
        } else if (pending.equals(here)) {
            player.displayClientMessage(Component.translatable("lumenwilds.anchor.same"), true);
        } else {
            link(serverLevel.getServer(), here, pending);
            PENDING.remove(player.getUUID());
            player.displayClientMessage(Component.translatable("lumenwilds.anchor.linked"), true);
            level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.2F);
        }
        return ItemInteractionResult.sidedSuccess(false);
    }

    /** Pair two anchors so each stores the other (loads the partner's chunk via {@code getBlockEntity}). */
    private static void link(MinecraftServer server, GlobalPos a, GlobalPos b) {
        setLink(server, a, b);
        setLink(server, b, a);
    }

    private static void setLink(MinecraftServer server, GlobalPos at, GlobalPos target) {
        ServerLevel level = server.getLevel(at.dimension());
        if (level != null && level.getBlockEntity(at.pos()) instanceof LumenAnchorBlockEntity anchor) {
            anchor.setLinkedTo(target);
        }
    }
}
