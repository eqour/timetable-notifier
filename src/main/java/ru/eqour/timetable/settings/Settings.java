package ru.eqour.timetable.settings;

import ru.eqour.timetable.model.Week;

import java.util.List;
import java.util.Objects;

public class Settings {

    public int maxDelayAfterChange;
    public int zoneOffset;
    public int parsingPeriod;
    public String vkToken;
    public String telegramToken;
    public String timetableFileId;
    public List<Week> savedWeeks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return maxDelayAfterChange == settings.maxDelayAfterChange && zoneOffset == settings.zoneOffset && parsingPeriod == settings.parsingPeriod && vkToken.equals(settings.vkToken) && telegramToken.equals(settings.telegramToken) && timetableFileId.equals(settings.timetableFileId) && Objects.equals(savedWeeks, settings.savedWeeks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDelayAfterChange, zoneOffset, parsingPeriod, vkToken, telegramToken, timetableFileId, savedWeeks);
    }
}
