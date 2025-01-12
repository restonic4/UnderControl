package com.chaotic_loom.under_control.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThreadHelper {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {}
    }

    public static void runCountDown(int desiredSeconds, Runnable runnable, Predicate<Integer> predicate) {
        for (int i = desiredSeconds; i >= 0 && predicate.test(i); i--) {
            if (i != 0) {
                sleep(1000);
            }
        }

        if (runnable != null) {
            runnable.run();
        }
    }

    public static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
}
