package ru.eqour.timetable.watch.api.google;

/**
 * MIME тип файла.
 */
public enum FileMimeType {

    /**
     * Электронная таблица с возможностью открытия в MS Excel.
     */
    OFFICE_SPREADSHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

    /**
     * Электронная таблица в Google Sheets.
     */
    GOOGLE_SPREADSHEET("application/vnd.google-apps.spreadsheet");

    public final String value;

    FileMimeType(String value) {
        this.value = value;
    }
}
