package ru.eqour.timetable.settings;

import ru.eqour.timetable.model.Week;

import java.util.Objects;

public class Settings {

    public int maxDelayAfterChange;
    public String vkToken;
    public String telegramToken;
    public String timetableFileId;
    public Week savedWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return maxDelayAfterChange == settings.maxDelayAfterChange && vkToken.equals(settings.vkToken) && telegramToken.equals(settings.telegramToken) && timetableFileId.equals(settings.timetableFileId) && Objects.equals(savedWeek, settings.savedWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDelayAfterChange, vkToken, telegramToken, timetableFileId, savedWeek);
    }
}
