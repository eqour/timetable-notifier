package ru.eqour.timetable.api;

public interface FileActualizer {

    boolean actualize();
    byte[] getActualFile();
}
