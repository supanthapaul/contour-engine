package org.supanthapaul.util;

public class Time {
    public static float timeStarted = System.nanoTime();

    // seconds passed since the application started
    public static float getTime() {
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
