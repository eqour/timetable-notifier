package ru.eqour.timetable.settings;

/**
 * Описывает интерфейс доступа к настройкам приложения.
 */
public interface SettingsManager {

    /**
     * Сохраняет настройки.
     *
     * @param settings настройки приложения.
     */
    void save(Settings settings);

    /**
     * Загружает настройки.
     *
     * @return настройки приложения.
     */
    Settings load();
}
