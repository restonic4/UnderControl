package com.chaotic_loom.under_control.util;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.networking.packets.client_to_server.AskServerTime;

import java.util.*;

public class SynchronizationHelper {
    public static final long THRESHOLD = 2500;

    private static final List<SynchronizationHelper> helperInstances = new ArrayList<>();

    private final long id;

    private final LinkedList<Long> offsets;
    private final int maxSize;

    private long averageOffset;
    private boolean dirty;

    private long lastAdjustment = 0;
    private long lastRTT = 0;

    public SynchronizationHelper() {
        this.id = MathHelper.getUniqueID();

        this.offsets = new LinkedList<>();
        this.maxSize = 128;

        this.averageOffset = 0;
        this.dirty = true;

        helperInstances.add(this);
    }

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

    public void askForMultipleSynchronizations(int count, int delayMs) {
        new Thread(() -> {
            for (int i = 0; i < count; i++) {
                AskServerTime.sendToServer(this.id);
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void schedulePeriodicSynchronization(long intervalMs) {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                askForSynchronization();
            }
        }, 0, intervalMs);
    }

    public long synchronizeTime(long clientSendTime, long serverTime) {
        long clientReceiveTime = System.currentTimeMillis();

        long rtt = clientReceiveTime - clientSendTime;
        long adjustment = ((serverTime - clientSendTime) + (serverTime - clientReceiveTime)) / 2;

        if (rtt > THRESHOLD) {
            return 0;
        }

        lastRTT = rtt;
        lastAdjustment = adjustment;

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
        if (!offsets.isEmpty()) {
            if (Math.abs(offset - getAverageOffset()) > THRESHOLD) {
                return;
            }
        }
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

    public void dump() {
        UnderControl.LOGGER.info("SynchronizationHelper dump:");
        UnderControl.LOGGER.info("---------------------------");
        UnderControl.LOGGER.info("ID: {}", id);
        UnderControl.LOGGER.info("Max size: {}", maxSize);
        UnderControl.LOGGER.info("Size: {}", offsets.size());
        UnderControl.LOGGER.info("Average offset: {}", averageOffset);
        UnderControl.LOGGER.info("Dirty: {}", dirty);
        UnderControl.LOGGER.info("Last adjustment: {}", lastAdjustment);
        UnderControl.LOGGER.info("Last RTT: {}", lastRTT);
        UnderControl.LOGGER.info("---------------------------");
    }
}
