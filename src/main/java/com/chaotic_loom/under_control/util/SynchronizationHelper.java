package com.chaotic_loom.under_control.util;

import com.chaotic_loom.under_control.networking.packets.client_to_server.AskServerTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SynchronizationHelper {
    private static final List<SynchronizationHelper> helperInstances = new ArrayList<>();

    private final long id;

    private final LinkedList<Long> offsets;
    private final int maxSize;

    private long averageOffset;
    private boolean dirty;

    public SynchronizationHelper(int maxSize) {
        this.id = MathHelper.getUniqueID();

        this.offsets = new LinkedList<>();
        this.maxSize = maxSize;

        this.averageOffset = 0;
        this.dirty = true;

        helperInstances.add(this);
    }

    public static SynchronizationHelper getHelper(long id) {
        for (SynchronizationHelper synchronizationHelper : helperInstances) {
            if (synchronizationHelper.id == id) {
                return synchronizationHelper;
            }
        }

        return null;
    }

    public void askForSynchronization() {
        AskServerTime.sendToServer(this.id);
    }

    public long synchronizeTime(long clientSendTime, long serverTime) {
        long clientReceiveTime = System.currentTimeMillis();

        long rtt = clientReceiveTime - clientSendTime;

        long adjustment = serverTime + (rtt / 2) - clientSendTime;

        addOffset(adjustment);

        return adjustment;
    }

    public long getAverageOffset() {
        if (dirty) {
            recalculateAverage();
        }
        return averageOffset;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis() + getAverageOffset();
    }

    private void addOffset(long offset) {
        if (offsets.size() >= maxSize) {
            offsets.removeFirst();
        }
        offsets.add(offset);
        dirty = true;
    }

    public void clearOffsets() {
        offsets.clear();
        dirty = true;
    }

    private void recalculateAverage() {
        if (offsets.isEmpty()) {
            averageOffset = 0;
        } else {
            averageOffset = offsets.stream().mapToLong(Long::longValue).sum() / offsets.size();
        }
        dirty = false;
    }
}
