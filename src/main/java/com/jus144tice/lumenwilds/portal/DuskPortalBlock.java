/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.portal;

import com.jus144tice.lumenwilds.registry.ModParticles;
import com.jus144tice.lumenwilds.world.LumenDimensionConstants;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Dusk Portal interior block ({@code lumenwilds:dusk_portal}) — a sibling of {@link LumenPortalBlock} that
 * links the <b>Lumenwilds ↔ the Nether</b> instead of the Overworld. A Duskglass frame lit with flint &amp; steel
 * fills with this block (see {@code event.DuskPortalIgnitionEvents}). It implements vanilla's {@link Portal}, so
 * dwell/teleport/cooldown are engine-driven exactly like the Nether portal, but routed between
 * {@code lumenwilds:lumenwilds} and {@code minecraft:the_nether} at Nether-scale (8:1) coordinates.
 */
public class DuskPortalBlock extends Block implements Portal {

    public static final MapCodec<DuskPortalBlock> CODEC = simpleCodec(DuskPortalBlock::new);

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    private static final int PLAYER_DWELL_TICKS = 80;

    private static final VoxelShape X_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    private static final VoxelShape Z_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public DuskPortalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected MapCodec<? extends DuskPortalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return entity instanceof Player ? PLAYER_DWELL_TICKS : 0;
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        // Dusk portals only bridge the Lumenwilds and the Nether — inert anywhere else.
        boolean inLumenwilds = level.dimension() == LumenDimensionConstants.LUMENWILDS_LEVEL;
        boolean inNether = level.dimension() == Level.NETHER;
        if (!inLumenwilds && !inNether) {
            return null;
        }
        ResourceKey<Level> targetKey = inLumenwilds ? Level.NETHER : LumenDimensionConstants.LUMENWILDS_LEVEL;
        ServerLevel target = level.getServer().getLevel(targetKey);
        if (target == null) {
            return null;
        }
        double scale = DimensionType.getTeleportationScale(level.dimensionType(), target.dimensionType());
        WorldBorder border = target.getWorldBorder();
        BlockPos approx = border.clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);
        Direction.Axis axis =
                entity.level().getBlockState(pos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
        return DuskPortalTeleporter.createDestinationTransition(target, entity, approx, axis);
    }

    @Override
    public Portal.Transition getLocalTransition() {
        // Nether-linking portal → the familiar nether-portal shimmer/nausea, distinguishing it from the calm
        // Lumen portal (which is Transition.NONE + a bespoke teal overlay).
        return Portal.Transition.CONFUSION;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false);
        }
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            double vx = (random.nextDouble() - 0.5) * 0.2;
            double vy = (random.nextDouble() - 0.5) * 0.2;
            double vz = (random.nextDouble() - 0.5) * 0.2;
            level.addParticle(ModParticles.CRYSTAL_SHIMMER.get(), x, y, z, vx, vy, vz);
        }
    }
}
