package com.restonic4.under_control.events.types;

import com.restonic4.under_control.events.Event;
import com.restonic4.under_control.events.EventFactory;
import com.restonic4.under_control.events.EventResult;

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
