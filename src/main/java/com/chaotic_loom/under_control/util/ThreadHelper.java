package com.chaotic_loom.under_control.util;

import java.util.function.Consumer;

public class ThreadHelper {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {}
    }

    public static void runCountDown(int desiredSeconds, Runnable runnable, Consumer<Integer> consumer) {
        for (int i = desiredSeconds; i >= 0; i--) {
            consumer.accept(i);

            if (i != 0) {
                sleep(1000);
            }
        }

        if (runnable != null) {
            runnable.run();
        }
    }

    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
}
