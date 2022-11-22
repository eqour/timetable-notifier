package ru.eqour.timetable.actualizer;

import ru.eqour.timetable.util.JsonFileHelper;

public class SettingsManager {

    private final String settingsPath;

    public SettingsManager(String settingsPath) {
        this.settingsPath = settingsPath;
    }

    public void save(Settings settings) {
        JsonFileHelper.saveToFile(settingsPath, settings);
    }

    public Settings load() {
        try {
            return JsonFileHelper.loadFromFile(settingsPath, Settings.class);
        } catch (Exception e) {
            return null;
        }
    }
}
