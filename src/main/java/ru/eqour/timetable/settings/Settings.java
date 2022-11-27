package ru.eqour.timetable.settings;

import ru.eqour.timetable.model.Week;

import java.util.Objects;

public class Settings {

    public String vkToken;
    public String telegramToken;
    public String timetableFileId;
    public Week savedWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(vkToken, settings.vkToken) && Objects.equals(telegramToken, settings.telegramToken) && Objects.equals(timetableFileId, settings.timetableFileId) && Objects.equals(savedWeek, settings.savedWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vkToken, telegramToken, timetableFileId, savedWeek);
    }
}
