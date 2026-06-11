/*
 * Copyright 2026 The Lumenwilds contributors.
 * Licensed under the Apache License, Version 2.0.
 */
package com.jus144tice.lumenwilds.network;

import com.jus144tice.lumenwilds.Lumenwilds;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers The Lumenwilds' network payloads (Phase 7d.2). Mod-bus.
 *
 * <p>Currently just the {@link LumenEventPayload} (server → client ambient-event sync). The handler runs only
 * on the client and writes the common {@link LumenEventClientState} (no client-only types, so this class is
 * safe to load on the dedicated server during registration).</p>
 */
@EventBusSubscriber(modid = Lumenwilds.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModNetworking {

    private ModNetworking() {}

    @SubscribeEvent
    public static void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(LumenEventPayload.TYPE, LumenEventPayload.CODEC, ModNetworking::handleOnClient);
    }

    private static void handleOnClient(final LumenEventPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> LumenEventClientState.set(payload.eventId(), payload.ticksRemaining()));
    }
}
