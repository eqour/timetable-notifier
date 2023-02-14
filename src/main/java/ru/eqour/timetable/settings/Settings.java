package ru.eqour.timetable.settings;

import java.util.Objects;

/**
 * Настройки приложения.
 */
public class Settings {

    /**
     * Задержка в минутах с последнего изменения расписания.
     */
    public int maxDelayAfterChange;

    /**
     * Смещение в часах от UTC.
     */
    public int zoneOffset;

    /**
     * Период, за который будет происходить актуализация расписания.
     */
    public int parsingPeriod;

    /**
     * Токен для доступа к боту в ВКонтакте.
     */
    public String vkToken;

    /**
     * Токен для доступа к боту Telegram.
     */
    public String telegramToken;

    /**
     * Идентификатор файла расписания в Google Drive.
     */
    public String timetableFileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return maxDelayAfterChange == settings.maxDelayAfterChange && zoneOffset == settings.zoneOffset && parsingPeriod == settings.parsingPeriod && vkToken.equals(settings.vkToken) && telegramToken.equals(settings.telegramToken) && timetableFileId.equals(settings.timetableFileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDelayAfterChange, zoneOffset, parsingPeriod, vkToken, telegramToken, timetableFileId);
    }
}
