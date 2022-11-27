package ru.eqour.timetable.settings;

import ru.eqour.timetable.util.JsonFileHelper;

public class SimpleSettingsManager implements SettingsManager {

    private final String settingsPath;

    public SimpleSettingsManager(String settingsPath) {
        this.settingsPath = settingsPath;
    }

    @Override
    public void save(Settings settings) {
        JsonFileHelper.saveToFile(settingsPath, settings);
    }

    @Override
    public Settings load() {
        try {
            return JsonFileHelper.loadFromFile(settingsPath, Settings.class);
        } catch (Exception e) {
            return null;
        }
    }
}
