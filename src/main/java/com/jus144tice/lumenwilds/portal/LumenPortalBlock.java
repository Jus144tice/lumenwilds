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
 * The portal interior block ({@code lumenwilds:lumen_portal}).
 *
 * <p>A non-solid, no-collision, light-emitting block that fills the inside of a Lumenbound Stone frame.
 * It implements vanilla's {@link Portal} interface, so entity dwell-timing, teleport, and post-teleport
 * cooldown are driven by the engine via {@link Entity#setAsInsidePortal(Portal, BlockPos)} — exactly
 * like the Nether portal, but routed to {@code lumenwilds:lumenwilds} and back.</p>
 *
 * <p>Travel is deliberately calm: {@link #getLocalTransition()} returns {@link Portal.Transition#NONE}
 * (no nausea/"purple" Nether feel). The teal screen overlay is a separate client effect (Phase 7).</p>
 */
public class LumenPortalBlock extends Block implements Portal {

    public static final MapCodec<LumenPortalBlock> CODEC = simpleCodec(LumenPortalBlock::new);

    /** Portal plane orientation, like the Nether portal's. */
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    /** Ticks a player must dwell before transit (non-players transit immediately). */
    private static final int PLAYER_DWELL_TICKS = 80;

    private static final VoxelShape X_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    private static final VoxelShape Z_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public LumenPortalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected MapCodec<? extends LumenPortalBlock> codec() {
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
        ResourceKey<Level> targetKey = level.dimension() == LumenDimensionConstants.LUMENWILDS_LEVEL
                ? Level.OVERWORLD
                : LumenDimensionConstants.LUMENWILDS_LEVEL;
        ServerLevel target = level.getServer().getLevel(targetKey);
        if (target == null) {
            return null;
        }
        double scale = DimensionType.getTeleportationScale(level.dimensionType(), target.dimensionType());
        WorldBorder border = target.getWorldBorder();
        BlockPos approx = border.clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);
        Direction.Axis axis =
                entity.level().getBlockState(pos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
        return LumenPortalTeleporter.createDestinationTransition(target, entity, approx, axis);
    }

    @Override
    public Portal.Transition getLocalTransition() {
        return Portal.Transition.NONE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Drifting Lumen Spores rising out of the portal (the bespoke atmosphere particle, Phase 7b).
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            double vx = (random.nextDouble() - 0.5) * 0.2;
            double vy = random.nextDouble() * 0.1;
            double vz = (random.nextDouble() - 0.5) * 0.2;
            level.addParticle(ModParticles.LUMEN_SPORE.get(), x, y, z, vx, vy, vz);
        }
    }
}
