package ru.eqour.timetable.notifier.api;

public interface FileActualizer {

    boolean actualize();
    byte[] getActualFile();
}
