/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.mixin;

import com.jus144tice.lumenwilds.world.time.LumenwildsTimeData;
import net.minecraft.world.level.storage.DerivedLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets the Lumenwilds' {@link DerivedLevelData} hold an <b>independent day clock</b> instead of mirroring the
 * Overworld (Phase 7d). When decoupled (toggled by {@code mixin.ServerLevelMixin} on level load), the day-time
 * getters/setters use private fields rather than the wrapped Overworld data — whose {@code setDayTime} is a
 * no-op, which is exactly why a custom clock was impossible without this. {@code gameTime} is intentionally
 * left derived (still shared with the Overworld); only the day cycle diverges.
 */
@Mixin(DerivedLevelData.class)
public abstract class DerivedLevelDataMixin implements LumenwildsTimeData {

    @Unique
    private boolean lumenwilds$decoupled = false;

    @Unique
    private long lumenwilds$dayTime;

    @Unique
    private float lumenwilds$dayTimePerTick = -1.0F;

    @Unique
    private float lumenwilds$dayTimeFraction = 0.0F;

    @Override
    public void lumenwilds$decouple(long startDayTime, float dayTimePerTick) {
        this.lumenwilds$dayTime = startDayTime;
        this.lumenwilds$dayTimePerTick = dayTimePerTick;
        this.lumenwilds$dayTimeFraction = 0.0F;
        this.lumenwilds$decoupled = true;
    }

    @Override
    public boolean lumenwilds$isDecoupled() {
        return this.lumenwilds$decoupled;
    }

    @Inject(method = "getDayTime", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$getDayTime(CallbackInfoReturnable<Long> cir) {
        if (this.lumenwilds$decoupled) {
            cir.setReturnValue(this.lumenwilds$dayTime);
        }
    }

    @Inject(method = "setDayTime", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$setDayTime(long dayTime, CallbackInfo ci) {
        if (this.lumenwilds$decoupled) {
            this.lumenwilds$dayTime = dayTime;
            ci.cancel();
        }
    }

    @Inject(method = "getDayTimePerTick", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$getDayTimePerTick(CallbackInfoReturnable<Float> cir) {
        if (this.lumenwilds$decoupled) {
            cir.setReturnValue(this.lumenwilds$dayTimePerTick);
        }
    }

    @Inject(method = "setDayTimePerTick", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$setDayTimePerTick(float dayTimePerTick, CallbackInfo ci) {
        if (this.lumenwilds$decoupled) {
            this.lumenwilds$dayTimePerTick = dayTimePerTick;
            ci.cancel();
        }
    }

    @Inject(method = "getDayTimeFraction", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$getDayTimeFraction(CallbackInfoReturnable<Float> cir) {
        if (this.lumenwilds$decoupled) {
            cir.setReturnValue(this.lumenwilds$dayTimeFraction);
        }
    }

    @Inject(method = "setDayTimeFraction", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$setDayTimeFraction(float dayTimeFraction, CallbackInfo ci) {
        if (this.lumenwilds$decoupled) {
            this.lumenwilds$dayTimeFraction = dayTimeFraction;
            ci.cancel();
        }
    }
}
