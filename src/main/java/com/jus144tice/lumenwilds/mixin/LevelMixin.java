/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.mixin;

import com.jus144tice.lumenwilds.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Luminite Umbrella — the <b>world-position</b> half of the rain shield (v1.7.1).
 *
 * <p>{@code mixin.EntityMixin} already shields the <em>entity-based</em> rain checks ({@code isInRain} →
 * {@code isInWaterOrRain}/{@code isInWaterRainOrBubble}). But water-allergy race mods (e.g. Origins/Apoli's
 * {@code apoli:in_rain} condition) query {@link Level#isRainingAt(BlockPos)} — a <b>position</b> check that
 * ignores the entity entirely, so the entity mixin can't cover it. This makes {@code isRainingAt} report "not
 * raining" for the column a Luminite-Umbrella-holding <b>player</b> occupies, so the rain damage never fires.</p>
 *
 * <p>Server-only + gated on {@code isRaining()} (matching vanilla's own first check) and scans only
 * {@link Level#players()} (a tiny list, no AABB query) so it costs nothing when it's dry or no player holds an
 * umbrella. The client still <em>renders</em> rain — only the gameplay/damage query is shielded.</p>
 */
@Mixin(Level.class)
public abstract class LevelMixin {

    @Inject(method = "isRainingAt", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$umbrellaBlocksRainAt(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Level self = (Level) (Object) this;
        if (self.isClientSide || !self.isRaining()) {
            return;
        }
        for (Player player : self.players()) {
            BlockPos at = player.blockPosition();
            // Apoli queries the entity's feet AND its max-Y column; ±3 in Y covers a standing player.
            if (at.getX() == pos.getX()
                    && at.getZ() == pos.getZ()
                    && Math.abs(at.getY() - pos.getY()) <= 3
                    && lumenwilds$holdsUmbrella(player)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }

    @Unique
    private static boolean lumenwilds$holdsUmbrella(Player player) {
        return player.getMainHandItem().is(ModItems.LUMINITE_UMBRELLA.get())
                || player.getOffhandItem().is(ModItems.LUMINITE_UMBRELLA.get());
    }
}
