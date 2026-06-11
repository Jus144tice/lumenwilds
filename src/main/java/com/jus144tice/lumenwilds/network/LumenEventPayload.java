/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.network;

import com.jus144tice.lumenwilds.util.ResourceLocationHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Server → client sync of the active {@link com.jus144tice.lumenwilds.world.event.LumenEvent} (by ordinal
 * id) + its remaining ticks (Phase 7d.2). Sent to Lumenwilds players when the event changes (and on join);
 * the client stores it in {@link LumenEventClientState} for the sky / particle effects to read.
 */
public record LumenEventPayload(int eventId, int ticksRemaining) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LumenEventPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocationHelper.modLoc("lumen_event"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LumenEventPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            LumenEventPayload::eventId,
            ByteBufCodecs.VAR_INT,
            LumenEventPayload::ticksRemaining,
            LumenEventPayload::new);

    @Override
    public CustomPacketPayload.Type<LumenEventPayload> type() {
        return TYPE;
    }
}
