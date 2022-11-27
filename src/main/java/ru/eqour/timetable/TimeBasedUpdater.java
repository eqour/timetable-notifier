package ru.eqour.timetable;

import java.util.function.Supplier;

public class TimeBasedUpdater {

    private final Supplier<Boolean> supplier;
    private final Runnable runnable;
    private final int maxPeriod;

    private long lastUpdateInMinutes;
    private int currentPeriod;
    private boolean timerIsEnabled;

    public TimeBasedUpdater(Supplier<Boolean> supplier, Runnable runnable, int maxPeriod) {
        this.supplier = supplier;
        this.runnable = runnable;
        this.maxPeriod = maxPeriod;
        lastUpdateInMinutes = -1;
        currentPeriod = 0;
        timerIsEnabled = false;
    }

    public void update(long timeInMilliseconds) {
        int MILLISECONDS_IN_MINUTE = 60 * 1000;
        long timeInMinutes = timeInMilliseconds / MILLISECONDS_IN_MINUTE;
        if (timeInMinutes < lastUpdateInMinutes) {
            throw new IllegalArgumentException();
        }
        if (lastUpdateInMinutes < timeInMinutes) {
            lastUpdateInMinutes = timeInMinutes;
            if (supplier.get()) {
                timerIsEnabled = true;
                currentPeriod = 0;
            } else if (timerIsEnabled) {
                currentPeriod++;
                if (currentPeriod >= maxPeriod) {
                    timerIsEnabled = false;
                    runnable.run();
                }
            }
        }
    }
}
