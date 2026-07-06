/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Glowvine — the dimension's glowing, passable, climbable hanging strand, with vanilla-vine <b>sever</b>
 * behaviour. A glowvine is supported by another glowvine directly above it (the hanging chain) OR by any
 * sturdy neighbouring face (a ceiling, a wall, or the ground it creeps over). When it loses all support it
 * breaks ({@link #updateShape} → air), which cascades down the strand — so cutting or un-hooking the top of a
 * free-hanging strand drops the whole thing, cutting the middle drops everything below while the part still
 * anchored above stays, exactly like a vanilla vine (see {@code VineBlock}).
 *
 * <p>The support rule is a deliberate <em>superset</em> of vanilla's (vanilla vines can't rest on the ground):
 * glowvine is also generated embedded as glowing veins inside cave rock (the {@code undercrown_glowvine} ore
 * feature) and crept over ruins as ground/wall cover ({@code world.structure.VestigeDecay}) — all of which
 * have a solid neighbour, so they stay put. Only a strand hanging in open air (supported solely by the vine
 * above) severs, which is precisely the case players were seeing float.</p>
 */
public class GlowvineBlock extends Block {

    public static final MapCodec<GlowvineBlock> CODEC = simpleCodec(GlowvineBlock::new);

    public GlowvineBlock(Properties props) {
        super(props);
    }

    @Override
    public MapCodec<GlowvineBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Hanging chain: a glowvine directly above holds this one (its own faces aren't "sturdy", so this must
        // be an explicit check).
        if (level.getBlockState(pos.above()).is(this)) {
            return true;
        }
        // Otherwise cling to any sturdy face around it — ceiling, wall, or the floor it creeps on.
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            if (level.getBlockState(neighbor).isFaceSturdy(level, neighbor, dir.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction dir,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos) {
        // Lost all support -> break. Setting air here cascades the same check onto the glowvine below, severing
        // the strand the rest of the way down (drops per the block's loot table, like a broken vanilla vine).
        return canSurvive(state, level, pos)
                ? super.updateShape(state, dir, neighborState, level, pos, neighborPos)
                : Blocks.AIR.defaultBlockState();
    }
}
