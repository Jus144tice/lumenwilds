/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.block;

import com.jus144tice.lumenwilds.registry.ModBlockEntities;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The Lumen Anchor's block entity (Phase 8c): stores the {@link GlobalPos} of the anchor this one is
 * <b>linked</b> to (the partner, possibly in another dimension), or {@code null} if unlinked. Two anchors are
 * paired by {@link LumenAnchorBlock} (right-click both with the Lumen Striker); the portal teleporter reads
 * the link via {@code portal.LumenAnchorLinks} to route return travel precisely instead of find-or-building
 * at scaled coordinates.
 */
public class LumenAnchorBlockEntity extends BlockEntity {

    @Nullable
    private GlobalPos linkedTo;

    public LumenAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LUMEN_ANCHOR.get(), pos, state);
    }

    @Nullable
    public GlobalPos getLinkedTo() {
        return this.linkedTo;
    }

    public void setLinkedTo(@Nullable GlobalPos target) {
        this.linkedTo = target;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.linkedTo != null) {
            tag.putString("LinkDim", this.linkedTo.dimension().location().toString());
            tag.put("LinkPos", NbtUtils.writeBlockPos(this.linkedTo.pos()));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("LinkDim") && tag.contains("LinkPos")) {
            ResourceKey<Level> dim =
                    ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("LinkDim")));
            NbtUtils.readBlockPos(tag, "LinkPos").ifPresent(pos -> this.linkedTo = GlobalPos.of(dim, pos));
        } else {
            this.linkedTo = null;
        }
    }
}
