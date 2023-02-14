package ru.eqour.timetable.util;

import java.nio.file.Paths;

/**
 * Константы приложения.
 */
public enum AppConstants {

    /**
     * Путь к директории запуска приложения.
     */
    APP_PATH(Paths.get(System.getProperty("user.dir")).toString()),

    /**
     * Путь к директории с данными приложения.
     */
    APP_DATA_PATH(Paths.get(APP_PATH.toString(), "data").toString()),

    /**
     * Путь к файлу настроек приложения.
     */
    SETTINGS_PATH(Paths.get(APP_PATH.toString(), "settings.json").toString()),

    /**
     * Путь к файлу с кэшем последней полученной информации о расписании.
     */
    TIMETABLE_CACHE_PATH(Paths.get(APP_DATA_PATH.toString(), "timetable-cache.json").toString());

    private final String value;

    AppConstants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
