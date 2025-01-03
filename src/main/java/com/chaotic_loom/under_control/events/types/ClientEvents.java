package com.chaotic_loom.under_control.events.types;

import com.chaotic_loom.under_control.events.Event;
import com.chaotic_loom.under_control.events.EventFactory;
import com.chaotic_loom.under_control.events.EventResult;

public class ClientEvents {
    public static final Event<ResourcePackReload> RESOURCE_PACK_RELOAD = EventFactory.createArray(ResourcePackReload.class, callbacks -> (forceReload) -> {
        for (ResourcePackReload callback : callbacks) {
            EventResult result = callback.onResourcePackReload(forceReload);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface ResourcePackReload {
        EventResult onResourcePackReload(boolean forceReload);
    }
}
