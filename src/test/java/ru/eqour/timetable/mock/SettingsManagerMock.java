package ru.eqour.timetable.mock;

import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.settings.SettingsManager;

public class SettingsManagerMock implements SettingsManager {

    private final boolean withBadSave, withBadLoad;
    private Settings settings;
    private int saveCalls, loadCalls;

    public SettingsManagerMock(boolean withBadSave, boolean withBadLoad, Settings settings) {
        this.withBadSave = withBadSave;
        this.withBadLoad = withBadLoad;
        this.settings = settings;
    }

    @Override
    public void save(Settings settings) {
        saveCalls++;
        if (withBadSave) {
            throw new RuntimeException();
        } else {
            this.settings = settings;
        }
    }

    @Override
    public Settings load() {
        loadCalls++;
        if (withBadLoad) {
            return null;
        } else {
            return settings;
        }
    }

    public int getSaveCalls() {
        return saveCalls;
    }

    public int getLoadCalls() {
        return loadCalls;
    }
}
