package ru.eqour.timetable.util.time;

import java.util.function.Supplier;

/**
 * Реализует механизм временного лага.
 */
public class TimeBasedUpdater {

    private final Supplier<Boolean> supplier;
    private final Runnable runnable;
    private final int maxPeriod;

    private long lastUpdateInMinutes;
    private int currentPeriod;
    private boolean timerIsEnabled;

    /**
     * Создаёт новый экземпляр класса {@code TimeBasedUpdater}.
     *
     * @param supplier возвращает {@code true} при изменении некоторого состояния и {@code false}, если состояние не
     *                 менялось.
     * @param runnable метод, который будет вызван при обновлении.
     * @param maxPeriod количество минут с последнего возвращённого {@code true} из {@code supplier}, после которого
     *                  произойдёт вызов {@code runnable}.
     */
    public TimeBasedUpdater(Supplier<Boolean> supplier, Runnable runnable, int maxPeriod) {
        this.supplier = supplier;
        this.runnable = runnable;
        this.maxPeriod = maxPeriod;
        lastUpdateInMinutes = -1;
        currentPeriod = 0;
        timerIsEnabled = false;
    }

    /**
     * Обновляет счётчик. Высчитывается разница по времени {@code timeInMilliseconds} с предыдущего и текущего
     * вызова метода.
     *
     * @param timeInMilliseconds метка времени в миллисекундах.
     */
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
            }
            if (timerIsEnabled) {
                if (currentPeriod >= maxPeriod) {
                    timerIsEnabled = false;
                    runnable.run();
                }
                currentPeriod++;
            }
        }
    }
}
