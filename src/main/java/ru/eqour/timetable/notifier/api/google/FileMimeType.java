package ru.eqour.timetable.notifier.api.google;

public enum FileMimeType {

    OFFICE_SPREADSHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    GOOGLE_SPREADSHEET("application/vnd.google-apps.spreadsheet");

    public final String value;

    FileMimeType(String value) {
        this.value = value;
    }
}
