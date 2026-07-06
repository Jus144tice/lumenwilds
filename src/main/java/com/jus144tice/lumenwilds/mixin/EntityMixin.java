/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.mixin;

import com.jus144tice.lumenwilds.registry.ModFluidTypes;
import com.jus144tice.lumenwilds.registry.ModItems;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes <b>Lumenwater behave as real water</b> at the engine level, instead of patching each water interaction
 * one at a time. NeoForge routes every water check ({@code isInWater}, buoyancy/swimming, the fall-distance
 * reset, eye-in-water breathing) through the entity's per-{@link FluidType} state, keyed on the <em>vanilla</em>
 * {@code WATER_TYPE} (see {@code IEntityExtension#isInFluidType} → {@code getFluidTypeHeight}, and
 * {@code isEyeInFluid}). Lumenwater carries its own {@link ModFluidTypes#LUMENWATER_TYPE} (so it can render
 * teal + glow), so those checks never saw it.
 *
 * <p>This bridges the two chokepoints: a query for the water type's height also counts any Lumenwater the
 * entity is standing in, and the eye-in-water check also fires when the eyes are in Lumenwater. From there the
 * vanilla code does the rest — {@code wasTouchingWater}/{@code isInWater()} go true (swim physics, no fall
 * damage, boats, fishing), and {@code isUnderWater()}/breathing work — while the fluid keeps its own teal fog
 * (the eye <em>type</em> stays Lumenwater for rendering). One systemic fix, not a pile of event handlers.</p>
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    protected Object2DoubleMap<FluidType> forgeFluidTypeHeight;

    @Shadow
    private FluidType forgeFluidTypeOnEyes;

    /** A height query for the vanilla water type also counts Lumenwater → the entity "is in water". */
    @Inject(method = "getFluidTypeHeight", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$lumenwaterIsWater(FluidType type, CallbackInfoReturnable<Double> cir) {
        if (type == NeoForgeMod.WATER_TYPE.value()) {
            FluidType lumen = ModFluidTypes.LUMENWATER_TYPE.value();
            if (forgeFluidTypeHeight.containsKey(lumen)) {
                double lumenHeight = forgeFluidTypeHeight.getDouble(lumen);
                if (lumenHeight > 0.0) {
                    double waterHeight =
                            forgeFluidTypeHeight.containsKey(type) ? forgeFluidTypeHeight.getDouble(type) : 0.0;
                    cir.setReturnValue(Math.max(lumenHeight, waterHeight));
                }
            }
        }
    }

    /** Eyes in Lumenwater count as eyes in water (breathing/drowning + the underwater state). */
    @Inject(method = "isEyeInFluid", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$lumenwaterEyeIsWater(TagKey<Fluid> tag, CallbackInfoReturnable<Boolean> cir) {
        if (tag == FluidTags.WATER && forgeFluidTypeOnEyes == ModFluidTypes.LUMENWATER_TYPE.value()) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Luminite Umbrella rain shield: while a living entity holds the umbrella in either hand, it is NOT counted
     * as being in rain. {@code isInRain} is private but backs the public {@code isInWaterOrRain} /
     * {@code isInWaterRainOrBubble} that water-allergy race/class mods read, so this shields the wielder from
     * the rain specifically (real water is unaffected — that goes through the Lumenwater/water path above).
     */
    @Inject(method = "isInRain", at = @At("HEAD"), cancellable = true)
    private void lumenwilds$umbrellaShieldsRain(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof LivingEntity living
                && (living.getMainHandItem().is(ModItems.LUMINITE_UMBRELLA.get())
                        || living.getOffhandItem().is(ModItems.LUMINITE_UMBRELLA.get()))) {
            cir.setReturnValue(false);
        }
    }
}
