package ru.eqour.timetable.settings;

import ru.eqour.timetable.model.Week;

import java.util.List;
import java.util.Objects;

public class Settings {

    public int maxDelayAfterChange;
    public String vkToken;
    public String telegramToken;
    public String timetableFileId;
    public List<Week> savedWeeks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return maxDelayAfterChange == settings.maxDelayAfterChange && Objects.equals(vkToken, settings.vkToken) && Objects.equals(telegramToken, settings.telegramToken) && Objects.equals(timetableFileId, settings.timetableFileId) && Objects.equals(savedWeeks, settings.savedWeeks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDelayAfterChange, vkToken, telegramToken, timetableFileId, savedWeeks);
    }
}
