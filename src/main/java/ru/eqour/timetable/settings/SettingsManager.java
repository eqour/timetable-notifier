package ru.eqour.timetable.settings;

public interface SettingsManager {

    void save(Settings settings);
    Settings load();
}
